package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * This class represents an entity counting the visits to the greetings page by all users.
 * In the corresponding table there will be exactly one entry, which will be updated each time a new visit occurs
 * (the value in the column "count" will be increased by 1)
 */

@Entity
public class ViewsCounter {

    @Id
    private int id;

    @Column
    private int count;

    public ViewsCounter(int initialValue){
        count = initialValue;
    }

    public ViewsCounter(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
