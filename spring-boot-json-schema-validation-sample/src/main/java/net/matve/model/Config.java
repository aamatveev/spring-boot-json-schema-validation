package net.matve.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Entity
public class Config {

    @Id
    @GeneratedValue
    private long id;

    @NotEmpty(message = "Please provide a name")
    private String name;

    @NotNull(message = "Please provide a price")
    @DecimalMin(value = "1.00" )
    private BigDecimal price;

    public Config(){

    }

    public Config(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
