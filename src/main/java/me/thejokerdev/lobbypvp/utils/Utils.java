package me.thejokerdev.lobbypvp.utils;

import me.thejokerdev.lobbypvp.Main;
import me.thejokerdev.lobbypvp.kit.KitManager;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.HashMap;

public class Utils {
    public static ArrayList<Player> startingPlayers =new ArrayList<>();
    public static HashMap<Player, PvPPlayer> savePlayer = new HashMap<>();
    public static ArrayList<Player> queuePlayers = new ArrayList<>();
    public static ArrayList<Player> playingPlayers = new ArrayList<>();
    public static String ct(String msg){
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
    public static Location center(Location var0) {
        return new Location(var0.getWorld(), getRelativeCoord(var0.getBlockX()), getRelativeCoord(var0.getBlockY()), getRelativeCoord(var0.getBlockZ()));
    }
    public static void leavePlayer(Player p){
        PvPPlayer pvPPlayer = savePlayer.get(p);
        if (pvPPlayer != null){
            p.teleport(pvPPlayer.getLoc());
            p.getInventory().setContents(pvPPlayer.getInvContents());
            p.getInventory().setArmorContents(pvPPlayer.getArmorContents());
            p.setHealth(pvPPlayer.getHealth());
            p.getInventory().setHeldItemSlot(pvPPlayer.getSlotChose());
            savePlayer.remove(p);
            if (startingPlayers.contains(p)){
                startingPlayers.remove(p);
            }
            if (playingPlayers.contains(p)){
                playingPlayers.remove(p);
            }
            if (queuePlayers.contains(p)){
                queuePlayers.remove(p);
            }
        }
    }
    public static void teleportPlayers(){
        Player p = queuePlayers.get(0);
        savePlayer.put(p, new PvPPlayer(p));
        Player p2 = queuePlayers.get(1);
        savePlayer.put(p2, new PvPPlayer(p2));
        if (!p.isOnline() || !p2.isOnline()){
            if (!p2.isOnline()){
                p.sendMessage(ct("&c¡"+p2.getName()+"salió!"));
                savePlayer.remove(1);
            }
            if (!p.isOnline()){
                p2.sendMessage(ct("&c¡"+p.getName()+" salió!"));
                savePlayer.remove(0);
            }
            Main.logError("Un jugador no está presente");
            return;
        }
        p.teleport(getFirstLocation());
        p2.teleport(getSecondLocation());
        p.getInventory().clear();
        p.setHealth(p.getMaxHealth());
        p2.getInventory().clear();
        p2.setHealth(p2.getMaxHealth());
        setKit(p);
        setKit(p2);
        startingPlayers.add(p);
        startingPlayers.add(p2);
    }
    public static void startPvP(){
        for (Player all : startingPlayers){
            playingPlayers.add(all);
            startingPlayers.remove(all);
        }

    }
    public static void sendStartingMessage(String msg){
        for (Player p : startingPlayers){
            p.sendMessage(ct(msg));
        }
    }
    public static Location getFirstLocation(){
        Location loc = getLocation(Main.cfg().getString("Locations.first"));
        return loc;
    }
    public static Location getSecondLocation(){
        Location loc = getLocation(Main.cfg().getString("Locations.second"));
        return loc;
    }
    public static String getWorld(){
        return Main.getPlugin().getConfig().getString("Location.world");
    }
    public static void setKit(Player p){
        for (ItemBuilder stack : KitManager.getKit("default").getItems()){
            if (isArmor(stack.build())) {
                if (stack.getType().toString().contains("HELMET")) {
                    p.getInventory().setHelmet(stack.build());
                } else if (stack.getType().toString().contains("CHESTPLATE")) {
                    p.getInventory().setChestplate(stack.build());
                } else if (stack.getType().toString().contains("LEGGINGS")) {
                    p.getInventory().setLeggings(stack.build());
                } else if (stack.getType().toString().contains("BOOTS")) {
                    p.getInventory().setBoots(stack.build());
                }
            } else {
                p.getInventory().addItem(stack.build());
            }
        }
    }
    private static boolean isArmor(final ItemStack itemStack) {
        if (itemStack == null)
            return false;
        final String typeNameString = itemStack.getType().name();
        if (typeNameString.endsWith("_HELMET")
                || typeNameString.endsWith("_CHESTPLATE")
                || typeNameString.endsWith("_LEGGINGS")
                || typeNameString.endsWith("_BOOTS")) {
            return true;
        }

        return false;
    }
    public static ItemBuilder readItem(String var0) {
        ItemBuilder var1 = null;
        String[] var2 = var0.split(",");
        String var3 = var2[0];
        String[] var4 = var3.split(":");
        if (isNumeric(var4[0])) {
            var1 = new ItemBuilder(Material.getMaterial(Integer.parseInt(var4[0])));
        } else {
            var1 = new ItemBuilder(Material.matchMaterial(var4[0]));
        }

        if (var4.length >= 2) {
            var1.setData(Short.parseShort(var4[1]));
        }

        if (var2.length >= 2) {
            int var5 = 1;
            if (isNumeric(var2[1])) {
                var1.setAmount(Integer.parseInt(var2[1]) <= 0 ? 1 : Integer.parseInt(var2[1]));
                ++var5;
            }

            if (var2.length >= 3 && isNumeric(var2[2])) {
                var1.setData(Short.parseShort(var2[2]));
            }

            for(int var6 = var5; var6 < var2.length; ++var6) {
                if (var2[var6] != null && !var2[var6].isEmpty()) {
                    String var7;
                    if (var2[var6].startsWith("lore:")) {
                        var7 = var2[var6].replace("lore:", "");
                        var1.addLore(var7);
                    } else {
                        String[] var8;
                        int var10;
                        int var11;
                        if (var2[var6].startsWith("potion:")) {
                            var7 = var2[var6].replace("potion:", "");
                            var8 = var7.split(":");
                            PotionType[] var17 = PotionType.values();
                            var10 = var17.length;

                            for(var11 = 0; var11 < var10; ++var11) {
                                PotionType var12 = var17[var11];
                                if (var8[0].equalsIgnoreCase(var12.toString())) {
                                    boolean var13;
                                    boolean var14;
                                    if (var8.length == 3) {
                                        var13 = Boolean.parseBoolean(var8[1]);
                                        var14 = Boolean.parseBoolean(var8[2]);
                                    } else {
                                        var13 = false;
                                        var14 = false;
                                    }

                                    var1.setPotion(var12.toString(), var1.getType(), var13, var14);
                                }
                            }
                        } else if (var2[var6].startsWith("name:")) {
                            var7 = var2[var6].replace("name:", "");
                            var1.setTitle(var7);
                        } else {
                            int var9;
                            if (var2[var6].startsWith("leather_color:")) {
                                var7 = var2[var6].replace("leather_color:", "");
                                var8 = var7.split("-");
                                if (var8.length == 3) {
                                    var9 = isNumeric(var8[0]) ? Integer.parseInt(var8[0]) : 0;
                                    var10 = isNumeric(var8[1]) ? Integer.parseInt(var8[1]) : 0;
                                    var11 = isNumeric(var8[2]) ? Integer.parseInt(var8[2]) : 0;
                                } else {
                                    var9 = 0;
                                    var10 = 0;
                                    var11 = 0;
                                }

                                var1.setColor(Color.fromRGB(var9, var10, var11));
                            } else if (var2[var6].equalsIgnoreCase("glowing")) {
                                var1.setGlow(true);
                            } else {
                                Enchantment var15 = Enchantment.getByName(var2[var6].toUpperCase().split(":")[0]);
                                if (var15 != null) {
                                    String var16 = var2[var6].replace(var15.getName().toUpperCase() + ":", "");
                                    var9 = 1;
                                    if (isNumeric(var16)) {
                                        var9 = Integer.parseInt(var16);
                                    }

                                    var1.addEnchantment(var15, var9);
                                }
                            }
                        }
                    }
                }
            }
        }

        return var1;
    }
    public static boolean isNumeric(String var0) {
        try {
            Integer.parseInt(var0);
            return true;
        } catch (NumberFormatException var2) {
            return false;
        }
    }
    private static double getRelativeCoord(int var0) {
        double var1 = (double)var0;
        var1 = var1 < 0.0D ? var1 + 0.5D : var1 + 0.5D;
        return var1;
    }

    public static String getString(Location var0, boolean var1) {
        if (var0 == null) {
            Main.logError("Location null, can't be converted to string");
            return null;
        } else {
            return var1 ? var0.getWorld().getName() + "," + center(var0).getX() + "," + var0.getY() + "," + center(var0).getZ() + "," + 0 + "," + var0.getYaw() : var0.getWorld().getName() + "," + var0.getX() + "," + var0.getY() + "," + var0.getZ() + "," + var0.getPitch() + "," + var0.getYaw();
        }
    }

    public static Location getLocation(String var0) {
        String[] var1 = var0.split(",");
        Location var2 = null;
        if (var1.length < 4) {
            Main.logError("Location can't be obtained from (world,x,y,z needed)'" + var0 + "'");
        } else if (var1.length < 6) {
            var2 = new Location(Bukkit.getWorld(var1[0]), Double.parseDouble(var1[1]), Double.parseDouble(var1[2]), Double.parseDouble(var1[3]));
        } else {
            try {
                var2 = new Location(Bukkit.getWorld(var1[0]), Double.parseDouble(var1[1]), Double.parseDouble(var1[2]), Double.parseDouble(var1[3]), Float.parseFloat(var1[5]), Float.parseFloat("0"));
            } catch (NullPointerException var4) {
                Main.logError("Location can't be obtained from '" + var0 + "'");
            }
        }

        return var2;
    }
}
