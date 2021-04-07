package app.spring_webapp_thymeleaf.controllers.driver;

import app.spring_webapp_thymeleaf.entities.DriverEntity;
import app.spring_webapp_thymeleaf.entities.PersonEntity;
import app.spring_webapp_thymeleaf.repositories.DriverRepository;
import app.spring_webapp_thymeleaf.repositories.PersonRepository;
import app.spring_webapp_thymeleaf.repositories.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith({MockitoExtension.class})
@SpringJUnitConfig(locations = {"classpath:spring/test-spring-config.xml"})
@WebAppConfiguration
class DriverControllerTest {
    private static final String URL = "/drivers";

    /**
     *     <bean id="personRepository" class="org.mockito.Mockito" factory-method="mock">
     *         <constructor-arg value="app.spring_webapp_thymeleaf.repositories.PersonRepository"/>
     *     </bean>
     */
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private PersonRepository personRepository;

    private MockMvc mockMvc;

    private List<DriverEntity> driverDb = new ArrayList<>();

    @BeforeEach
    void setup(WebApplicationContext wac) {
        DriverEntity first = new DriverEntity();
        first.setId(1);

        DriverEntity second = new DriverEntity();
        second.setId(2);

        driverDb.add(first);
        driverDb.add(second);

        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        Mockito.when(this.driverRepository.findAll())
                .thenReturn(driverDb);

        Mockito.when(this.driverRepository.save(any(DriverEntity.class)))
                .then(invocation ->  { // perform "update"
                    // if already in db
                    DriverEntity driver = invocation.getArgument(0, DriverEntity.class);
                    for (DriverEntity entity: driverDb) {
                        if (entity.getId() == driver.getId()) {
                            return entity;
                        }
                    }
                    driverDb.add(driver);
                    return driver;
                });

        Mockito.when(this.driverRepository.existsById(1))
                .thenReturn(true);

        Mockito.when(this.driverRepository.findById(1))
                .thenReturn(Optional.of(first));

        Mockito.when(this.driverRepository.findById(2))
                .thenReturn(Optional.of(second));

        Mockito.when(this.driverRepository.findById(99))
                .thenReturn(Optional.empty());

        Mockito.when(this.driverRepository.existsById(99))
                .thenReturn(false);

    }

    @Test
    public void addingDriverEntityWithoutExistingPersonFailsTest() throws Exception {
        String urlEntityRepre = "?personEntity.id=1&vehicleEntity.id=2";

        Mockito.when(this.personRepository.existsById(1))
                .thenReturn(false);

        Mockito.when(this.vehicleRepository.existsById(2))
                .thenReturn(true);

        mockMvc.perform(
                post(URL + "/add" + urlEntityRepre)
                .accept(MediaType.TEXT_HTML)
        ).andExpect(status().isOk())
                .andExpect(content().string(containsString("Id 1 does not exist.")));
    }
}