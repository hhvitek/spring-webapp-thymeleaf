package app.spring_webapp_thymeleaf.controllers.vehicle;

import app.spring_webapp_thymeleaf.entities.PersonEntity;
import app.spring_webapp_thymeleaf.entities.VehicleEntity;
import app.spring_webapp_thymeleaf.repositories.VehicleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({MockitoExtension.class})
@SpringJUnitConfig(locations = {"classpath:spring/test-spring-config.xml"})
@WebAppConfiguration
class VehicleControllerTest {

    private static final String URL = "/vehicles";

    /**
     *     <bean id="vehicleRepository" class="org.mockito.Mockito" factory-method="mock">
     *         <constructor-arg value="app.spring_webapp_thymeleaf.repositories.VehicleRepository"/>
     *     </bean>
     */
    @Autowired
    private VehicleRepository vehicleRepository;
    private MockMvc mockMvc;

    private List<VehicleEntity> vehiclesDb = new ArrayList<>();

    @BeforeEach
    void setup(WebApplicationContext wac) {
        VehicleEntity first = new VehicleEntity();
        first.setId(1);
        first.setName("First name");
        first.setType("Type 1");

        VehicleEntity second = new VehicleEntity();
        second.setId(2);
        second.setName("Second name");
        second.setType("Type 2");

        vehiclesDb.add(first);
        vehiclesDb.add(second);

        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        Mockito.when(this.vehicleRepository.findAll())
                .thenReturn(vehiclesDb);

        Mockito.when(this.vehicleRepository.save(any(VehicleEntity.class)))
                .then(invocation ->  { // perform "update"
                    // if already in db
                    VehicleEntity vehicle = invocation.getArgument(0, VehicleEntity.class);
                    for (VehicleEntity entity: vehiclesDb) {
                        if (entity.getId() == vehicle.getId()) {
                            entity.setType(vehicle.getType());
                            entity.setName(vehicle.getName());
                            return entity;
                        }
                    }
                    vehiclesDb.add(vehicle);
                    return vehicle;
                });

        Mockito.when(this.vehicleRepository.existsById(1))
                .thenReturn(true);

        Mockito.when(this.vehicleRepository.findById(1))
                .thenReturn(Optional.of(first));

        Mockito.when(this.vehicleRepository.findById(2))
                .thenReturn(Optional.of(second));

        Mockito.when(this.vehicleRepository.findById(99))
                .thenReturn(Optional.empty());

        Mockito.when(this.vehicleRepository.existsById(99))
                .thenReturn(false);

        Mockito.doAnswer(invocationOnMock -> vehiclesDb.remove(1)).when(this.vehicleRepository).deleteById(1);
        Mockito.doAnswer(invocationOnMock -> vehiclesDb.remove(99)).when(this.vehicleRepository).deleteById(99);

        Page<VehicleEntity> vehiclePage = new PageImpl<>(vehiclesDb);
        Mockito.when(this.vehicleRepository.findAll(any(Pageable.class)))
                .thenReturn(vehiclePage);
    }

    @Test
    public void basicGetAll() throws Exception {
        mockMvc.perform(
                        get(URL)
                        .accept(MediaType.TEXT_HTML)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.parseMediaType("text/html;charset=UTF-8")))
                .andExpect(content().string(containsString("1")))
                .andExpect(content().string(containsString("First name")))
                .andExpect(content().string(containsString("Type 1")));
    }

    @Test
    public void basicAddNewShowOk() throws Exception {
        mockMvc.perform(
                get(URL + "/add")
                        .accept(MediaType.TEXT_HTML)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.parseMediaType("text/html;charset=UTF-8")))
                .andExpect(content().string(containsString("<label for=\"form-name\" class=\"form-label\"")));

    }

    @Test
    public void basicAddNewPostOkEntity() throws Exception {
        String urlEntityRepre = "?id=99&name=Name99&type=Type99";
        Assertions.assertEquals(2, vehiclesDb.size());

        mockMvc.perform(
                post(URL + "/add" + urlEntityRepre)
                        .accept(MediaType.TEXT_HTML)
        )
                .andExpect(status().is3xxRedirection());


        Assertions.assertEquals(3, vehiclesDb.size());
    }

    @Test
    public void addNewPostEntityEmptyName() throws Exception {
        String urlEntityRepre = "?id=99&type=Type99";
        Assertions.assertEquals(2, vehiclesDb.size());

        mockMvc.perform(
                post(URL + "/add" + urlEntityRepre)
                        .accept(MediaType.TEXT_HTML)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Please enter valid field.")));


        Assertions.assertEquals(2, vehiclesDb.size());
    }

    @Test
    public void basicAddNewPostEntityIdAlreadyInDb() throws Exception {
        String urlEntityRepre = "?id=1&name=Name99&type=Type99";
        Assertions.assertEquals(2, vehiclesDb.size());

        mockMvc.perform(
                post(URL + "/add" + urlEntityRepre)
                        .accept(MediaType.TEXT_HTML)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Id 1 already exist.")));


        Assertions.assertEquals(2, vehiclesDb.size());
    }

    @Test
    public void basicAddNewPostEntityIdAlreadyInDbCZ() throws Exception {
        String urlEntityRepre = "?id=1&name=Name99&type=Type99&lang=cs_CZ";
        Assertions.assertEquals(2, vehiclesDb.size());

        mockMvc.perform(
                post(URL + "/add" + urlEntityRepre)
                        .accept(MediaType.TEXT_HTML)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Id 1 již existuje.")));


        Assertions.assertEquals(2, vehiclesDb.size());
    }

    @Test
    public void basicEditShowOk() throws Exception {
        String urlEntityRepre = "?id=1";

        mockMvc.perform(
                get(URL + "/edit" + urlEntityRepre)
                        .accept(MediaType.TEXT_HTML)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.parseMediaType("text/html;charset=UTF-8")))
                .andExpect(content().string(containsString("<label for=\"form-name\" class=\"form-label\"")))
                .andExpect(content().string(containsString("1")))
                .andExpect(content().string(containsString("First name")))
                .andExpect(content().string(containsString("Type 1")));
    }

    @Test
    public void basicEditNewPostOkEntity() throws Exception {
        String urlEntityRepre = "?id=1&name=Name99&type=Type99";
        Assertions.assertEquals(2, vehiclesDb.size());

        mockMvc.perform(
                post(URL + "/edit" + urlEntityRepre)
                        .accept(MediaType.TEXT_HTML)
        )
                .andExpect(status().is3xxRedirection());


        Assertions.assertEquals(2, vehiclesDb.size());
    }

    @Test
    public void basicEditGetEntityIdNotInDb() throws Exception {
        String urlEntityRepre = "?id=99";
        Assertions.assertEquals(2, vehiclesDb.size());

        mockMvc.perform(
                get(URL + "/edit" + urlEntityRepre)
                        .accept(MediaType.TEXT_HTML)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Id 99 does not exist.")));


        Assertions.assertEquals(2, vehiclesDb.size());
    }

    @Test
    public void basicEditPostEntityIdNotInDb() throws Exception {
        String urlEntityRepre = "?id=99&name=99&type=99";
        Assertions.assertEquals(2, vehiclesDb.size());

        mockMvc.perform(
                post(URL + "/edit" + urlEntityRepre)
                        .accept(MediaType.TEXT_HTML)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Id 99 does not exist.")));


        Assertions.assertEquals(2, vehiclesDb.size());
    }

    @Test
    public void basicEditPostEntityNameEmptyDb() throws Exception {
        String urlEntityRepre = "?id=1&type=99";
        Assertions.assertEquals(2, vehiclesDb.size());

        mockMvc.perform(
                post(URL + "/edit" + urlEntityRepre)
                        .accept(MediaType.TEXT_HTML)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Please enter valid field.")));


        Assertions.assertEquals(2, vehiclesDb.size());
    }

    @Test
    public void removeTestOk() throws Exception {
        String urlEntityRepre = "?id=1";
        Assertions.assertEquals(2, vehiclesDb.size());

        mockMvc.perform(
                delete(URL + "/remove" + urlEntityRepre)
                .accept(MediaType.TEXT_HTML)
        ).andExpect(status().is3xxRedirection());

        Assertions.assertEquals(1, vehiclesDb.size());
    }

    @Test void remoteTestIdDoesNotExist() throws Exception {
        String urlEntityRepre = "?id=99";
        Assertions.assertEquals(2, vehiclesDb.size());

        mockMvc.perform(
                delete(URL + "/remove" + urlEntityRepre)
                        .accept(MediaType.TEXT_HTML)
        ).andExpect(status().is3xxRedirection());

        Assertions.assertEquals(2, vehiclesDb.size());
    }


}