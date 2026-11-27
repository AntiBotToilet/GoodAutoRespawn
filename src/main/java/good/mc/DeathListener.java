package good.mc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class DeathListener implements Listener {

    private final AutoRespawn plugin;

    public DeathListener(AutoRespawn plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        long delay = plugin.getConfig().getLong("respawn-delay-ticks", 5L);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnline() && player.isDead()) {
                    player.spigot().respawn();
                }
            }
        }.runTaskLater(plugin, delay);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        boolean useCustomSpawn = plugin.getConfig().getBoolean("use-custom-spawn", false);
        if (useCustomSpawn) {
            String worldName = plugin.getConfig().getString("spawn.world", "spawn");
            double x = plugin.getConfig().getDouble("spawn.x", 0);
            double y = plugin.getConfig().getDouble("spawn.y", 64);
            double z = plugin.getConfig().getDouble("spawn.z", 0);
            float yaw = (float) plugin.getConfig().getDouble("spawn.yaw", 0);
            float pitch = (float) plugin.getConfig().getDouble("spawn.pitch", 0);

            if (Bukkit.getWorld(worldName) != null) {
                Location loc = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
                event.setRespawnLocation(loc);
            }
        }

        if (plugin.getConfig().getBoolean("title.enabled", true)) {
            String title = plugin.getMessagesManager().get("title.title");
            String subtitle = plugin.getMessagesManager().get("title.subtitle");
            int fadeIn = plugin.getConfig().getInt("title.fade-in-ticks", 10);
            int stay = plugin.getConfig().getInt("title.stay-ticks", 40);
            int fadeOut = plugin.getConfig().getInt("title.fade-out-ticks", 10);

            player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        }
    }
}