package objects.projectiles;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class ProjectileManager {
    public List<Projectile> toRemove = new ArrayList<>();
    public List<Projectile> projectiles = new ArrayList<>();

    public void add(Projectile projectile) {
        this.projectiles.add(projectile);
    }

    public void removeProjectiles() {
        this.projectiles.removeAll(toRemove);
    }

    public void draw(Graphics2D graphics2D) {
        for (Projectile projectile : this.projectiles) {
            projectile.draw(graphics2D);
        }
        this.removeProjectiles();
    }

    public void clear() {
        this.projectiles.clear();
    }
}
