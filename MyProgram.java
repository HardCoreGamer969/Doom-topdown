// MyProgram.java
import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MyProgram {
    private static boolean up, down, left, right;

    public static void main(String[] args) {
        // load level
        Level lvl = Level1.create();
        // spawn player
        DoomGuy player = new DoomGuy(lvl.playerStartX, lvl.playerStartY);
        // create canvas
        GameCanvas canvas = new GameCanvas(player, lvl);

        // build frame
        JFrame frame = new JFrame("Down-Doom: Level 1");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(canvas);
        frame.pack();
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // input
        frame.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_W: up    = true; break;
                    case KeyEvent.VK_S: down  = true; break;
                    case KeyEvent.VK_A: left  = true; break;
                    case KeyEvent.VK_D: right = true; break;
                    case KeyEvent.VK_R: player.getWeapon().reload(); break;
                }
            }
            @Override public void keyReleased(KeyEvent e) {
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_W: up    = false; break;
                    case KeyEvent.VK_S: down  = false; break;
                    case KeyEvent.VK_A: left  = false; break;
                    case KeyEvent.VK_D: right = false; break;
                }
            }
        });

        // game loop
        new Timer(1000/60, ev -> {
            double dx=0, dy=0, sp=player.getSpeed();
            if(up)    dy -= sp;
            if(down)  dy += sp;
            if(left)  dx -= sp;
            if(right) dx += sp;

            double nx=player.getX()+dx, ny=player.getY()+dy;
            if(canvas.canMove(nx, ny)) player.moveBy(dx, dy);

            canvas.update(player);
            canvas.repaint();
        }).start();
    }
}
