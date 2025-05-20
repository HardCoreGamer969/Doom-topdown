// Monster.java
public abstract class Monster {
    protected double x, y;
    protected int    health;
    protected int    damage;    // how much it deals on hit
    protected boolean alive = true;

    public Monster(double startX, double startY, int health, int damage) {
        this.x = startX;
        this.y = startY;
        this.health = health;
        this.damage = damage;
    }

    public double getX()   { return x; }
    public double getY()   { return y; }
    public int    getHealth() { return health; }
    public boolean isAlive()  { return alive; }

    /** called by the game when this monster takes a hit **/
    public void takeDamage(int dmg) {
        health -= dmg;
        if (health <= 0) {
            health = 0;
            alive = false;
            onDeath();
        }
    }

    /** deal damage to the player **/
    public void attack(DoomGuy player) {
        player.takeDamage(damage);
    }

    /** movement & AI each tick **/
    public abstract void update(DoomGuy player);

    /** optional death effect **/
    protected void onDeath() {
        // override to spawn gibs, drop items, etc.
    }
}
