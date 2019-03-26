package pack;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class AddDialog extends JFrame {
    private static final long serialVersionUID = 1L;

    private CatalogFrame frame;
    private JLabel nameLabel, yearLabel, systemLabel, priceLabel;
    private JTextField nameTextField, yearTextField, systemTextField, priceTextField;
    private JButton cancelButton, okButton;

    AddDialog(CatalogFrame temp) {
        frame = temp;
        initComponents();
        setTitle("Add game");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    private void initComponents() {
        nameLabel = new JLabel();
        yearLabel = new JLabel();
        systemLabel = new JLabel();
        priceLabel = new JLabel();
        nameTextField = new JTextField();
        yearTextField = new JTextField();
        systemTextField = new JTextField();
        priceTextField = new JTextField();
        okButton = new JButton();
        cancelButton = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        nameLabel.setText("Name:");
        yearLabel.setText("Year:");
        systemLabel.setText("System:");
        priceLabel.setText("Price:");

        nameTextField.addCaretListener(this::brandTextFieldCaretUpdate);
        yearTextField.addCaretListener(this::modelTextFieldCaretUpdate);
        systemTextField.addCaretListener(this::systemTextFieldCaretUpdate);
        okButton.setText("OK");
        okButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                okButtonMouseClicked(evt);
            }
        });
        cancelButton.setText("Cancel");
        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                cancelButtonMouseClicked(evt);
            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(nameLabel)
                                                        .addComponent(yearLabel)
                                                        .addComponent(systemLabel)
                                                        .addComponent(priceLabel))
                                                .addGap(28, 28, 28)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(nameTextField, GroupLayout.PREFERRED_SIZE, 392, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(yearTextField, GroupLayout.PREFERRED_SIZE, 392, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(systemTextField, GroupLayout.PREFERRED_SIZE, 392, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(priceTextField, GroupLayout.PREFERRED_SIZE, 392, GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(okButton)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cancelButton))
                                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                .addGroup(layout.createSequentialGroup()
                                                                        .addGap(18, 18, 18))))
                                                .addGap(0, 4, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGap(8, 8, 8)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(nameLabel)
                                        .addComponent(nameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(yearLabel)
                                        .addComponent(yearTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(systemLabel)
                                        .addComponent(systemTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(priceLabel)
                                        .addComponent(priceTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(okButton)
                                        .addComponent(cancelButton))
                                .addContainerGap())
        );

        pack();
    }

    private void brandTextFieldCaretUpdate(CaretEvent evt) {
        checkOkClose();
    }

    private void modelTextFieldCaretUpdate(CaretEvent evt) {
        checkOkClose();
    }

    private void systemTextFieldCaretUpdate(CaretEvent evt) {
        checkOkClose();
    }

    private void cancelButtonMouseClicked(MouseEvent evt) {
        dispose();
    }

    private void okButtonMouseClicked(MouseEvent evt) {
        if (okButton.isEnabled()) {
            CatalogFrame.addResult = new GameNode(nameTextField.getText(),
                    yearTextField.getText(),
                    systemTextField.getText(),
                    priceTextField.getText()
                            .isEmpty()
                            ? "-1" : this.priceTextField.getText());
            frame.addNewItem();
            dispose();
        }
    }


    private void checkOkClose() {
        if (!nameTextField.getText().isEmpty() &&
                !yearTextField.getText().isEmpty() &&
                !systemTextField.getText().isEmpty()) {
            okButton.setEnabled(true);
        }
    }

}