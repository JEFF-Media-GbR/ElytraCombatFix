package de.jeff_media.ElytraCombatFix.commands;

import de.jeff_media.ElytraCombatFix.Main;
import de.jeff_media.ElytraCombatFix.Permissions;
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
