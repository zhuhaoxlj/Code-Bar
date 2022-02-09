package view;

import viewUtils.Style;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicScrollBarUI;

/* loaded from: KaChat_Client.jar:cn/com/view/viewutil/ScrollBarUI.class */
public class ScrollBarUI extends BasicScrollBarUI {
    protected void configureScrollBarColors() {
        this.thumbColor = Color.PINK;
        this.thumbHighlightColor = Color.PINK;
        this.thumbDarkShadowColor = Color.PINK;
        this.thumbLightShadowColor = Color.PINK;
        this.trackColor = Color.PINK;
        setThumbBounds(0, 0, 3, 50);
        this.trackHighlightColor = Color.GREEN;
    }

    public Dimension getPreferredSize(JComponent c) {
        c.setPreferredSize(new Dimension(0, 0));
        return ScrollBarUI.super.getPreferredSize(c);
    }

    public void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        Graphics2D g2 = (Graphics2D) g;
        Paint paint = null;
        if (this.scrollbar.getOrientation() == 1) {
            paint = new GradientPaint(0.0f, 0.0f, Style.bgColor, trackBounds.width, 0.0f, Style.bgColor);
        }
        if (this.scrollbar.getOrientation() == 0) {
            paint = new GradientPaint(0.0f, 0.0f, Style.bgColor, trackBounds.height, 0.0f, Style.bgColor);
        }
        g2.setPaint(paint);
        g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        if (this.trackHighlight == 1) {
            paintDecreaseHighlight(g);
        }
        if (this.trackHighlight == 2) {
            paintIncreaseHighlight(g);
        }
    }

    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        g.translate(thumbBounds.x, thumbBounds.y);
        g.setColor(Color.lightGray);
        Graphics2D g2 = (Graphics2D) g;
        g2.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        g2.setComposite(AlphaComposite.getInstance(3, 0.5f));
        g2.setPaint(new GradientPaint(c.getWidth() / 2, 1.0f, Color.lightGray, c.getWidth() / 2, c.getHeight(), Color.lightGray));
        g2.fillRoundRect(0, 0, 10, thumbBounds.height - 1, 10, 10);
    }

    protected JButton createIncreaseButton(int orientation) {
        JButton button = new JButton();
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setBorder((Border) null);
        return button;
    }

    protected JButton createDecreaseButton(int orientation) {
        JButton button = new JButton();
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusable(false);
        button.setBorder(new RoundBorder());
        return button;
    }
}
