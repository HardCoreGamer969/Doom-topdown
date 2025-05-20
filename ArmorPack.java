// ArmorPack.java
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

public class ArmorPack extends Item {
    private int armorAmount;
    public ArmorPack(double x, double y, int armorAmount) {
        super(x, y, 16);
        this.armorAmount = armorAmount;
    }

    @Override
    public void apply(DoomGuy player) {
        player.addArmor(armorAmount);
    }

    @Override
    public void draw(Graphics2D g2, double camX, double camY) {
        int px = (int)(x - camX);
        int py = (int)(y - camY);
        int size = (int)radius * 2;

        g2.setColor(Color.CYAN);
        g2.fillOval(px - size/2, py - size/2, size, size);

        g2.setColor(Color.BLUE);
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(px - size/2, py - size/2, size, size);
    }
}
