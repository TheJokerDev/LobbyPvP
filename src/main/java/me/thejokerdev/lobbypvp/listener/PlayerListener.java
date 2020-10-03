package me.thejokerdev.lobbypvp.listener;

import me.thejokerdev.lobbypvp.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        Player p = e.getPlayer();

        if (Utils.getFirstLocation().getWorld().equals(p.getLocation().getWorld())){
            if (Utils.startingPlayers.contains(p)){
                p.teleport(p.getLocation());
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        Player p = e.getEntity();
        if (Utils.getFirstLocation().getWorld().equals(p.getLocation().getWorld())){
            if (Utils.playingPlayers.contains(p)) {
                e.setDeathMessage(null);
                e.setDeathMessage(Utils.ct("&a"+p.getName() +"&e murió a manos de &c "+p.getKiller()));
                p.spigot().respawn();
                Utils.leavePlayer(p);
                Utils.leavePlayer(p.getKiller());
            }
        }
    }
}
