package app.spring_webapp_thymeleaf.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity(name = "person")
public class PersonEntity extends AbstractEntity {
    @NotNull
    @NotBlank
    @Size(min = 2)
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private Boolean canDrive = false;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "personEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DriverEntity> drivers = new ArrayList<>();

    public void addDriver(DriverEntity driver) {
        drivers.add(driver);
        driver.setPersonEntity(this);
    }
}