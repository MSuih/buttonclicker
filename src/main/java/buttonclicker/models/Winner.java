package buttonclicker.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import java.time.Instant;

@Entity
@JsonIgnoreProperties({"new", "id"})
public class Winner extends AbstractPersistable<Long> {
    private String name;
    private String price;
    private Instant time;

    public Winner(String name, String price, Instant time) {
        this.name = name;
        this.price = price;
        this.time = time;
    }

    public Winner() {
        //needed for hibernate
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }
}
