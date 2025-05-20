// Imp.java
public class Imp extends Monster {
    private static final int IMP_HEALTH = 30;
    private static final int IMP_DAMAGE = 8;
    private static final double SPEED   = 2.0;

    public Imp(double startX, double startY) {
        super(startX, startY, IMP_HEALTH, IMP_DAMAGE);
    }

    @Override
    public void update(DoomGuy player) {
        if (!alive) return;
        // simple chase AI: move toward player
        double dx = player.getX() - x;
        double dy = player.getY() - y;
        double len = Math.hypot(dx, dy);
        if (len > 0) {
            x += dx/len * SPEED;
            y += dy/len * SPEED;
        }
        // if close enough, attack
        if (Math.hypot(player.getX()-x, player.getY()-y) < 20) {
            attack(player);
        }
    }

    @Override
    protected void onDeath() {
        System.out.println("Imp died!");
    }
}
