import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONWriter {

    private String output;

    public boolean checkValid(String name) {
        if (!name.equals("")) {
            Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
            Matcher matcher = pattern.matcher(name);
            return !matcher.find();
        } else {
            return false;
        }
    }

    public void createJSONFile(String location, String name) {
        try {
            File file = new File(location + "/" + name + ".json");
            if (file.createNewFile()) {
                output = "DataFrame is saved to " + name + ".json!";
            } else {
                output = name + ".json already exists! Please try again.";
            }
            } catch (IOException e) {
            output = "There has been an error saving the DataFrame to " + name + ".json";
            e.printStackTrace();
        }
    }

    public void addDataToFile(DataFrame dataFrame, String location, String name) {
        try {
            FileWriter fileWriter = new FileWriter(location + "/" + name + ".json");
            fileWriter.write("{");
            for (String columnName:dataFrame.getColumnNames()) {
                fileWriter.write("\n\t\"" + columnName + "\" : \"");
                fileWriter.write(Arrays.toString(dataFrame.getColumnValues(columnName)) + "\"");
                int length = dataFrame.getColumnNames().length;
                if (!columnName.equals(dataFrame.getColumnNames()[length-1])) {
                    fileWriter.write(",");
                }
            }
            fileWriter.write("\n}");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String saveAsJSONFile(DataFrame dataFrame, String location, String name) {
        if (checkValid(name)) {
            createJSONFile(location, name);
            if (new File(location + "/" + name +".json").isFile()) {
                addDataToFile(dataFrame, location, name);
            } else {
                output = "File cannot be created!";
            }
            } else {
            output = "File can only contain numbers and letters!";
        }
        return output;
    }
}
