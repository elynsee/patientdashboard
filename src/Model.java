import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Model {

    private DataFrame dataFrame = new DataFrame();
    private ArrayList<Boolean> headers;
    private ArrayList<JTable> frequencyTables;

    public void emptyDataFrame() {
        dataFrame = new DataFrame();
        headers = new ArrayList<>();
        frequencyTables = new ArrayList<>();
    }

    public void loadDataFrame(String filePath) {
        dataFrame = new DataLoader().loadData(filePath);
        headers = new ArrayList<>();
        for (String s:dataFrame.getColumnNames()) {
            headers.add(true);
        }
        frequencyTables = new ArrayList<>();
    }

    public String[] getColumnNames() {
        return dataFrame.getColumnNames();
    }

    public String[] getShownNames() {
        ArrayList<String> shownNames = new ArrayList<>();
        int size = headers.size();
        for (int index = 0; index < size; index++) {
            if (headers.get(index)) {
                shownNames.add(dataFrame.getColumnNames()[index]);
            }
        }
        return shownNames.toArray(new String[0]);
    }

    public void changeState(String name, Boolean state) {
        int length = dataFrame.getColumnNames().length;
        for (int index = 0; index < length; index++) {
            if (dataFrame.getColumnNames()[index].equals(name)) {
                headers.set(index, state);
            }
        }
    }

    public DefaultTableModel getTable() {
        DefaultTableModel model = new DefaultTableModel(0, 0);
        int size = headers.size();
        for (int i = 0; i <size; i++) {
            if (headers.get(i)) {
                model.addColumn(dataFrame.getColumnNames()[i], dataFrame.getColumnValues(dataFrame.getColumnNames()[i]));
            }
        }
        return model;
    }

    public String saveFrameToJSON(String fileLocation, String name) {
        if (dataFrame.isEmpty()) {
            String output = "DataFrame is empty!";
            return output;
        } else {
            return new JSONWriter().saveAsJSONFile(dataFrame, fileLocation, name);
        }
    }

    public Boolean isEmpty() {
        return dataFrame.isEmpty();
    }

    private static Map<String, Integer> sortByValue(Map<String, Integer> unsortedMap)
    {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(unsortedMap.entrySet());
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()) == 0
                ? o2.getKey().compareTo(o1.getKey())
                : o2.getValue().compareTo(o1.getValue()));
        return list.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));
    }

    private Map<String, Integer> getFrequencyTableData(String name){
        Map<String, Integer> frequencyTableData = new HashMap<>();
        for (String value : dataFrame.getColumnValues(name)) {
            if (frequencyTableData.containsKey(value)) {
                frequencyTableData.put(value, frequencyTableData.get(value) + 1);
            } else {
                frequencyTableData.put(value, 1);
            }
        }
        frequencyTableData = sortByValue(frequencyTableData);
        return frequencyTableData;
    }

    public JPanel getFrequencyDataChartsPanel(String name){
        JPanel frequencyDataChart = new JPanel(new GridLayout(2, 1));
        Map<String, Integer> frequencyData = getFrequencyTableData(name);
        JTable frequencyTable = new JTable(){
            @Override
            // Makes it so the every cell is uneditable
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        DefaultTableModel frequencyTableModel = new DefaultTableModel(0, 0);
        frequencyTableModel.setColumnIdentifiers(new String[]{""," Value", "Frequency"});
        AtomicInteger counter = new AtomicInteger(1);
        frequencyData.forEach((k,v) -> {
            if (k.isBlank()){
                frequencyTableModel.addRow(new String[]{String.valueOf(counter.get()), "Null", String.valueOf(v)});
            }
            else{
                frequencyTableModel.addRow(new String[]{String.valueOf(counter.get()), k, String.valueOf(v)});
            }
            counter.getAndIncrement();
        });

        frequencyTable.setModel(frequencyTableModel);
        frequencyTables.add(frequencyTable);

        frequencyTable.getColumnModel().setColumnMargin(20);
        frequencyTable.getTableHeader().setResizingAllowed(false);
        frequencyTable.getTableHeader().setBackground(Color.MAGENTA);
        BarChart barChart = new BarChart(frequencyData, name + "'s");
        frequencyDataChart.add(new JScrollPane(barChart));
        frequencyDataChart.add(new JScrollPane(frequencyTable));
        return frequencyDataChart;
    }

    public void changeFrequencyTableVisualSettings(int setting, Color color, int style, int size){
        for (JTable frequencyTable:frequencyTables){
            if (setting == 0) {
                frequencyTable.getTableHeader().setBackground(color);
            } else if (setting == 1) {
                frequencyTable.setFont(new Font("Dialog", style, size));
            }
        }
    }
}
