package app.spring_webapp_thymeleaf.controllers.driver;

import app.spring_webapp_thymeleaf.entities.DriverEntity;
import app.spring_webapp_thymeleaf.repositories.PersonRepository;
import app.spring_webapp_thymeleaf.repositories.VehicleRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class DriverValidator implements Validator {
    private final PersonRepository personRepository;
    private final VehicleRepository vehicleRepository;

    public DriverValidator(PersonRepository personRepository, VehicleRepository vehicleRepository) {
        this.personRepository = personRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return DriverEntity.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        DriverEntity driver = (DriverEntity) o;


        if (driver.getPersonEntity() == null || driver.getPersonEntity().getId() == null) {
            errors.rejectValue(
                    "personEntity.id",
                    "form.entity.id.notexist",
                    new Object[]{("null")},
                    "Id does not exist."
            );
        } else

        if (!personRepository.existsById(driver.getPersonEntity().getId())) {
            errors.rejectValue(
                    "personEntity.id",
                    "form.entity.id.notexist",
                    new Object[]{(driver.getPersonEntity().getId())},
                    "Id does not exist."
            );
        }


        if (driver.getVehicleEntity() == null || driver.getVehicleEntity().getId() == null) {
            errors.rejectValue(
                    "vehicleEntity.id",
                    "form.entity.id.notexist",
                    new Object[]{("null")},
                    "Id does not exist."
            );
        } else

        if (!vehicleRepository.existsById(driver.getVehicleEntity().getId())) {
            errors.rejectValue(
                    "vehicleEntity.id",
                    "form.entity.id.notexist",
                    new Object[]{(driver.getVehicleEntity().getId())},
                    "Id does not exist."
            );
        }

        if (driver.getDateFrom() == null) {
            errors.rejectValue(
                    "dateFrom",
                    "form.entity.id.notexist",
                    new Object[]{"null"},
                    "DateFrom does not exist."
            );
        }

        if (driver.getDateTo() == null) {
            errors.rejectValue(
                    "dateTo",
                    "form.entity.id.notexist",
                    new Object[]{"null"},
                    "DateTo does not exist."
            );
        }

        if (driver.getDateFrom().isAfter(driver.getDateTo())) {
            errors.rejectValue(
                    "dateTo",
                    "form.entity.date.toIsBefore",
                    new Object[]{driver.getDateFrom(), driver.getDateTo()},
                    "To is Before From."
            );
        }

    }

}