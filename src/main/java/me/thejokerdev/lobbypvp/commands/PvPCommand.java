package me.thejokerdev.lobbypvp.commands;

import me.thejokerdev.lobbypvp.Main;
import me.thejokerdev.lobbypvp.kit.KitManager;
import me.thejokerdev.lobbypvp.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PvPCommand implements CommandExecutor, TabCompleter {
    private int seconds = 5;
    private final List<String> cmds;
    public PvPCommand(){
        cmds = new ArrayList<>();
        cmds.add("help");
        cmds.add("setfirstspawn");
        cmds.add("setsecondspawn");
        cmds.add("kitPreview");
        cmds.add("setWorld");
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            Bukkit.getConsoleSender().sendMessage(Utils.ct("&4¡Este comando es solo para jugadores!"));
            return true;
        }
        Player p = (Player) sender;
        if (args.length == 0){
            if (Utils.playingPlayers.contains(p)){
                Utils.leavePlayer(p);
            } else {
                if (Utils.queuePlayers.size() >2){
                    p.sendMessage(Utils.ct("&cEn este momento, ya están jugando 2 personas en PvP."));
                    return true;
                }
                if (!Utils.queuePlayers.contains(p)){
                    Utils.queuePlayers.add(p);
                    p.sendMessage(Utils.ct("&a¡Has entrado a la cola de PvP!"));
                } else {
                    p.sendMessage(Utils.ct("&c¡Has salido de la cola de PvP!"));
                }
                if (Utils.queuePlayers.size() == 2){
                    p.sendMessage(Utils.ct("&a¡Has entrado a la cola de PvP!"));
                    Utils.teleportPlayers();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (seconds == 0){
                                Utils.sendStartingMessage("&e&l¡A PELEAR!");
                                Utils.startPvP();
                                cancel();
                            }
                            if (seconds == 5){
                                Utils.sendStartingMessage("&aIniciando PvP en 5 segundos...");
                            }
                            if (seconds<4 && seconds != 0){
                                Utils.sendStartingMessage("&aIniciando pvp en "+seconds+" segundos...");
                            }
                            --seconds;
                        }
                    }.runTaskTimerAsynchronously(Main.getPlugin(), 0L, 20L);
                    return true;
                }
            }
            return true;
        }
        if (!p.hasPermission("lobbypvp.admin")){
            return true;
        }
        if (args.length == 1){
            String arg1 = args[0];
            if (arg1.equalsIgnoreCase("help")){
                help(p);
                return true;
            }
            if (arg1.equalsIgnoreCase("setfirstspawn")){
                Location location = p.getLocation();
                Main.cfg().set("Locations.first", Utils.getString(location, true));
                Main.cfg().save();
                Main.cfg().reload();
                p.sendMessage(Utils.ct("&a¡Se ha establecido correctamente el primer punto de aparición!"));
                return true;
            }
            if (arg1.equalsIgnoreCase("setworld")){
                Main.cfg().set("Locations.world", p.getLocation().getWorld().getName());
                Main.cfg().save();
                Main.cfg().reload();
                p.sendMessage(Utils.ct("&a¡Se ha establecido correctamente el mundo de Lobby!"));
                return true;
            }
            if (arg1.equalsIgnoreCase("setsecondspawn")){
                Location location = p.getLocation();
                Main.cfg().set("Locations.second", Utils.getString(location, true));
                Main.cfg().save();
                Main.cfg().reload();
                p.sendMessage(Utils.ct("&a¡Se ha establecido correctamente el segundo punto de aparición!"));
                if (Main.cfg().get("Locations.first") != null && Main.cfg().get("Locations.second") != null){
                    p.sendMessage(" ");
                    p.sendMessage(Utils.ct("&e¡Ya se puede jugar, usa &7/pvp &epara empezar!"));
                }
                return true;
            }
            if (arg1.equalsIgnoreCase("kitPreview")){
                if (KitManager.getKit("default").getItems() == null) {
                  p.sendMessage(Utils.ct("&c¡No hay kit para luchar aún!"));
                } else {
                    Utils.setKit(p);
                }
                return true;
            }
        }
        return true;
    }

    public void help(Player p){
        String[] msg = new String[]{"&7==========| &aLobby PvP &6"+ Main.getPlugin().getDescription().getVersion()+" &7|=========="
                , "&7 - /pvp help &a- &bMuestra esta ayuda.", "&7 - /pvp setfirstspawn &a- &bEstablece el punto de aparición del primer jugador.",
                "&7 - /pvp setsecondspawn &a- &bEstablece el punto de aparición del segundo jugador.",
                "&7 - /pvp kitPreview &a- &bMuestra el kit establecido.", "&7 - /pvp setWorld &a- &bSetea el mundo de la Lobby."};
        for (String s : msg) {
            p.sendMessage(Utils.ct(s));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)){
            return null;
        }
        if (sender.hasPermission("lobbypvp.admin") && args.length == 1){
            ArrayList<String> arrayList = new ArrayList<>();
            StringUtil.copyPartialMatches(args[0], this.cmds, arrayList);
            Collections.sort(arrayList);
            return arrayList;
        }

        return null;
    }
}
