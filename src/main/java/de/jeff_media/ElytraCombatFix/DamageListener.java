package de.jeff_media.ElytraCombatFix;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DamageListener implements Listener {

    private final Main main;
    private final HashMap<UUID, ArrayList<ItemStack>> seizedItems = new HashMap<>();

    public DamageListener(Main main) {
        this.main=main;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDamage(EntityDamageEvent entityDamageEvent) {
        if(!(entityDamageEvent.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) entityDamageEvent.getEntity();

        if(!hasElytraEquipped(player)) return;
        if(!isDamageTypeEnabled(entityDamageEvent.getCause())) return;

        seizeAndScheduleRefund(player,player.getInventory().getChestplate());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent playerDeathEvent) {
        giveAllSeizedItemsBack(playerDeathEvent.getEntity());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
        giveAllSeizedItemsBack(playerQuitEvent.getPlayer());
    }

    private boolean isDamageTypeEnabled(EntityDamageEvent.DamageCause cause) {
        for(String entry : main.getConfig().getStringList(Config.DAMAGE_TYPES)) {
            if(entry.equalsIgnoreCase(cause.name())) {
                return true;
            }
        }
        return false;
    }

    private void storeSeizedItem(Player player, ItemStack item) {
        if(item==null) return;
        if(seizedItems.containsKey(player.getUniqueId())) {
            seizedItems.get(player.getUniqueId()).add(item);
        } else {
            ArrayList<ItemStack> list = new ArrayList<>();
            list.add(item);
            seizedItems.put(player.getUniqueId(),list);
        }
    }

    private void giveAllSeizedItemsBack(Player player) {
        for(ItemStack item : getSeizedItems(player)) {
            giveOrDrop(player,item);
        }
        getSeizedItems(player).clear();
    }

    private @NotNull ArrayList<ItemStack> getSeizedItems(Player player) {
        if(seizedItems.containsKey(player.getUniqueId())) {
            return seizedItems.get(player.getUniqueId());
        } else {
            return new ArrayList<ItemStack>();
        }
    }

    private boolean hasElytraEquipped(Player player) {
        ItemStack chestplate = player.getInventory().getChestplate();
        if(chestplate == null) return false;
        return chestplate.getType() == Material.ELYTRA;
    }

    private void seizeAndScheduleRefund(Player player, ItemStack item) {
        storeSeizedItem(player,item);
        player.getInventory().setChestplate(null);
        if(main.getConfig().getBoolean(Config.SHOW_ACTIONBAR_MESSAGE)) {
            Messages.showActionBarMessage(player,main.messages.SEIZED
                    .replaceAll("\\{time}",
                            String.valueOf(main.getConfig().getDouble(Config.TIME))));
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
            giveOrDrop(player,item);
            getSeizedItems(player).remove(item);
            if(main.getConfig().getBoolean(Config.SHOW_ACTIONBAR_MESSAGE)) {
                Messages.showActionBarMessage(player,main.messages.RETURNED);
            }
        }, (long) (main.getConfig().getDouble("time")*20L));
    }

    private void giveOrDrop(Player player, ItemStack item) {
        if(player.getInventory().getChestplate() == null) {
            player.getInventory().setChestplate(item);
            return;
        }
        for(Map.Entry<Integer,ItemStack> leftover : player.getInventory().addItem(item).entrySet()) {
            player.getWorld().dropItemNaturally(player.getLocation(),leftover.getValue());
        }
    }

}
