package de.ingogriebsch.spring.hateoas.siren.samples.deserialization;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.empty;

import static de.ingogriebsch.spring.hateoas.siren.MediaTypes.SIREN_JSON;
import static de.ingogriebsch.spring.hateoas.siren.samples.deserialization.PersonController.BASE_PATH;
import static de.ingogriebsch.spring.hateoas.siren.samples.deserialization.PersonController.FIND_ONE_PATH;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import de.ingogriebsch.spring.hateoas.siren.SirenMediaTypeConfiguration;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * HINT If you want to test your controller through a test slice (@WebMvcTest in this case) you need to explicitly import the
 * configuration of the Siren media-type. Otherwise the library is not included in the reduced application context and therefore
 * not active while requesting resources through the controller.
 * If testing the code through @SpringBootTest it is not necessary to import the configuration explicitly because in this case the
 * whole application context is prepared in the same way as during the regular runtime of the application.
 */
@Import({ HypermediaConfiguration.class, SirenMediaTypeConfiguration.class })
@WebMvcTest(PersonController.class)
class PersonControllerTest {

    @MockBean
    private PersonService personService;
    @MockBean
    private PersonClient personClient;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void findAll_should_return_a_matching_response_if_no_resource_is_available() throws Exception {
        List<Person> content = newArrayList();
        doReturn(content).when(personService).findAll();
        String responsePayload = jsonSource("findAll_should_return_a_matching_response_if_no_resource_is_available");

        ResultActions resultActions = mockMvc.perform(get(BASE_PATH) //
            .accept(SIREN_JSON) //
            .queryParam("sink", "true"));

        resultActions.andExpect(status().isOk()) //
            .andExpect(content().contentType(SIREN_JSON)) //
            .andExpect(content().json(responsePayload, true));
    }

    @Test
    void findAll_should_return_a_matching_response_if_some_resources_are_available() throws Exception {
        List<Person> content = newArrayList( //
            new Person("1", "Max Mustermann", 42, "max@mustermann.de"), //
            new Person("2", "Petra Poster", 21, "petra@poster.de") //
        );
        doReturn(content).when(personService).findAll();
        String responsePayload = jsonSource("findAll_should_return_a_matching_response_if_some_resources_are_available");

        ResultActions resultActions = mockMvc.perform(get(BASE_PATH) //
            .accept(SIREN_JSON) //
            .queryParam("sink", "true"));

        resultActions.andExpect(status().isOk()) //
            .andExpect(content().contentType(SIREN_JSON)) //
            .andExpect(content().json(responsePayload, true));
    }

    @Test
    void findOne_should_return_a_matching_response_if_the_resource_is_not_available() throws Exception {
        String id = "3";
        doReturn(empty()).when(personService).findOne(id);

        ResultActions resultActions = mockMvc.perform(get(BASE_PATH + FIND_ONE_PATH, id) //
            .accept(SIREN_JSON) //
            .queryParam("sink", "true"));

        resultActions.andExpect(status().isNotFound()) //
            .andExpect(content().string(EMPTY));
    }

    @Test
    void findOne_should_return_a_matching_response_if_the_resource_is_available() throws Exception {
        String responsePayload = jsonSource("findOne_should_return_a_matching_response_if_the_resource_is_available");
        Person person = new Person(Integer.toString(3), "Max Mustermann", 42, "max@mustermann.de");
        doReturn(Optional.of(person)).when(personService).findOne(person.getId());

        ResultActions resultActions = mockMvc.perform(get(BASE_PATH + FIND_ONE_PATH, person.getId()) //
            .accept(SIREN_JSON) //
            .queryParam("sink", "true"));

        resultActions.andExpect(status().isOk()) //
            .andExpect(content().contentType(SIREN_JSON)) //
            .andExpect(content().json(responsePayload, true));

    }

    private static String jsonSource(String source) throws IOException {
        try (InputStream is = PersonControllerTest.class.getResourceAsStream(source + ".json")) {
            return IOUtils.toString(is, UTF_8);
        }
    }
}
