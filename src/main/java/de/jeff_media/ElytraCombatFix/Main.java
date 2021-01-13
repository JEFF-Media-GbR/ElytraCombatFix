package de.jeff_media.ElytraCombatFix;

import de.jeff_media.ElytraCombatFix.commands.MainCommand;
import de.jeff_media.PluginUpdateChecker.PluginUpdateChecker;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static final String SPIGOT_RESOURCE_ID = "123456789";
    private static final int BSTATS_ID = 9993;
    private static final String UPDATECHECKER_LINK_API = "https://api.spigotmc.org/legacy/update.php?resource="+SPIGOT_RESOURCE_ID;
    private static final String UPDATECHECKER_LINK_DOWNLOAD = "https://www.spigotmc.org/resources/"+SPIGOT_RESOURCE_ID;
    private static final String UPDATECHECKER_LINK_CHANGELOG = "https://www.spigotmc.org/resources/"+SPIGOT_RESOURCE_ID+"/updates";
    private static final String UPDATECHECKER_LINK_DONATE = "https://paypal.me/mfnalex";

    private PluginUpdateChecker updateChecker;

    public Messages messages;

    @Override
    public void onEnable() {
        reload();
        registerMetrics();
        getCommand("elytracombatfix").setExecutor(new MainCommand(this));
        Bukkit.getPluginManager().registerEvents(new DamageListener(this),this);
    }

    public void reload() {
        createConfig();
        reloadConfig();
        initUpdateChecker();

        messages = new Messages(this);
    }

    private void createConfig() {
        saveDefaultConfig();

        getConfig().addDefault(Config.CHECK_FOR_UPDATES, "true");
        getConfig().addDefault(Config.CHECK_FOR_UPDATES_INTERVAL, 4);
        getConfig().addDefault(Config.TIME,5.0D);
        getConfig().addDefault(Config.SHOW_ACTIONBAR_MESSAGE, true);
        getConfig().addDefault(Config.DAMAGE_TYPES,ConfigUtils.getAllDamageTypes());
    }

    private void registerMetrics() {
        Metrics metrics = new Metrics(this,BSTATS_ID);

        metrics.addCustomChart(new Metrics.SimplePie("check_for_updates", () -> getConfig().getString(Config.CHECK_FOR_UPDATES)));
        metrics.addCustomChart(new Metrics.SimplePie("check_for_updates_interval", () -> String.valueOf(getConfig().getInt(Config.CHECK_FOR_UPDATES_INTERVAL))));
        metrics.addCustomChart(new Metrics.SimplePie("time", () -> String.valueOf(getConfig().getDouble(Config.TIME))));
    }

    private void initUpdateChecker() {
        if(updateChecker == null) {
            updateChecker = new PluginUpdateChecker(this,
                    UPDATECHECKER_LINK_API,
                    UPDATECHECKER_LINK_DOWNLOAD,
                    UPDATECHECKER_LINK_CHANGELOG,
                    UPDATECHECKER_LINK_DONATE);
        } else {
            updateChecker.stop();
        }

        switch(getConfig().getString(Config.CHECK_FOR_UPDATES).toLowerCase()) {
            case "true":
                updateChecker.check((long) (getConfig().getDouble(Config.CHECK_FOR_UPDATES_INTERVAL) * 60 * 60));
                break;
            case "false":
                break;
            default:
                updateChecker.check();
        }
    }
}
