package de.jeff_media.PluginName;

import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;

public class ConfigUtils {

    static ArrayList<String> getAllDamageTypes() {
        ArrayList<String> list = new ArrayList<>();
        for(EntityDamageEvent.DamageCause cause : EntityDamageEvent.DamageCause.values()) {
            list.add(cause.name());
        }
        return list;
    }

}
