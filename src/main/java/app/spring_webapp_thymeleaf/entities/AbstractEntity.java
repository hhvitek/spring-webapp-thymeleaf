package app.spring_webapp_thymeleaf.entities;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Min;
import java.io.Serializable;

@Data
@MappedSuperclass
public class AbstractEntity implements Serializable {

    @Min(1)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
}