import java.util.ArrayList;

public class DataFrame {

    // store column in an arraylist
    private final ArrayList<Column> Columns = new ArrayList<>();

    // adds column to DataFrame with specified name and values
    public void addColumn(String name, ArrayList<String> values) {
        Column newColumn = new Column(name, values);
        Columns.add(newColumn);
    }

    // returns string with a list of names that are stored in DataFrame
    public String[] getColumnNames() {
        ArrayList<String> columnNames = new ArrayList<>();
        for (Column column:Columns) {
            columnNames.add(column.getName());
        }
        return columnNames.toArray(new String[0]);
    }

    // returns number of rows in DataFrame
    public int getRowCount() {
        int count = 0;
        for (Column column:Columns) {
            if (count < column.getSize()) {
                count = column.getSize();
            }
        }
        return count;
    }

    // gets a row value from a column
    public String getValue(String name, int row) {
        String value = "-1";
        for (Column column:Columns) {
            if (column.getName().equals(name)) {
                value = column.getRowValue(row);
                break;
            }
        }
        return value;
    }

    // puts value into a row in a column
    public void putValue(String name, int row, String value) {
        for (Column column:Columns) {
            if (column.getName().equals(name)) {
                column.setRowValue(row, value);
            }
        }

    }

    // adds a value to the end of the column
    public void addValue(String name, String value) {
        for (Column column:Columns) {
            if (column.getName().equals(name)) {
                column.addRowValue(value);
            }
        }
    }

    // gets values in a column
    public String[] getColumnValues(String name) {
        ArrayList<String> columnValues = new ArrayList<>();
        for (Column column:Columns) {
            if (column.getName().equals(name)) {
            columnValues = column.getColumnValues();
            break;
            }
        }
        return columnValues.toArray(new String[0]);
    }

    public Boolean isEmpty() {
        return Columns.size() == 0;
    }


}
