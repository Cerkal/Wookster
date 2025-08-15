package objects.projectiles;

import java.util.ArrayList;
import java.util.List;

public class ProjectileManager {
    public List<Projectile> toRemove = new ArrayList<>();
    public List<Projectile> projectiles = new ArrayList<>();

    public void removeProjectiles() {
        this.projectiles.removeAll(toRemove);
    }
}
