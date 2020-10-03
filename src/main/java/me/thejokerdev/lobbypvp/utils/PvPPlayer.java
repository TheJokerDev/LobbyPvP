package me.thejokerdev.lobbypvp.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PvPPlayer {
    private Player p;
    private Location loc;
    private ItemStack[] invContents;
    private ItemStack[] armorContents;
    private Double health;
    private int slotChose;

    public Player getP() {
        return p;
    }

    public void setP(Player p) {
        this.p = p;
    }

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public ItemStack[] getInvContents() {
        return invContents;
    }

    public void setInvContents(ItemStack[] invContents) {
        this.invContents = invContents;
    }

    public ItemStack[] getArmorContents() {
        return armorContents;
    }

    public void setArmorContents(ItemStack[] armorContents) {
        this.armorContents = armorContents;
    }

    public Double getHealth() {
        return health;
    }

    public void setHealth(Double health) {
        this.health = health;
    }

    public int getSlotChose() {
        return slotChose;
    }

    public void setSlotChose(int slotChose) {
        this.slotChose = slotChose;
    }

    public PvPPlayer(Player p){
        this.p = p;
        this.loc = p.getLocation();
        this.invContents = p.getInventory().getContents();
        this.health = p.getHealth();
        this.slotChose = p.getInventory().getHeldItemSlot();
    }
}
