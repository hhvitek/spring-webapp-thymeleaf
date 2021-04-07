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
@Entity(name = "vehicle")
public class VehicleEntity extends AbstractEntity {
    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String type;

    @NotNull
    @NotBlank
    @Size(min = 2)
    @Column(nullable = false)
    private String name;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "vehicleEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DriverEntity> drivers = new ArrayList<>();

    public void addDriver(DriverEntity driver) {
        drivers.add(driver);
        driver.setVehicleEntity(this);
    }

}