package viewer;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.io.File;

public class SQLiteViewer extends JFrame {
    final SQLiteViewer mi = this;
    TableModel tableModel = new TableModel();
    JButton OpenFileButton;
    JTextField FileNameTextField;
    JComboBox<String> TablesComboBox;
    JButton ExecuteQueryButton;
    JTextArea QueryTextArea;
    JTable Table;
    DataBaseAdapter db;

    public SQLiteViewer() {
        super("SQLite Viewer");
        db = new DataBaseAdapter();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(700, 500);
        getContentPane().setLayout(new BorderLayout(3, 3));
        addAllControls();
        setIsDBConnected(false);
    }

    protected void addAllControls() {
        getContentPane().add(new JPanel() {{
            setLayout(null);
            setPreferredSize(new Dimension(-1, 165));
            add(FileNameTextField = new JTextField() {{
                setName("FileNameTextField");
                setBounds(3, 3, 400, 30);
            }});
            add(OpenFileButton = new JButton("Open") {{
                setName("OpenFileButton");
                setBounds(406, 3, 100, 30);
                addActionListener(event -> {
                    if (new File(FileNameTextField.getText()).exists()) {
                        if (db.OpenDataSource("jdbc:sqlite:" + FileNameTextField.getText())) {
                            TablesComboBox.removeAllItems();
                            db.getDBTables().forEach(TablesComboBox::addItem);
                            mi.setIsDBConnected(true);
                        }
                    } else {
                        JOptionPane.showMessageDialog(
                                new Frame(),
                                "File doesn't exist!",
                                "Ooops..",
                                JOptionPane.ERROR_MESSAGE);
                        mi.setIsDBConnected(false);
                    }
                });
            }});
            add(TablesComboBox = new JComboBox<>() {{
                setName("TablesComboBox");
                setBounds(3, 36, 503, 30);
                addActionListener(event ->
                    QueryTextArea.setText(
                            TablesComboBox.getSelectedItem() == null ?
                                    "" :
                                    "SELECT * FROM " + TablesComboBox.getSelectedItem().toString() + ";"
                    )
                );
            }});
            add(QueryTextArea = new JTextArea() {{
                setName("QueryTextArea");
                setBounds(3, 69, 400, 100);
            }});
            add(ExecuteQueryButton = new JButton("Execute") {{
                setName("ExecuteQueryButton");
                setBounds(406, 69, 100, 30);
                addActionListener(event -> new SwingWorker<TableData, Integer>() {
                    @Override
                    protected TableData doInBackground() {
                        TableData result = null;
                        try {
                            result = db.ExecuteSQL(QueryTextArea.getText());
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(
                                    new Frame(),
                                    e.getMessage(),
                                    "Ooops..",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        return result;
                    }

                    @Override
                    protected void done() {
                        try {
                            if(get()!=null) {
                                tableModel.setTd(get());
                                tableModel.fireTableStructureChanged();
                                tableModel.fireTableDataChanged();
                            }
                        } catch (InterruptedException | ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }.execute());
            }});
        }}, BorderLayout.NORTH);

        getContentPane().add(
                new JScrollPane(Table = new JTable(tableModel) {{
                    setName("Table");
                }}),
                BorderLayout.CENTER
        );
    }

    void setIsDBConnected(boolean isDBConnected) {
        //TablesComboBox.setEnabled(isDBConnected);
        ExecuteQueryButton.setEnabled(isDBConnected);
        QueryTextArea.setEnabled(isDBConnected);
        //Table.setEnabled(isDBConnected);
    }
}
