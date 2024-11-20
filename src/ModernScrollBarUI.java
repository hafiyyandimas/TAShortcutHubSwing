import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ModernScrollBarUI extends BasicScrollBarUI {

    @Override
    protected void configureScrollBarColors() {
        // Set background color of the scrollbar
        this.thumbColor = new Color(100, 100, 100);  // Thumb color
        this.trackColor = new Color(40, 40, 40);  // Track color
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return new JButton() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(0, 0);  // Make the button invisible
            }
        };
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return new JButton() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(0, 0);  // Make the button invisible
            }
        };
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(thumbColor);

        // Draw a rounded rectangle for the thumb
        g2d.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 10, 10);
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(trackColor);
        g2d.fillRoundRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height, 10, 10);
    }
}
