package xyz.lncvrt.galaxyboxpvp.events;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.projectiles.ProjectileSource;

public class ProjectileHitListener implements Listener {
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if (projectile.getType() == EntityType.WITHER_SKULL) {
            ProjectileSource shooter = projectile.getShooter();
            if (shooter instanceof org.bukkit.entity.Wither) {
                event.getEntity().remove();
            }
        }
    }
}
