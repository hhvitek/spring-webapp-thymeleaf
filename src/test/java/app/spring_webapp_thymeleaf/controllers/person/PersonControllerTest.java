package app.spring_webapp_thymeleaf.controllers.person;


import app.spring_webapp_thymeleaf.entities.PersonEntity;
import app.spring_webapp_thymeleaf.repositories.PersonRepository;
import org.junit.jupiter.api.Assertions;
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

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({MockitoExtension.class})
@SpringJUnitConfig(locations = {"classpath:spring/test-spring-config.xml"})
@WebAppConfiguration
class PersonControllerTest {
    private static final String URL = "/people";

    /**
     *     <bean id="personRepository" class="org.mockito.Mockito" factory-method="mock">
     *         <constructor-arg value="app.spring_webapp_thymeleaf.repositories.PersonRepository"/>
     *     </bean>
     */
    @Autowired
    private PersonRepository personRepository;
    private MockMvc mockMvc;

    private List<PersonEntity> personDb = new ArrayList<>();

    @BeforeEach
    void setup(WebApplicationContext wac) {
        PersonEntity first = new PersonEntity();
        first.setId(1);
        first.setName("First name");
        first.setCanDrive(true);

        PersonEntity second = new PersonEntity();
        second.setId(2);
        second.setName("Second name - cannot drive");
        second.setCanDrive(false);

        personDb.add(first);
        personDb.add(second);

        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        Mockito.when(this.personRepository.findAll())
                .thenReturn(personDb);

        Mockito.when(this.personRepository.save(any(PersonEntity.class)))
                .then(invocation ->  { // perform "update"
                    // if already in db
                    PersonEntity person = invocation.getArgument(0, PersonEntity.class);
                    for (PersonEntity entity: personDb) {
                        if (entity.getId().equals(person.getId())) {
                            entity.setCanDrive(person.getCanDrive());
                            entity.setName(person.getName());
                            return entity;
                        }
                    }
                    personDb.add(person);
                    return person;
                });

        Mockito.when(this.personRepository.existsById(1))
                .thenReturn(true);

        Mockito.when(this.personRepository.findById(1))
                .thenReturn(Optional.of(first));

        Mockito.when(this.personRepository.findById(2))
                .thenReturn(Optional.of(second));

        Mockito.when(this.personRepository.findById(99))
                .thenReturn(Optional.empty());

        Mockito.when(this.personRepository.existsById(99))
                .thenReturn(false);
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
                .andExpect(content().string(containsString("true")));
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
        String urlEntityRepre = "?id=99&name=Name99&CanDrive=true";
        Assertions.assertEquals(2, personDb.size());

        mockMvc.perform(
                post(URL + "/add" + urlEntityRepre)
                        .accept(MediaType.TEXT_HTML)
        )
                .andExpect(status().is3xxRedirection());


        Assertions.assertEquals(3, personDb.size());
    }

    @Test
    public void addNewPostEntityEmptyName() throws Exception {
        String urlEntityRepre = "?id=99&canDrive=true";
        Assertions.assertEquals(2, personDb.size());

        mockMvc.perform(
                post(URL + "/add" + urlEntityRepre)
                        .accept(MediaType.TEXT_HTML)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Please enter valid field.")));


        Assertions.assertEquals(2, personDb.size());
    }

    @Test
    public void basicAddNewPostEntityIdAlreadyInDb() throws Exception {
        String urlEntityRepre = "?id=1&name=Name99&canDrive=true";
        Assertions.assertEquals(2, personDb.size());

        mockMvc.perform(
                post(URL + "/add" + urlEntityRepre)
                        .accept(MediaType.TEXT_HTML)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Id 1 already exist.")));


        Assertions.assertEquals(2, personDb.size());
    }

    @Test
    public void basicAddNewPostEntityIdAlreadyInDbCZ() throws Exception {
        String urlEntityRepre = "?id=1&name=Name99&canDrive=true&lang=cs_CZ";
        Assertions.assertEquals(2, personDb.size());

        mockMvc.perform(
                post(URL + "/add" + urlEntityRepre)
                        .accept(MediaType.TEXT_HTML)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Id 1 již existuje.")));


        Assertions.assertEquals(2, personDb.size());
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
                .andExpect(content().string(containsString("true")));
    }

    @Test
    public void basicEditNewPostOkEntity() throws Exception {
        String urlEntityRepre = "?id=1&name=Name99&canDrive=true";
        Assertions.assertEquals(2, personDb.size());

        mockMvc.perform(
                post(URL + "/edit" + urlEntityRepre)
                        .accept(MediaType.TEXT_HTML)
        )
                .andExpect(status().is3xxRedirection());


        Assertions.assertEquals(2, personDb.size());
    }

    @Test
    public void basicEditGetEntityIdNotInDb() throws Exception {
        String urlEntityRepre = "?id=99";
        Assertions.assertEquals(2, personDb.size());

        mockMvc.perform(
                get(URL + "/edit" + urlEntityRepre)
                        .accept(MediaType.TEXT_HTML)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Id 99 does not exist.")));


        Assertions.assertEquals(2, personDb.size());
    }

    @Test
    public void basicEditPostEntityIdNotInDb() throws Exception {
        String urlEntityRepre = "?id=99&name=99&canDrive=false";
        Assertions.assertEquals(2, personDb.size());

        mockMvc.perform(
                post(URL + "/edit" + urlEntityRepre)
                        .accept(MediaType.TEXT_HTML)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Id 99 does not exist.")));


        Assertions.assertEquals(2, personDb.size());
    }

    @Test
    public void basicEditPostEntityNameEmptyDb() throws Exception {
        String urlEntityRepre = "?id=1&canDrive=false";
        Assertions.assertEquals(2, personDb.size());

        mockMvc.perform(
                post(URL + "/edit" + urlEntityRepre)
                        .accept(MediaType.TEXT_HTML)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Please enter valid field.")));


        Assertions.assertEquals(2, personDb.size());
    }

}