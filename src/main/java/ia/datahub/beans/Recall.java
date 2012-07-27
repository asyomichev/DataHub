package ia.datahub.beans;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Recall {

    // Offset IDs by epoch time to avoid ID conflicts when importing to ForceField
    private static int nextId = (int) (System.currentTimeMillis()/1000 - 1343407472);
    
    @Id
    public int id;

    public String description;

    public Recall() {
    }

    public Recall(String desc) {
        description = desc;
        id = nextId++;
    }

}
