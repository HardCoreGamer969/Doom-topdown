// Level.java
import java.util.List;

public class Level {
    public final Vertex[] vertices;
    public final LineDef[] lines;
    public final double playerStartX, playerStartY;
    public final List<Monster> monsters;
    public final List<Item> items;

    public Level(
            Vertex[] vertices,
            LineDef[] lines,
            double playerStartX,
            double playerStartY,
            List<Monster> monsters,
            List<Item> items
    ) {
        this.vertices      = vertices;
        this.lines         = lines;
        this.playerStartX  = playerStartX;
        this.playerStartY  = playerStartY;
        this.monsters      = monsters;
        this.items         = items;
    }
}
