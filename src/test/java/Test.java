import org.bukkit.event.entity.EntityDamageEvent;

public class Test {

    public static void main(String[] args) {
        for(EntityDamageEvent.DamageCause cause : EntityDamageEvent.DamageCause.values()) {
            System.out.println(cause.name());
        }
    }

}
