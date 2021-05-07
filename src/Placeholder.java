import javax.swing.*;
import java.awt.*;

public class Placeholder extends JTextField {
    private final String placeholder;
    public Placeholder(String placeholderText){
        placeholder = placeholderText;
    }

    @Override
    protected void paintComponent(final Graphics paintGraphics) {
        super.paintComponent(paintGraphics);
        int textLength = getText().length();
        int placeholderLength = placeholder.length();
        if (placeholder == null || placeholderLength == 0 || textLength > 0) {
            return;
        }
        final Graphics2D graphics = (Graphics2D) paintGraphics;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(getDisabledTextColor());
        graphics.drawString(placeholder, getInsets().left, paintGraphics.getFontMetrics().getMaxAscent() + 2*getInsets().top);
    }

}