
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

enum DisasterCategory {
    EARTHQUAKE, FLOOD, FIRE
}

class Alert {
    private String title;
    private DisasterCategory category;
    private String location;
    private String severity;
    private String instructions;
    public Alert(String title, DisasterCategory category, String location, String severity, String instructions) {
        this.title = title;
        this.category = category;
        this.location = location;
        this.severity = severity;
        this.instructions = instructions;
    }
    public DisasterCategory getCategory() {
        return category;
    }
    @Override
    public String toString() {
        return "[\n title=" + title + "\n category=" + category + "\n location=" + location + "\n severity=" + severity
                + "\n instructions=" + instructions + "\n]";
    }
}

class Citizen { // observer
    private String name;
    private int id;
    private static int cnt = 0;
    private List<String> alertLog = new ArrayList<>();

    public Citizen(String name) {
        this.name = name;
        this.id = ++cnt;
    }
    public void update(Alert alert) {
        alertLog.add(alert.toString());
        System.out.println("Alerting "+name+ " : \n" + alert.toString());
        System.out.println();
    }
    public void showNotifications() {
        System.out.println("Alerts received by " + name + " : ");
        for(String s : alertLog) System.out.println(s);
        System.out.println();
    }
}

class DisasterAlertSystem { // subject
    private Map<DisasterCategory, List<Citizen>> subscribers = new HashMap<>();
    private List<Citizen> registeredCitizens = new ArrayList<>();
    public DisasterAlertSystem() {
        for (DisasterCategory c : DisasterCategory.values()) {
            subscribers.put(c, new ArrayList<>());
        }
    }
    public void register(Citizen citizen) {
        if(!registeredCitizens.contains(citizen)) registeredCitizens.add(citizen);
    }
    public void subscribe(Citizen citizen, DisasterCategory c) {
        List<Citizen> curList = subscribers.get(c);
        if(!curList.contains(citizen)) curList.add(citizen);
    }
    public void unsubscribe(Citizen citizen, DisasterCategory c) {
        List<Citizen> curList = subscribers.get(c);
        if(curList.contains(citizen))  curList.remove(citizen);
    }
    public void publishAlert(Alert alert) {
        List<Citizen> curList = subscribers.get(alert.getCategory());
        for(Citizen citizen : curList) citizen.update(alert);
        System.out.println();
    }
}


public class task1 {
    public static void main(String[] args) {
        System.out.println();
        DisasterAlertSystem bdAlert = new DisasterAlertSystem();

        Citizen citizen1 = new Citizen("Hasan");
        Citizen citizen2 = new Citizen("Sobuj");
        Citizen citizen3 = new Citizen("Milon");

        bdAlert.register(citizen1);
        bdAlert.register(citizen2);
        bdAlert.register(citizen3);

        bdAlert.subscribe(citizen1, DisasterCategory.EARTHQUAKE);
        bdAlert.subscribe(citizen2, DisasterCategory.FLOOD);
        bdAlert.subscribe(citizen3, DisasterCategory.FIRE);
        bdAlert.subscribe(citizen3, DisasterCategory.EARTHQUAKE);

        Alert eqAlert1 = new Alert("Magnitude 6.0 Earthquake",
                                 DisasterCategory.EARTHQUAKE,
                                  "Dhaka",
                                   "HIGH",
                                    "Dont panic. Drop, cover, hold.");

        bdAlert.publishAlert(eqAlert1);

        Alert floodAlert = new Alert("Flood Warning",
                                     DisasterCategory.FLOOD,
                                      "Chittagong",
                                       "HIGH",
                                        "Move to high ground.");

        bdAlert.publishAlert(floodAlert);

        Alert fireAlert = new Alert("Fire Hazard",
                                     DisasterCategory.FIRE,
                                      "Rajshahi",
                                       "MEDIUM",
                                        "Evacuate if within 1 Km");

        bdAlert.publishAlert(fireAlert);

        bdAlert.unsubscribe(citizen3, DisasterCategory.EARTHQUAKE);
        bdAlert.subscribe(citizen2, DisasterCategory.EARTHQUAKE);
     
        Alert eqAlert2 = new Alert("Magnitude 3.0 Earthquake",
                                 DisasterCategory.EARTHQUAKE,
                                  "Sylhet",
                                   "LOW",
                                    "Dont panic. Drop, cover, hold.");

        bdAlert.publishAlert(eqAlert2);

        citizen1.showNotifications();
        citizen2.showNotifications();
        citizen3.showNotifications();

    }
}
