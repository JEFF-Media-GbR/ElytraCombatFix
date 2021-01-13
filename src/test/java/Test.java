import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.Collections;

public class Test {

    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        for(EntityDamageEvent.DamageCause cause : EntityDamageEvent.DamageCause.values()) {
            list.add(cause.name());
        }
        Collections.sort(list);
        for(String name : list) {
            System.out.println("  "+name+": true");
        }
    }

}
