import java.util.ArrayList;

public class Column {

    private final String columnName;
    private final ArrayList<String> rowArray;

    // initialise column with only name
    public Column(String name) {
        columnName = name;
        rowArray = new ArrayList<>();
    }

    // initialise column with name and values
    public Column(String name, ArrayList<String> values) {
        columnName = name;
        rowArray = values;
    }

    // returns name of column
    public String getName() {
        return columnName;
    }

    // returns size of column
    public int getSize() {
    return rowArray.size();
    }

    // returns value of row at indexOfRow
    public String getRowValue(int indexOfRow) {
        return rowArray.get(indexOfRow);
    }

    // sets value to value of indexOfRow
    public void setRowValue(int indexOfRow, String value) {
        rowArray.set(indexOfRow, value);
    }

    // adds value to a new row
    public void addRowValue(String newValue) {
        rowArray.add(newValue);
    }

    // return values in column
    public ArrayList<String> getColumnValues() {
        return rowArray;
    }
}
