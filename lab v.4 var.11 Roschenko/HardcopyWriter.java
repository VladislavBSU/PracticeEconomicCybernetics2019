import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.*;
import java.util.*;

public class HardcopyWriter extends Writer {
    // These are the instance variables for the class
    protected PrintJob job;                 // The PrintJob object in use
    protected Graphics page;                // Graphics object for current page
    protected String jobname;               // The name of the print job
    protected int fontsize;                 // Point size of the font
    protected String time;                  // Current time (appears in header)
    protected Dimension pagesize;           // Size of the page (in dots)
    protected int pagedpi;                  // Page resolution in dots per inch
    protected Font font, headerfont;        // Body font and header font
    protected FontMetrics metrics;          // Metrics for the body font
    protected FontMetrics headermetrics;    // Metrics for the header font
    protected int x0, y0;                   // Upper-left corner inside margin
    protected int width, height;            // Size (in dots) inside margins
    protected int headery;                  // Baseline of the page header
    protected int charwidth;                // The width of each character
    protected int lineheight;               // The height of each line
    protected int lineascent;               // Offset of font baseline
    protected int chars_per_line;           // Number of characters per line
    protected int lines_per_page;           // Number of lines per page
    protected int charnum = 0, linenum = 0; // Current column and line position
    public int type;
    // A field to save state between invocations of the write() method
    private boolean last_char_was_return = false;
    // A static variable that holds user preferences between print jobs
    protected static Properties printprops = new Properties();
    static Dimension SIZE = new Dimension(600, 600);

    public HardcopyWriter(Frame frame, String jobname, int fontsize, double leftmargin, double rightmargin, double topmargin, double bottommargin) throws HardcopyWriter.PrintCanceledException
    {
        Toolkit toolkit = frame.getToolkit();   // get Toolkit from Frame
        synchronized(printprops) {
            PageAttributes pa = new PageAttributes();
            pa.setOrientationRequested(PageAttributes.OrientationRequestedType.LANDSCAPE);
            JobAttributes ja = new JobAttributes();
            ja.setSides(JobAttributes.SidesType.TWO_SIDED_LONG_EDGE);
            job = toolkit.getPrintJob(frame, jobname, ja, pa);
            if(pa.getOrientationRequested()==PageAttributes.OrientationRequestedType.PORTRAIT)
            { type = 0; }
            else
            { type = 1;}
        }
        if (job == null)
        { throw new PrintCanceledException("User cancelled print request"); }

        pagesize = job.getPageDimension();      // query the page size
        pagedpi = job.getPageResolution();      // query the page resolution

        if (System.getProperty("os.name").regionMatches(true, 0, "windows", 0, 7)){
            pagedpi = toolkit.getScreenResolution();
            pagesize = new Dimension((int)(8.5 * pagedpi),(int)( 11*pagedpi));
            fontsize = fontsize * pagedpi / 72;
        }

        x0 = (int)(leftmargin * pagedpi);
        y0 = (int)(topmargin * pagedpi);
        width = pagesize.width - (int)((leftmargin + rightmargin) * pagedpi) - 200;
        height = pagesize.height - (int)((topmargin + bottommargin) * pagedpi);
        font = new Font("Arial", Font.PLAIN, fontsize);
        metrics = frame.getFontMetrics(font);
        lineheight = metrics.getHeight();
        lineascent = metrics.getAscent();
        charwidth = metrics.charWidth('0');
        switch(type){
            case 0:
                chars_per_line = width / charwidth - 1;
                lines_per_page = height / lineheight - 10;
                break;
            case 1:
                chars_per_line = width / charwidth + 15;
                lines_per_page = height / lineheight - 21;
                break;
        }
        headerfont = new Font("Arial", Font.ITALIC, fontsize);
        headermetrics = frame.getFontMetrics(headerfont);
        headery = y0 - (int)(0.125 * pagedpi) - headermetrics.getHeight() + headermetrics.getAscent();
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT);
        df.setTimeZone(TimeZone.getDefault());
        time = df.format(new Date());
        this.jobname = jobname;
        this.fontsize = fontsize;
    }

    public void write(char[] buffer, int index, int len) {
        synchronized(this.lock) {
            for(int i = index; i < index + len; i++) {
                if (page == null) newpage();

                if (buffer[i] == '\n') {
                    if (!last_char_was_return) newline();
                    continue;
                }
                if (buffer[i] == '\r') {
                    newline();
                    last_char_was_return = true;
                    continue;
                }
                else
                { last_char_was_return = false; }


                if (Character.isWhitespace(buffer[i]) && !Character.isSpaceChar(buffer[i]) && (buffer[i] != '\t'))
                { continue; }
                if (charnum >= chars_per_line) {
                    newline();
                    if (page == null) newpage();
                }
                if (Character.isSpaceChar(buffer[i]))
                { charnum++; }
                else if (buffer[i] == '\t')
                { charnum += 8 - (charnum % 8); }
                else {
                    page.drawChars(buffer, i, 1, x0 + charnum * charwidth, y0 + (linenum*lineheight) + lineascent);
                    charnum++;
                }
            }
        }
    }
    public void flush() { /* do nothing */ }
    public void close() {
        synchronized(this.lock) {
            if (page != null) page.dispose();
            job.end();
        }
    }
    public void setFontStyle(int style) {
        synchronized (this.lock) {
            Font current = font;
            try { font = new Font("Arial", style, fontsize); }
            catch (Exception e) { font = current; }
            if(page != null)
            { page.setFont(font); }
        }
    }
    protected void newpage() { page = job.getGraphics(); }
    @SuppressWarnings("serial")
	public static class PrintCanceledException extends Exception {
        public PrintCanceledException(String msg) { super(msg); }
    }
    protected void newline() {
        charnum = 0;
        linenum++;
        if (linenum >= lines_per_page) {
            page.dispose();
            linenum = 0;
            page = null;
        }
    }

    // Testing
    public static void printFile(String s, HardcopyWriter out) {
       try {
           FileReader in = new FileReader(s);
           char []  buffer = new char [4096];
           int numchars;
           out.setFontStyle(Font.ITALIC);
           while((numchars = in.read(buffer)) != -1)
               out.write(buffer, 0, numchars);
           in.close();
           out.close();
           }
           catch (Exception e) {
               System.err.println(e);
               System.err.println("Usage: " + "java HardcopyWriter$PrintFile <filename>");
               System.exit(1);
           }
       System.exit(0);
   }
    private void drawImage(BufferedImage img) {
        page.drawImage(img, (int) (x0), y0, null) ;
        linenum += SIZE.getHeight() / (lineheight + lineascent);
    }
    @SuppressWarnings("serial")
	public static class Demo extends Frame implements ActionListener {
        public static String str = null;
        protected Button print, quit;

        /** The main method of the program.  Create a test window */
        public static void main(String[] args) {
            str = ".\\src\\Quadratrix.java";
            Frame f = new Demo();
            f.setVisible(true);

        }

        /** Constructor for the test program's window. */
        public Demo() {
            super("HardcopyWriter Test");          // Call frame constructor
            Panel p = new Panel();                 // Add a panel to the frame
            this.add(p, "Center");                 // Center it
            p.setFont(new Font("Arial",Font.BOLD, 18));
            print = new Button("Print Test Page"); // Create a Print button
            quit = new Button("Quit");             // Create a Quit button
            print.addActionListener(this);         // Specify that we'll handle
            quit.addActionListener(this);          //   button presses
            p.add(print);                          // Add the buttons to panel
            p.add(quit);
            this.pack();                           // Set the frame size
        }

        /** Handle the button presses */
        public void actionPerformed(ActionEvent e) {
            Object o = e.getSource();
            if (o == quit) System.exit(0);
            else if (o == print) try {
                printDemoPage();
            } catch (IOException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        @SuppressWarnings("static-access")
		public void printDemoPage() throws IOException {
            HardcopyWriter hw;
            try { hw = new HardcopyWriter(this, "Quadratrix print",10,.75,.75,.75,.75);}
            catch (HardcopyWriter.PrintCanceledException e) { return; }

            PrintWriter out = new PrintWriter(hw);
            hw.setFontStyle(Font.BOLD);
            hw.newpage();
            BufferedImage img = new BufferedImage((int)(SIZE.getWidth()), (int) SIZE.getHeight(),Image.SCALE_DEFAULT);
            Graphics2D ig = (Graphics2D) img.getGraphics();
            ig.fillRect(0,0,(int)SIZE.getWidth(), (int) SIZE.getHeight());
            ig.setColor(Color.RED);
            Shape temp = new Quadratrix(200, 0, 0, 1, 1);
            Stroke stroke = new QStroke(2, 10, 10);	
            ig.setStroke(stroke);            
            ig.translate((int)SIZE.getWidth()/2, (int) SIZE.getHeight()/2);
            ig.draw(temp);
            hw.drawImage(img);
            out.println("\n\n");            
            out.println("Quadratrix \n");            
            ig.translate(0, 0);
            printFile(str, hw);
        }
    }
}


