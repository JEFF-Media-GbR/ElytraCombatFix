package de.jeff_media.PluginName;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Messages {

    public final String CONFIG_RELOADED, SEIZED, RETURNED;

    private final Main main;
    private final String messagePrefix = "message-";

    public Messages(Main main) {
        this.main = main;


        CONFIG_RELOADED = color(String.format("&a%s has been reloaded.",main.getName()));
        SEIZED = load("seized", "&cYour elytra has been seized for {time} seconds!");
        RETURNED = load("returned", "&aYour elytra has been returned!");
    }

    private String load(String path, String defaultMessage) {
        return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString(messagePrefix + path,defaultMessage));
    }

    private String color(String message) {
        return ChatColor.translateAlternateColorCodes('&',message);
    }

    public static void showActionBarMessage(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

}
