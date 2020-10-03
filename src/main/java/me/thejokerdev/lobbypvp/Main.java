package me.thejokerdev.lobbypvp;

import me.thejokerdev.lobbypvp.commands.PvPCommand;
import me.thejokerdev.lobbypvp.kit.KitManager;
import me.thejokerdev.lobbypvp.listener.PlayerListener;
import me.thejokerdev.lobbypvp.utils.FileUtil;
import me.thejokerdev.lobbypvp.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

public final class Main extends JavaPlugin {
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getCommand("pvp").setExecutor(new PvPCommand());
        getCommand("pvp").setTabCompleter(new PvPCommand());
        File kits = new File(getDataFolder()+File.separator+"kits");
        if (!kits.exists()){
            this.saveResource("kits/default.yml", false);
        }
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        KitManager.initKits();
    }
    public static void logError(String var0) {
        System.out.println("[LobbyPvP] ERROR: " + var0);
    }

    public static Main getPlugin(){
        return instance;
    }

    public static FileUtil cfg(){
        return new FileUtil(instance.getDataFolder(), "config.yml");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
