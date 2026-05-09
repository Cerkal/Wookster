package objects.projectiles;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProjectileManager {
    public List<Projectile> toRemove = new ArrayList<>();
    public List<Projectile> projectiles = new ArrayList<>();

    public void add(Projectile projectile) {
        this.projectiles.add(projectile);
    }

    public void update() {
        if (toRemove.isEmpty()) return;
        Iterator<Projectile> it = projectiles.iterator();
        while (it.hasNext()) {
            if (toRemove.contains(it.next())) {
                it.remove();
            }
        }
        toRemove.clear();
    }

    public void draw(Graphics2D graphics2D) {
        for (Projectile projectile : new ArrayList<>(this.projectiles)) {
            projectile.draw(graphics2D);
        }
    }

    public void clear() {
        this.projectiles.clear();
        this.toRemove.clear();
    }
}
