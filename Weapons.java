// Weapons.java
public class Weapons {
    private final String name;
    private final int magSize;
    private int ammoInMag;
    private int reserveAmmo;
    private final double fireRate;      // shots per sec
    private final int damage;           // damage per hit
    private long lastFired;

    private final long reloadDuration;
    private boolean reloading;
    private long reloadStartTime;

    public Weapons(
            String name,
            int magSize,
            int initialReserve,
            double fireRate,
            int damage,
            long reloadDuration
    ) {
        this.name           = name;
        this.magSize        = magSize;
        this.ammoInMag      = magSize;
        this.reserveAmmo    = initialReserve;
        this.fireRate       = fireRate;
        this.damage         = damage;
        this.reloadDuration = reloadDuration;
        this.lastFired      = 0;
        this.reloading      = false;
    }

    public void update() {
        if (reloading &&
                System.currentTimeMillis() - reloadStartTime >= reloadDuration
        ) {
            int needed   = magSize - ammoInMag;
            int toLoad   = Math.min(needed, reserveAmmo);
            ammoInMag   += toLoad;
            reserveAmmo -= toLoad;
            reloading    = false;
        }
    }

    public boolean tryFire() {
        long now = System.currentTimeMillis();
        if (reloading || ammoInMag <= 0) return false;
        if (now - lastFired >= 1000.0 / fireRate) {
            ammoInMag--;
            lastFired = now;
            return true;
        }
        return false;
    }

    public void reload() {
        if (!reloading && ammoInMag < magSize && reserveAmmo > 0) {
            reloading      = true;
            reloadStartTime = System.currentTimeMillis();
        }
    }

    // getters
    public String getName()        { return name; }
    public int    getAmmoInMag()   { return ammoInMag; }
    public int    getReserveAmmo() { return reserveAmmo; }
    public boolean isReloading()   { return reloading; }
    public double getFireRate()    { return fireRate; }
    public int    getDamage()      { return damage; }
}
