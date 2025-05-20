// GameCanvas.java
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class GameCanvas extends JPanel {
    private final DoomGuy player;
    private Vertex[]     vertices;
    private LineDef[]    lines;
    private final List<Monster> monsters = new ArrayList<>();
    private final List<Item>    items    = new ArrayList<>();

    private static class Shot {
        final Line2D.Double line;
        final long expiry;
        Shot(Line2D.Double l, long e) { line = l; expiry = e; }
    }
    private final List<Shot> shots = new ArrayList<>();

    private static final int    CANVAS_SIZE    = 600;
    private static final int    DOT_SIZE       = 16;
    private static final long   SHOT_DURATION  = 10;
    private static final double IMP_RADIUS     = 20.0;
    public  static final double PLAYER_RADIUS  = DOT_SIZE/2.0;

    public GameCanvas(DoomGuy player, Level lvl) {
        this.player   = player;
        this.vertices = lvl.vertices;
        this.lines    = lvl.lines;
        this.monsters.clear(); this.monsters.addAll(lvl.monsters);
        this.items.clear();    this.items.addAll(lvl.items);

        setPreferredSize(new Dimension(CANVAS_SIZE, CANVAS_SIZE));
        setBackground(Color.BLACK);

        addMouseListener(new MouseAdapter(){
            @Override public void mousePressed(MouseEvent e) {
                if (e.getButton()==MouseEvent.BUTTON1
                        && player.getWeapon().tryFire()
                ) {
                    fireHitscan(e.getX(), e.getY());
                    repaint();
                }
            }
        });
    }

    /** Called each tick: reload, AI, pickup **/
    public void update(DoomGuy p) {
        // reload timer
        p.getWeapon().update();
        // monster AI & remove dead
        Iterator<Monster> mit = monsters.iterator();
        while (mit.hasNext()) {
            Monster m = mit.next();
            m.update(p);
            if (!m.isAlive()) mit.remove();
        }
        // item pickup by player
        Iterator<Item> iit = items.iterator();
        while (iit.hasNext()) {
            Item it = iit.next();
            double dx = p.getX() - it.getX(), dy = p.getY() - it.getY();
            if (Math.hypot(dx, dy) <= PLAYER_RADIUS + it.getRadius()) {
                it.apply(p);
                iit.remove();
            }
        }
    }

    private void fireHitscan(int mx, int my) {
        int w = getWidth(), h = getHeight();
        double camX = player.getX() - w/2.0;
        double camY = player.getY() - h/2.0;

        double wx = camX + mx, wy = camY + my;
        double px = player.getX(), py = player.getY();
        double dx = wx - px, dy = wy - py;
        double dist = Math.hypot(dx, dy);
        dx /= dist; dy /= dist;

        double maxDist = 1000;
        double ex = px + dx*maxDist, ey = py + dy*maxDist;

        // hit monsters
        double best = maxDist, ix = ex, iy = ey;
        Monster hitM = null;
        for (Monster m : monsters) {
            double mxv = m.getX(), myv = m.getY();
            double t = (mxv-px)*dx + (myv-py)*dy;
            if (t>0 && t<best) {
                double cx = px+dx*t, cy = py+dy*t;
                if (Math.hypot(mxv-cx,myv-cy) <= IMP_RADIUS) {
                    best = t; ix = cx; iy = cy; hitM = m;
                }
            }
        }

        // hit walls
        for (LineDef ld : lines) {
            Vertex a = vertices[ld.startIndex], b = vertices[ld.endIndex];
            Point2D.Double hit = getLineIntersection(px,py,ex,ey,a.x,a.y,b.x,b.y);
            if (hit != null) {
                double d = hit.distance(px, py);
                if (d < best) {
                    best = d;
                    ix = hit.x; iy = hit.y;
                    hitM = null;
                }
            }
        }

        if (hitM != null) {
            hitM.takeDamage(player.getWeapon().getDamage());
        }

        double sx1 = w/2.0, sy1 = h/2.0;
        double sx2 = ix - camX, sy2 = iy - camY;
        shots.add(new Shot(
                new Line2D.Double(sx1, sy1, sx2, sy2),
                System.currentTimeMillis() + SHOT_DURATION
        ));
    }

    /** Test circle‐to‐wall collision **/
    public boolean canMove(double nx, double ny) {
        for (LineDef ld : lines) {
            Vertex a = vertices[ld.startIndex], b = vertices[ld.endIndex];
            if (pointToSegmentDist(nx, ny, a.x,a.y, b.x,b.y) < PLAYER_RADIUS) {
                return false;
            }
        }
        return true;
    }

    private double pointToSegmentDist(
            double px,double py,
            double x1,double y1,
            double x2,double y2
    ) {
        double dx = x2-x1, dy = y2-y1;
        if (dx==0&&dy==0) return Math.hypot(px-x1,py-y1);
        double t = ((px-x1)*dx+(py-y1)*dy)/(dx*dx+dy*dy);
        t = Math.max(0, Math.min(1, t));
        double projX = x1+t*dx, projY = y1+t*dy;
        return Math.hypot(px-projX, py-projY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth(), h = getHeight();
        Graphics2D g2 = (Graphics2D) g;
        double camX = player.getX() - w/2.0;
        double camY = player.getY() - h/2.0;

        // draw items
        for (Item it : items) it.draw(g2, camX, camY);

        // draw walls
        g2.setColor(Color.WHITE);
        for (LineDef ld : lines) {
            Vertex a = vertices[ld.startIndex], b = vertices[ld.endIndex];
            int x1 = (int)(a.x - camX), y1 = (int)(a.y - camY);
            int x2 = (int)(b.x - camX), y2 = (int)(b.y - camY);
            g2.drawLine(x1, y1, x2, y2);
        }

        // draw monsters
        g2.setColor(Color.GREEN);
        for (Monster m : monsters) {
            int mx = (int)(m.getX() - camX), my = (int)(m.getY() - camY);
            int r  = (int)IMP_RADIUS;
            g2.fillOval(mx - r, my - r, r*2, r*2);
        }

        // draw shots
        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.YELLOW);
        long now = System.currentTimeMillis();
        Iterator<Shot> it = shots.iterator();
        while (it.hasNext()) {
            Shot s = it.next();
            if (now > s.expiry) it.remove();
            else g2.draw(s.line);
        }

        // draw UI
        Weapons wp = player.getWeapon();
        int inMag   = wp.getAmmoInMag();
        int reserve = wp.getReserveAmmo();
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 16));
        g2.drawString("Weapon: " + wp.getName(),           10, 20);
        g2.drawString("In Mag: " + inMag,                 10, 40);
        g2.drawString("Total Ammo: " + (inMag + reserve), 10, 60);
        g2.drawString(
                "HP: " + player.getHealth() +
                        "  AR: " + player.getArmor(),
                10, 80
        );

        // draw player
        int px = w/2 - DOT_SIZE/2, py = h/2 - DOT_SIZE/2;
        g2.setColor(Color.RED);
        g2.fillOval(px, py, DOT_SIZE, DOT_SIZE);
    }

    private Point2D.Double getLineIntersection(
            double x1,double y1,double x2,double y2,
            double x3,double y3,double x4,double y4
    ) {
        double d = (y4-y3)*(x2-x1) - (x4-x3)*(y2-y1);
        if (Math.abs(d) < 1e-6) return null;
        double ua = ((x4-x3)*(y1-y3) - (y4-y3)*(x1-x3)) / d;
        double ub = ((x2-x1)*(y1-y3) - (y2-y1)*(x1-x3)) / d;
        if (ua<0||ua>1||ub<0||ub>1) return null;
        return new Point2D.Double(
                x1+ua*(x2-x1),
                y1+ua*(y2-y1)
        );
    }
}
