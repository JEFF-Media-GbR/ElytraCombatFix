package de.jeff_media.PluginName.commands;

import de.jeff_media.PluginName.Main;
import de.jeff_media.PluginName.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ReloadCommand {

    public static boolean run(Main main, CommandSender commandSender, Command command, String alias, String[] args) {

        if(!commandSender.hasPermission(Permissions.ALLOW_RELOAD)) {
            commandSender.sendMessage(command.getPermissionMessage());
            return true;
        }
        main.reload();
        commandSender.sendMessage(main.messages.CONFIG_RELOADED);
        return true;
    }

}
