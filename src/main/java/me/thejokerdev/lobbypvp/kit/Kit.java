package me.thejokerdev.lobbypvp.kit;

import me.thejokerdev.lobbypvp.Main;
import me.thejokerdev.lobbypvp.utils.FileUtil;
import me.thejokerdev.lobbypvp.utils.ItemBuilder;
import me.thejokerdev.lobbypvp.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Kit {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ItemBuilder> getItems() {
        return items;
    }

    public void setItems(List<ItemBuilder> items) {
        this.items = items;
    }

    public FileUtil getConfig() {
        return config;
    }

    public void setConfig(FileUtil config) {
        this.config = config;
    }

    private List<ItemBuilder> items;
    private FileUtil config;

    public Kit(String fileName){
        this.items = new ArrayList();

        FileUtil file = new FileUtil(Main.getPlugin().getDataFolder(), "kits" + File.separator + fileName + ".yml");
        this.name =fileName;
        this.config = file;
        this.items.clear();
        for (String str : this.config.getStringList("items")) {
            try {
                this.items.add(Utils.readItem(str));
            } catch (NullPointerException nullPointerException) {
                Main.logError("The kit '" + fileName + "' has skipped the item \"" + str.toString() + "\" due to a syntax error");
            }
        }
        KitManager.kits.put(fileName, this);
    }
}
