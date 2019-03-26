package pack;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.util.Enumeration;
import java.util.List;

public class CatalogFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    static GameNode addResult = null;

    private JTable infoPanel = new JTable();
    private JTree tree = new JTree();
    private CatalogTableModel tableModel = null;
    private CatalogTreeModel treeModel = null;

    private CatalogFrame() {
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> openAddDialog());

        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(e -> removeItem());

        tableModel = new CatalogTableModel();
        infoPanel = new JTable(tableModel);
        treeModel = new CatalogTreeModel(new GameTreeNode("Games shop"));
        tree = new JTree(treeModel);
        tree.addTreeSelectionListener(e -> {
            GameTreeNode node = (GameTreeNode) tree.getLastSelectedPathComponent();
            if (node == null) {
                return;
            }

            List<GameNode> phoneNodes = node.getAllLeaves();
            tableModel = new CatalogTableModel(phoneNodes);
            infoPanel.setModel(tableModel);
        });

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                true,
                new JScrollPane(tree),
                new JScrollPane(infoPanel));
        splitPane.setDividerLocation(250);

        getContentPane().add(splitPane);
        getContentPane().add("North", addButton);
        getContentPane().add("South", removeButton);
        setBounds(100, 100, 640, 480);
        setTitle("Games catalog");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void openAddDialog() {
        AddDialog addForm = new AddDialog(this);
        addForm.setVisible(true);
    }

    void addNewItem() {
        GameTreeNode temp, where, insert, root = treeModel.getRoot();

        if (addResult != null) {
            try {
                insert = new GameTreeNode(addResult.getSystem(), addResult);
                if ((where = findNode(root, addResult.getSystem())) != null) {
                    treeModel.insertNodeInto(insert, where, where.getChildCount(), false);
                } else if (findNode(root, addResult.getYear()) != null) {
                    treeModel.insertNodeInto(new GameTreeNode(addResult.getSystem()),
                            temp = findNode(root, addResult.getYear()),
                            temp.getChildCount(),
                            false);
                    where = findNode(root, addResult.getSystem());
                    treeModel.insertNodeInto(insert, where, where.getChildCount(), false);
                } else if (findNode(root, addResult.getName()) != null) {
                    treeModel.insertNodeInto(new GameTreeNode(addResult.getYear()),
                            temp = findNode(root, addResult.getName()),
                            temp.getChildCount(),
                            false);
                    treeModel.insertNodeInto(new GameTreeNode(addResult.getSystem()),
                            temp = findNode(root, addResult.getYear()),
                            temp.getChildCount(),
                            false);
                    where = findNode(root, addResult.getSystem());
                    treeModel.insertNodeInto(insert, where, where.getChildCount(), false);
                } else {
                    treeModel.insertNodeInto(new GameTreeNode(addResult.getName()),
                            root,
                            root.getChildCount(),
                            false);
                    treeModel.insertNodeInto(new GameTreeNode(addResult.getYear()),
                            temp = findNode(root, addResult.getName()),
                            temp.getChildCount(),
                            false);
                    treeModel.insertNodeInto(new GameTreeNode(addResult.getSystem()),
                            temp = findNode(root, addResult.getYear()),
                            temp.getChildCount(),
                            false);
                    where = findNode(root, addResult.getSystem());
                    treeModel.insertNodeInto(insert, where, where.getChildCount(), false);
                }
            } catch (Exception e) {
                addResult = null;
                return;
            }
        }
        addResult = null;
    }

    private void removeItem() {
        TreePath currentSelection = tree.getSelectionPath();
        if (currentSelection != null) {
            GameTreeNode currentNode = (GameTreeNode) (currentSelection.getLastPathComponent());
            GameTreeNode parent = (GameTreeNode) (currentNode.getParent());
            if (parent != null) {
                treeModel.removeNodeFromParent(currentNode);
                parent.deleteNode(currentNode);
                List<GameNode> phoneNodes = parent.getAllLeaves();
                tableModel = new CatalogTableModel(phoneNodes);
                infoPanel.setModel(tableModel);
            }
        }
    }

    private GameTreeNode findNode(GameTreeNode root, String s) {
        Enumeration e = root.depthFirstEnumeration();
        while (e.hasMoreElements()) {
            GameTreeNode node = (GameTreeNode) e.nextElement();
            if (node.toString().equalsIgnoreCase(s)) {
                return node;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        CatalogFrame mainClass = new CatalogFrame();
        mainClass.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainClass.setVisible(true);
    }
}
