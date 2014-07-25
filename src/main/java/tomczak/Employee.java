package tomczak;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Employee {

	@Id @GeneratedValue int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
