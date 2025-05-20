// Level1.java
import java.util.List;
import java.util.ArrayList;

public class Level1 {
    public static Level create() {
        // 1) All verts (chamber, corridor, alcove)
        Vertex[] verts = {
                new Vertex(100, 100),  // 0: Chamber BL
                new Vertex(500, 100),  // 1: Chamber BR
                new Vertex(500, 500),  // 2: Chamber TR
                new Vertex(100, 500),  // 3: Chamber TL

                new Vertex(650, 300),  // 4: Corridor end
                new Vertex(500, 300),  // 5: Chamber→Corridor S
                new Vertex(500, 350),  // 6: Chamber→Corridor N

                new Vertex(650, 350),  // 7: Corridor→Alcove E
                new Vertex(550, 350),  // 8: Alcove E corner
                new Vertex(550, 400),  // 9: Alcove NE
                new Vertex(500, 400)   // 10: Alcove NW
        };

        // 2) Walls — **notice there is NO LineDef(5,6) or LineDef(7,8)**
        LineDef[] lines = {
                // Main chamber (omitting the vertical 5→6 so it’s open)
                new LineDef(0,1),
                new LineDef(1,5),
                new LineDef(6,2),
                new LineDef(2,3),
                new LineDef(3,0),

                // East corridor (no 7→8, so alcove entry is open)
                new LineDef(5,4),
                new LineDef(4,7),
                new LineDef(7,6),

                // Secret alcove
                new LineDef(6,10),
                new LineDef(10,9),
                new LineDef(9,8),
                new LineDef(8,7)
        };

        // 3) Player spawn in chamber center
        double spawnX = 300, spawnY = 300;

        // 4) Monsters
        List<Monster> monsters = new ArrayList<>();
        monsters.add(new Imp(350, 250)); // center patrol
        monsters.add(new Imp(450, 350)); // corridor guard
        monsters.add(new Imp(600, 325)); // alcove lurker

        // 5) Items
        List<Item> items = new ArrayList<>();
        items.add(new HealthPack(300, 300, 25)); // chamber health
        items.add(new ArmorPack(450, 150, 50));  // corridor armor
        items.add(new HealthPack(550, 380, 50)); // alcove health

        // 6) Build and return
        return new Level(verts, lines, spawnX, spawnY, monsters, items);
    }
}
