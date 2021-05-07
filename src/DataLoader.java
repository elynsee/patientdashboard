import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class DataLoader {

    private final DataFrame loadData = new DataFrame();

    private ArrayList<ArrayList<String>> openCSV(String pathToCSV) {
        ArrayList<ArrayList<String>> formattedData = new ArrayList<>();
        ArrayList<String[]> CSVdata = new ArrayList<>();

        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(pathToCSV));
            String row;
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                CSVdata.add(data);
            }
            csvReader.close();
        } catch(FileNotFoundException e) {
            System.out.println("File cannot be found!");
        } catch(IOException e) {
            e.printStackTrace();
        }

        int length = CSVdata.get(0).length;
        for (int counter = 0; counter != length; counter++) {
            ArrayList<String> columnData = new ArrayList<>();
            for (String[] record:CSVdata) {
                try {
                    columnData.add(record[counter]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    columnData.add("");
                }
            }
            formattedData.add(columnData);
        }
        return formattedData;
    }

    private ArrayList<ArrayList<String>> openJSON(String pathToJSON) {
        ArrayList<ArrayList<String>> JSONdata = new ArrayList<>();
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(pathToJSON));
            String row;
            while ((row = csvReader.readLine()) != null) {
                if (!(row.equals("{") || row.equals("}"))) {
                    row = row.replaceAll("\t", "").replaceAll("\\[", "").replaceAll("\",", "").replaceAll(" :", ",").replaceAll("]", "").replaceAll("\"", "");
                    String[] data = row.split(",");

                    int length = data.length;
                    for (int index = 0; index < length; index++) {
                        if (!data[index].isBlank()) {
                            data[index] = data[index].trim();
                        } else {
                            data[index] = "";
                        }
                    }
                    JSONdata.add(new ArrayList<>(Arrays.asList(data)));
                }
            }
            csvReader.close();
        } catch(FileNotFoundException e) {
            System.out.println("File cannot be found!");
        } catch(IOException e) {
            e.printStackTrace();
        }
        return JSONdata;
    }

    private void createDataFrame(ArrayList<ArrayList<String>> file) {
        for (ArrayList<String> data: file) {
            String name = data.get(0);
            data.remove(0);
            loadData.addColumn(name, data);
        }
    }

    public DataFrame loadData(String filePath) {
        ArrayList<ArrayList<String>> data;
        if (filePath.endsWith(".csv")) {
            data = openCSV(filePath);
        } else {
            data = openJSON(filePath);
        }
        createDataFrame(data);
        return loadData;
    }




}
