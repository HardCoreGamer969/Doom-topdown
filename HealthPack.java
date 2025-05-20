// HealthPack.java
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

public class HealthPack extends Item {
    private int healAmount;
    public HealthPack(double x, double y, int healAmount) {
        super(x, y, 16);  // pack radius 16
        this.healAmount = healAmount;
    }

    @Override
    public void apply(DoomGuy player) {
        player.heal(healAmount);
    }

    @Override
    public void draw(Graphics2D g2, double camX, double camY) {
        int px = (int)(x - camX);
        int py = (int)(y - camY);
        int size = (int)radius * 2;

        g2.setColor(Color.GREEN);
        g2.fillRect(px - size/2, py - size/2, size, size);

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(px - size/4, py, px + size/4, py);
        g2.drawLine(px, py - size/4, px, py + size/4);
    }
}
