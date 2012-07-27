package ia.datahub.beans;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;
import javax.persistence.Id;

@Entity
public class Recall {

    static final int initialId = 500;
    
    @Id
    @SequenceGenerator(name="RECALL_SEQ", initialValue=initialId)
    @GeneratedValue(generator="RECALL_SEQ")
    public int id;

    public String description;

    public Recall() {
    }

    public Recall(String desc) {
        description = desc;
    }

}
