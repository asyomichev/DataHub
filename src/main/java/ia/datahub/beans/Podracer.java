package ia.datahub.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Random;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Podracer bean
 */
@Entity
public class Podracer {

    @Id
    public String model;

    public String affiliation;
    public String passenger;
    public String era;
    public String manufacturer;
    public String crew;
    public String length;
    public String craftClass;
    public String engines;
    public String productLine;
    public String role;
    public String armament;
    public String maxSpeed;

    @OneToMany
    private Collection<Recall> recalls;

    public Collection<Recall> getRecalls() {
        return recalls;
    }

    public void setRecalls(Collection<Recall> recalls) {
        this.recalls = recalls;
    }

    // Default constructor as required by JPA
    public Podracer() {
        // Leave everything at defaults
    }

    /**
     * A little utility getter with defaults
     * 
     * @param attrs
     * @param name
     * @param dv
     * @return
     */
    static String get(Map<String, String> attrs, String name, String dv) {
        if (attrs.containsKey(name)) {
            return attrs.get(name);
        } else {
            return dv;
        }
    }

    /**
     * Initialize from name/value pairs scraped from the webpage. Leave missing
     * attributes at default values
     * 
     * @param attributes
     *            - name/value
     */
    public Podracer(Map<String, String> attributes) {
        affiliation = get(attributes, "Affiliation", affiliation);
        passenger = get(attributes, "Passengers", passenger);
        era = get(attributes, "Era(s)", era);
        manufacturer = get(attributes, "Manufacturer", manufacturer);
        model = get(attributes, "Model", model);
        crew = get(attributes, "Crew", crew);
        length = get(attributes, "Length", length);
        craftClass = get(attributes, "Class", craftClass);
        engines = get(attributes, "Engine unit(s)", engines);
        productLine = get(attributes, "Product line", productLine);
        role = get(attributes, "Role(s)", role);
        armament = get(attributes, "Armament", armament);
        maxSpeed = get(attributes, "Maximum speed", maxSpeed);

        recalls = new ArrayList<Recall>();
        generateRecalls();
    }

    private void generateRecalls() {
        final String[] variants = {"SERVICE BRAKES, AIR:ANTILOCK:WHEEL SPEED SENSOR", 
                                   "STEERING:COLUMN",
                                   "ELECTRICAL SYSTEM:FUSES AND CIRCUIT BREAKERS",
                                   "AIR BAGS:FRONTAL:SENSOR/CONTROL MODULE",
                                   "FUEL SYSTEM, GASOLINE:DELIVERY:FUEL PUMP",
                                   "STRUCTURE:BODY:HATCHBACK/LIFTGATE",
                                   "STRUCTURE:BODY:HATCHBACK/LIFTGATE:SUPPORT DEVICE",
                                   "SERVICE BRAKES, HYDRAULIC:FOUNDATION COMPONENTS:MASTER CYLINDER",
                                  };
        Random rnd = new Random();
        int numRecalls = rnd.nextInt(8);
        while (numRecalls-- > 0)
        {
            String desc = variants[rnd.nextInt(variants.length)];
            recalls.add(new Recall(desc));
        }
    }
}
