import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Map;

public class BarChart extends JPanel {

    public static final int TOP_BUFFER = 30;
    public static final int AXIS_OFFSET = 20;
    private final Map<String, Integer> data;
    private int width;
    private int height;
    private int yAxis;
    private final String xLabel;

    public BarChart(Map<String, Integer> frequencyTable, String name){
        super();
        data = frequencyTable;
        xLabel = name;
        setBackground(Color.pink);
        int tableSize = frequencyTable.size();
        if (((width/tableSize) < 5)) {
            setPreferredSize(new Dimension((5 * tableSize) + 50, getHeight()));
        }
    }

    private void getChartSize() {
        width = this.getWidth() - 2*AXIS_OFFSET;
        height = this.getHeight() - 2*AXIS_OFFSET - TOP_BUFFER;
        yAxis = this.getHeight() - AXIS_OFFSET;
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;
        getChartSize();
        drawBars(graphics2D);
        drawLabels(graphics2D);
        drawChartInfo(graphics2D);
    }

    public void drawBars(Graphics2D graphics2D) {
        int count = data.size();
        int barWidth = width/count;
        int value;
        int barHeight;
        int xLeftCord;
        int yTopLeftCord;
        int counter = 0;
        double maxHeight = 0;

        for (Integer frequency:data.values()) {
            if (maxHeight < frequency)
                maxHeight = frequency;
        }
        if ((width/count) < 5) {
            barWidth = 5;
        }
        graphics2D.setColor(Color.black);

        for (String bar:data.keySet()) {
            value = data.get(bar);
            double barHeightDecimal = (value/maxHeight) * height;
            barHeight = (int) barHeightDecimal;
            xLeftCord = AXIS_OFFSET + counter * barWidth;
            yTopLeftCord = yAxis - barHeight;
            Rectangle rec = new Rectangle(xLeftCord, yTopLeftCord, barWidth, barHeight);
            graphics2D.draw(rec);
            counter++;
        }
    }

    // Draws x-axis and y-axis labels
    private void drawLabels(Graphics2D graphics2D) {
        graphics2D.drawString(xLabel, AXIS_OFFSET + 100, yAxis + AXIS_OFFSET/2 +3) ;
        Font originalFont = graphics2D.getFont();
        Font font = new Font(null, originalFont.getStyle(), originalFont.getSize());
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate(Math.toRadians(-90), 0, 0);
        Font rotatedFont = font.deriveFont(affineTransform);
        graphics2D.setFont(rotatedFont);
        graphics2D.drawString("Frequency",AXIS_OFFSET/2+3, yAxis- height/4);
        graphics2D.setFont(originalFont);
    }

    private void drawChartInfo(Graphics2D graphics2D) {
        int size = data.keySet().size();
        graphics2D.drawString("Number of Unique Values: " + size, AXIS_OFFSET +10, 15);
    }

}