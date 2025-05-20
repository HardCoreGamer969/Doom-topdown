public abstract class Item {
    protected double x, y;
    protected double radius;  // for collision

    public Item(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getRadius() { return radius; }

    // apply effect to player
    public abstract void apply(DoomGuy player);
    // draw at screen position (world->screen via cam offsets)
    public abstract void draw(java.awt.Graphics2D g2, double camX, double camY);
}