import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.PatternSyntaxException;

public class GUI extends JFrame implements ActionListener{

    private final Model currentData = new Model();
    private JPanel backPanel;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu visualMenu;
    private JMenu tableHeadColourSubMenu;
    private JMenu textSizeSubMenu;
    private JMenu textStyleSubMenu;
    private JMenuItem clearDataFrameItem;
    private JMenuItem loadDataFrameItem;
    private JMenuItem saveDataFrameItem;
    private JMenuItem yellowTableHeaderColourItem;
    private JMenuItem orangeTableHeaderColourItem;
    private JMenuItem greenTableHeaderColourItem;
    private JMenuItem cyanTableHeaderColourItem;
    private JMenuItem magentaTableHeaderColourItem;
    private JMenuItem redTableHeaderColourItem;
    private JMenuItem lightGrayTableHeaderColourItem;
    private JSlider textSizeSlider;
    private int textStyle;
    private ButtonGroup textStyleRadioButtonGroup;
    private JRadioButtonMenuItem plainTextItem;
    private JRadioButtonMenuItem italicTextItem;
    private JRadioButtonMenuItem boldTextItem;
    private JPanel dataSelectionPanel;
    private JButton showAllColumnsButton;
    private JButton hideAllColumnsButton;
    private JTabbedPane displayDataTabbedPane;
    private JScrollPane scrollableDataFrameTable;
    private JTable dataFrameTable;
    private JPanel searchBarPanel;
    private JComboBox<String> searchColumnComboBox;
    private DefaultComboBoxModel<String> searchColumnComboBoxModel;
    private Placeholder searchBarTextField;
    private JLabel searchBarMatchesLabel;
    private JTextField fileNameTextField;
    private String folderLocation;
    private String fileName;
    private String statusMessage;
    private JCheckBox overwriteFileCheckBox;
    private final ImageIcon messageIcon = new ImageIcon("img/message.png");

    public GUI(){
        createGUI();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setMinimumSize(new Dimension(1000, 400 ));
        setLocationRelativeTo(null);
        setVisible(true);
        JOptionPane.showMessageDialog(this, "Click File for more options!", null, JOptionPane.INFORMATION_MESSAGE, messageIcon);
    }

    // Creates the individual components and adds them together
    private void createGUI(){
        setTitle("DataFrame Dashboard Manager");
        createMenuBar();
        createDataSelectionPanel();
        createDisplayDataTabbedPane();
        createSearchBarPanel();
        setJMenuBar(menuBar);
        addBackPanel();

    }

    private void createMenuBar(){
        menuBar = new JMenuBar();

        // Creates the menu headers
        fileMenu = new JMenu("File");
        visualMenu = new JMenu("Preferences");
        tableHeadColourSubMenu = new JMenu("Change Table Head Colour");
        textSizeSubMenu = new JMenu("Text Size");
        textStyleSubMenu = new JMenu("Text Style");
        loadDataFrameItem = new JMenuItem("Load");
        saveDataFrameItem = new JMenuItem("Save");
        clearDataFrameItem = new JMenuItem("Clear");
        yellowTableHeaderColourItem = new JMenuItem("Yellow");
        orangeTableHeaderColourItem = new JMenuItem("Orange");
        greenTableHeaderColourItem = new JMenuItem("Green");
        cyanTableHeaderColourItem = new JMenuItem("Cyan");
        magentaTableHeaderColourItem = new JMenuItem("Magenta");
        redTableHeaderColourItem = new JMenuItem("Red");
        lightGrayTableHeaderColourItem = new JMenuItem("Light Gray");


        textSizeSlider = new JSlider(8, 16, 12);
        textSizeSlider.setPaintTicks(true);
        textSizeSlider.setMajorTickSpacing(4);
        textSizeSlider.setPaintLabels(true);
        textSizeSlider.setOrientation(SwingConstants.VERTICAL);
        textStyle = 0;
        textSizeSlider.addChangeListener(e -> {
            dataFrameTable.setFont(new Font("Dialog", textStyle, textSizeSlider.getValue()));
            currentData.changeFrequencyTableVisualSettings(1, Color.LIGHT_GRAY, textStyle, textSizeSlider.getValue());
        });


        textStyleRadioButtonGroup = new ButtonGroup();textStyleRadioButtonGroup.add(plainTextItem = new JRadioButtonMenuItem("Plain"));
        textStyleRadioButtonGroup.add(italicTextItem = new JRadioButtonMenuItem("Italic"));
        textStyleRadioButtonGroup.add(boldTextItem = new JRadioButtonMenuItem("Bold"));
        plainTextItem.setSelected(true);

        clearDataFrameItem.setIcon(new ImageIcon("img/clear.png"));
        loadDataFrameItem.setIcon(new ImageIcon("img/load.png"));
        saveDataFrameItem.setIcon(new ImageIcon("img/save.png"));
        yellowTableHeaderColourItem.setIcon(new ImageIcon("img/yellow.png"));
        orangeTableHeaderColourItem.setIcon(new ImageIcon("img/orange.png"));
        greenTableHeaderColourItem.setIcon(new ImageIcon("img/green.png"));
        cyanTableHeaderColourItem.setIcon(new ImageIcon("img/cyan.png"));
        magentaTableHeaderColourItem.setIcon(new ImageIcon("img/magenta.png"));
        redTableHeaderColourItem.setIcon(new ImageIcon("img/red.png"));
        lightGrayTableHeaderColourItem.setIcon(new ImageIcon("img/lightGray.png"));

        clearDataFrameItem.addActionListener(this);
        loadDataFrameItem.addActionListener(this);
        saveDataFrameItem.addActionListener(this);

        yellowTableHeaderColourItem.addActionListener(this);
        orangeTableHeaderColourItem.addActionListener(this);
        greenTableHeaderColourItem.addActionListener(this);
        cyanTableHeaderColourItem.addActionListener(this);
        magentaTableHeaderColourItem.addActionListener(this);
        redTableHeaderColourItem.addActionListener(this);
        lightGrayTableHeaderColourItem.addActionListener(this);

        plainTextItem.addActionListener(this);
        italicTextItem.addActionListener(this);
        boldTextItem.addActionListener(this);

        // Shortcuts added for easier usability
        fileMenu.setMnemonic(KeyEvent.VK_F); // Keyboard shortcut : [Alt + f] or [Ctrl + Option + f] for File
        visualMenu.setMnemonic(KeyEvent.VK_V); // Keyboard shortcut : [Alt + d] or [Ctrl + Option + d] for Visual Settings
        clearDataFrameItem.setMnemonic(KeyEvent.VK_C); // Keyboard shortcut : [c] for Clear
        loadDataFrameItem.setMnemonic(KeyEvent.VK_L); // Keyboard shortcut : [l] for Load
        saveDataFrameItem.setMnemonic(KeyEvent.VK_S); // Keyboard shortcut : [s] for Save

        fileMenu.add(loadDataFrameItem);
        fileMenu.add(saveDataFrameItem);
        fileMenu.add(clearDataFrameItem);

        tableHeadColourSubMenu.add(yellowTableHeaderColourItem);
        tableHeadColourSubMenu.add(orangeTableHeaderColourItem);
        tableHeadColourSubMenu.add(greenTableHeaderColourItem);
        tableHeadColourSubMenu.add(cyanTableHeaderColourItem);
        tableHeadColourSubMenu.add(magentaTableHeaderColourItem);
        tableHeadColourSubMenu.add(redTableHeaderColourItem);
        tableHeadColourSubMenu.add(lightGrayTableHeaderColourItem);

        textSizeSubMenu.add(textSizeSlider);

        textStyleSubMenu.add(plainTextItem);
        textStyleSubMenu.add(italicTextItem);
        textStyleSubMenu.add(boldTextItem);

        visualMenu.add(tableHeadColourSubMenu);
        visualMenu.add(textSizeSubMenu);
        visualMenu.add(textStyleSubMenu);

        menuBar.add(fileMenu);

    }

    private void createDataSelectionPanel(){
        dataSelectionPanel = new JPanel();
        dataSelectionPanel.setPreferredSize(new Dimension(200, 750));
        dataSelectionPanel.setBorder(BorderFactory.createEtchedBorder());
    }

    private void createDisplayDataTabbedPane(){
        displayDataTabbedPane = new JTabbedPane();
        displayDataTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        displayDataTabbedPane.setPreferredSize(new Dimension(1400, 750));

        dataFrameTable = new JTable(){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                int rendererWidth = component.getPreferredSize().width;
                TableColumn tableColumn = getColumnModel().getColumn(column);
                tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
                return component;
            }
        };

        dataFrameTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        dataFrameTable.getTableHeader().setResizingAllowed(false);
        dataFrameTable.setCellSelectionEnabled(true);
        dataFrameTable.getColumnModel().setColumnMargin(20);
        dataFrameTable.getTableHeader().setBackground(Color.LIGHT_GRAY);
        dataFrameTable.setGridColor(Color.white);

        scrollableDataFrameTable = new JScrollPane(dataFrameTable);
        displayDataTabbedPane.add("", scrollableDataFrameTable);
    }

    private void createSearchBarPanel(){
        searchBarPanel = new JPanel(new FlowLayout());
        searchBarPanel.setBorder(BorderFactory.createEtchedBorder());
        searchBarPanel.setPreferredSize(new Dimension(1600, 40));

        searchColumnComboBoxModel = new DefaultComboBoxModel<>(new String[]{"All"});
        searchColumnComboBox = new JComboBox<>(searchColumnComboBoxModel);
        searchColumnComboBox.setPreferredSize(new Dimension(250, 30));
        searchColumnComboBox.addActionListener(this);

        searchBarTextField = new Placeholder(" Search Bar");
        searchBarTextField.setPreferredSize(new Dimension(500, 30));

        searchBarMatchesLabel = new JLabel("0 matches found.");

        searchBarTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable(e);
            }

            private void filterTable(DocumentEvent e){
                try{
                    String text = e.getDocument().getText(0, e.getDocument().getLength());

                    if (!text.equals("")) {
                        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(((DefaultTableModel) dataFrameTable.getModel()));

                        if (Objects.equals(searchColumnComboBox.getSelectedItem(), "All")) {
                            sorter.setRowFilter(RowFilter.regexFilter(text));
                        }else{
                            sorter.setRowFilter(RowFilter.regexFilter(text, searchColumnComboBox.getSelectedIndex() -1));
                        }

                        dataFrameTable.setRowSorter(sorter);
                        searchBarMatchesLabel.setText(dataFrameTable.getRowCount() + " matches found.");
                    }else {
                        dataFrameTable.setAutoCreateRowSorter(true);
                        searchBarMatchesLabel.setText("0 matches found.");
                    }
                }
                catch (BadLocationException | PatternSyntaxException e1){
                    System.out.println(e1);
                }
            }
        });
    }

    private void addBackPanel(){
        backPanel = new JPanel(new BorderLayout());

        backPanel.add(dataSelectionPanel, BorderLayout.WEST);
        backPanel.add(displayDataTabbedPane, BorderLayout.CENTER);
        backPanel.add(searchBarPanel, BorderLayout.SOUTH);
        add(backPanel, BorderLayout.CENTER);
    }

    private void clearData() {
        currentData.emptyDataFrame();
        dataFrameTable.setModel(currentData.getTable());
        fileName = "";

        updateTabs(false);
        resetVisualSettings();
        updateMenuBar(false);
        updateDataSelectionPanel();
        updateSearchBarPanel(false);
    }
    
    private void loadFile() {
        JFileChooser fc = new JFileChooser("./DataSet/");
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            File file = fc.getSelectedFile();
            String fileName = file.getName();

            try {
                if ( fileName.endsWith(".csv") || fileName.endsWith(".json")){
                    currentData.loadDataFrame(file.getAbsolutePath());
                    dataFrameTable.setModel(currentData.getTable());
                    dataFrameTable.setAutoCreateRowSorter(true);

                    updateTabs(true);
                    displayDataTabbedPane.setTitleAt(0, fileName);

                    updateMenuBar(true);
                    updateDataSelectionPanel();
                    updateSearchBarPanel(true);
                }else {
                    JOptionPane.showMessageDialog(this,"The selected file is not supported.", null, JOptionPane.INFORMATION_MESSAGE, messageIcon);
                }
            }catch (StringIndexOutOfBoundsException e){
                JOptionPane.showMessageDialog(this, "The selected file does not exit. Please choose a valid file", null, JOptionPane.INFORMATION_MESSAGE, messageIcon);
            }
        }
    }

    // Retrieves the file name the user wishes to save the data as
    private void getFileName(){
        fileNameTextField = new JTextField();
        Object[] msgContent = {"Please enter the name of the file you wish to save the file as: ", fileNameTextField};
        JOptionPane.showConfirmDialog(this,  msgContent,  "Save File", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, messageIcon);

        fileName = fileNameTextField.getText();
    }

    // Main body to save the data as a .json file
    private void saveFile(){
        // Retrieves the name the user wishes to save file as
        getFileName();

        // Lets the user choose which folder to save the file in
        JFileChooser fc = new JFileChooser(".");
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            folderLocation = fc.getSelectedFile().getAbsolutePath();

            // Saves the status when saving the JSON File
            statusMessage = currentData.saveFrameToJSON(folderLocation, fileName);

            // If the filename already exists, give the user the option to replace it
            if (statusMessage.equals("The file " + fileName + ".json already exists")){
                overwriteFileCheckBox = new JCheckBox("Overwrite " + fileName + ".json");
                Object[] msgContent = {statusMessage, overwriteFileCheckBox};
                JOptionPane.showConfirmDialog(this,  msgContent,  null, JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, messageIcon);

                if (overwriteFileCheckBox.isSelected()){
                    File myObj = new File(folderLocation + "/" + fileName + ".json");
                    if (myObj.delete()) {
                        JOptionPane.showMessageDialog(this, currentData.saveFrameToJSON(folderLocation, fileName), null, JOptionPane.INFORMATION_MESSAGE, messageIcon);
                    }else {
                        JOptionPane.showMessageDialog(this, "Unable to delete the file", null, JOptionPane.INFORMATION_MESSAGE, messageIcon);
                    }
                }

            }else {
                // Display the status message
                JOptionPane.showMessageDialog(this, statusMessage, null, JOptionPane.INFORMATION_MESSAGE, messageIcon);
            }
        }
    }

    // Updates the dataSelectionPanel with the Column Names of the DataFrame loaded in
    private void updateDataSelectionPanel(){
        // Removes any previous data and creates a new layout
        dataSelectionPanel.removeAll();
        dataSelectionPanel.setLayout(new GridLayout(currentData.getColumnNames().length +2, 1, 10, 0));

        if (!currentData.isEmpty()){
            showAllColumnsButton = new JButton("Show All Columns");
            hideAllColumnsButton = new JButton("Hide All Columns");

            showAllColumnsButton.addActionListener(e -> checkAllBoxes(true));
            hideAllColumnsButton.addActionListener(e -> checkAllBoxes(false));

            dataSelectionPanel.add(showAllColumnsButton);
            dataSelectionPanel.add(hideAllColumnsButton);

            // Creates a checkbox for each Column Name in the DataFrame
            for (String columnName : currentData.getColumnNames()) {
                JCheckBox checkBox = new JCheckBox();
                checkBox.setText(columnName);
                checkBox.setSelected(true);

                // Depending on the state of the checkbox, it should show/hide the corresponding column
                checkBox.addItemListener(event -> {
                    JCheckBox cb = (JCheckBox) event.getSource();
                    currentData.changeState(checkBox.getText(), cb.isSelected());
                    updateSearchBarPanel(true);
                    dataFrameTable.setModel(currentData.getTable());
                });

                dataSelectionPanel.add(checkBox);
            }
        }

        // Refreshes the dataSelectionPanel
        dataSelectionPanel.revalidate();
        dataSelectionPanel.repaint();
    }

    // Updates the searchBarPanel
    private void updateSearchBarPanel(Boolean dataLoadedIn){
        searchBarPanel.removeAll();

        searchColumnComboBoxModel.removeAllElements();
        searchColumnComboBoxModel.addElement("All");
        searchBarTextField.setText("");

        // if data is loaded in the Dashboard Manager, add the column names that are displayed in the table
        if (dataLoadedIn) {
            for (String column : currentData.getColumnNames()) {
                searchColumnComboBoxModel.addElement(column);
            }

            // Adds the components onto the searchBarPanel
            searchBarPanel.add(searchColumnComboBox, BorderLayout.CENTER);
            searchBarPanel.add(searchBarTextField, BorderLayout.CENTER);
            searchBarPanel.add(searchBarMatchesLabel, BorderLayout.EAST);

        }
        searchBarPanel.repaint();
    }

    // Updates the menu bar everytime the data is changed
    private void updateMenuBar(Boolean dataLoadedIn){
        menuBar.removeAll();
        menuBar.add(fileMenu);
        if (dataLoadedIn){
            menuBar.add(visualMenu);
        }
    }

    // Resets all the visual settings back to default
    private void resetVisualSettings(){
        textSizeSlider.setValue(12);
        dataFrameTable.getTableHeader().setBackground(Color.LIGHT_GRAY);
        plainTextItem.setSelected(true);
        textStyle = 0;
        dataFrameTable.setFont(new Font("Dialog", textStyle, textSizeSlider.getValue()));
    }

    // Updates JTabbedPane depending if data is loaded in and adds the relevant tabs
    private void updateTabs(Boolean dataLoadedIn){
        for (int i = displayDataTabbedPane.getTabCount() - 1; 0 < i; i--) {
            displayDataTabbedPane.removeTabAt(i);
        }
        displayDataTabbedPane.setTitleAt(0, "");
        if(dataLoadedIn) {
            for (String columnName : currentData.getColumnNames()){
                JPanel frequencyDataChartPanel = currentData.getFrequencyDataChartsPanel(columnName);
                displayDataTabbedPane.addTab(columnName, frequencyDataChartPanel);
            }
        }
    }

    // Selects or Deselects all the checkboxes within dataSelectionPanel
    private void checkAllBoxes(Boolean state){
        Component[] components = dataSelectionPanel.getComponents();

        for (Component component : components) {
            if (component instanceof JCheckBox) {
                JCheckBox checkbox = (JCheckBox) component;
                checkbox.setSelected(state);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Actions for when one of the File Menu Options have been selected
        if (e.getSource() == clearDataFrameItem){
            clearData();
        }
        else if (e.getSource() == loadDataFrameItem){
            loadFile();
        }
        else if (e.getSource() == saveDataFrameItem){
            saveFile();
        }

        // Action set for when the selected data in searchColumnComboBox has changed
        if (e.getSource() == searchColumnComboBox){
            searchBarTextField.setText("");
        }

        // If the user wishes to change the colour of the table headers
        if (e.getSource() == yellowTableHeaderColourItem){
            dataFrameTable.getTableHeader().setBackground(Color.YELLOW);
            currentData.changeFrequencyTableVisualSettings(0, Color.YELLOW, 0, 0);
        }
        else if (e.getSource() == orangeTableHeaderColourItem){
            dataFrameTable.getTableHeader().setBackground(Color.ORANGE);
            currentData.changeFrequencyTableVisualSettings(0, Color.ORANGE, 0, 0);
        }
        else if (e.getSource() == greenTableHeaderColourItem) {
            dataFrameTable.getTableHeader().setBackground(Color.GREEN);
            currentData.changeFrequencyTableVisualSettings(0, Color.GREEN, 0, 0);
        }
        else if (e.getSource() == cyanTableHeaderColourItem) {
            dataFrameTable.getTableHeader().setBackground(Color.CYAN);
            currentData.changeFrequencyTableVisualSettings(0, Color.CYAN, 0, 0);
        }
        else if (e.getSource() == magentaTableHeaderColourItem) {
            dataFrameTable.getTableHeader().setBackground(Color.MAGENTA);
            currentData.changeFrequencyTableVisualSettings(0, Color.MAGENTA, 0, 0);
        }
        else if (e.getSource() == redTableHeaderColourItem) {
            dataFrameTable.getTableHeader().setBackground(Color.RED);
            currentData.changeFrequencyTableVisualSettings(0, Color.RED, 0, 0);
        }
        else if (e.getSource() == lightGrayTableHeaderColourItem) {
            dataFrameTable.getTableHeader().setBackground(Color.LIGHT_GRAY);
            currentData.changeFrequencyTableVisualSettings(0, Color.LIGHT_GRAY, 0, 0);
        }

        // If the user wishes to change the style of writing
        if (e.getSource() == plainTextItem){
            textStyle = 0;
            dataFrameTable.setFont(new Font("Dialog", textStyle, textSizeSlider.getValue()));
            currentData.changeFrequencyTableVisualSettings(1, Color.LIGHT_GRAY, textStyle, textSizeSlider.getValue());
        }
        else if (e.getSource() == italicTextItem){
            textStyle = 2;
            dataFrameTable.setFont(new Font("Dialog", textStyle, textSizeSlider.getValue()));
            currentData.changeFrequencyTableVisualSettings(1, Color.LIGHT_GRAY, textStyle, textSizeSlider.getValue());
        }
        else if (e.getSource() == boldTextItem){
            textStyle = 1;
            dataFrameTable.setFont(new Font("Dialog", textStyle, textSizeSlider.getValue()));
            currentData.changeFrequencyTableVisualSettings(1, Color.LIGHT_GRAY, textStyle, textSizeSlider.getValue());
        }

    }

}