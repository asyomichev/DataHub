package ia.datahub.beans;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Recall {

    @Id
    @GeneratedValue
    public int id;

    public String description;

    public Recall() {
    }

    public Recall(String desc) {
        description = desc;
    }

}
