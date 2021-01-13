package de.jeff_media.PluginName;

import org.bukkit.ChatColor;

public class Messages {

    public final String TEST1, TEST2, TEST3;
    public final String CONFIG_RELOADED;

    private final Main main;
    private final String messagePrefix = "message-";

    public Messages(Main main) {
        this.main = main;

        TEST1 = load("test","&aThis is a test message.");
        TEST2 = load("test","&bThis is a test message.");
        TEST3 = load("test","&cThis is a test message.");

        CONFIG_RELOADED = color(String.format("&a%s has been reloaded.",main.getName()));
    }

    private String load(String path, String defaultMessage) {
        return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString(messagePrefix + path,defaultMessage));
    }

    private String color(String message) {
        return ChatColor.translateAlternateColorCodes('&',message);
    }

}
