// DoomGuy.java
public class DoomGuy {
    private double x, y;
    private final double speed = 4.0;

    private int health;
    private int armor;  // bonus armor up to 100
    private final Weapons weapon;

    public static final int MAX_HEALTH = 100;
    public static final int MAX_ARMOR  = 100;

    public DoomGuy(double startX, double startY) {
        this.x = startX;
        this.y = startY;
        this.health = MAX_HEALTH;
        this.armor  = 25;  // starts with 25 bonus armor
        // name, magSize, reserve, fireRate, damage, reloadMs
        this.weapon = new Weapons("Pistol", 12, 36, 5.0, 10, 1000);
    }

    public double getX()            { return x; }
    public double getY()            { return y; }
    public double getSpeed()        { return speed; }
    public Weapons getWeapon()      { return weapon; }
    public int     getHealth()      { return health; }
    public int     getArmor()       { return armor; }

    public void moveBy(double dx, double dy) {
        x += dx; y += dy;
    }

    /** apply damage: armor absorbs first, rest to health **/
    public void takeDamage(int dmg) {
        if (armor >= dmg) {
            armor -= dmg;
        } else {
            int leftover = dmg - armor;
            armor = 0;
            health = Math.max(0, health - leftover);
        }
    }

    /** heal up to max **/
    public void heal(int amount) {
        health = Math.min(MAX_HEALTH, health + amount);
    }

    /** pick up armor, up to max **/
    public void addArmor(int amount) {
        armor = Math.min(MAX_ARMOR, armor + amount);
    }
}
