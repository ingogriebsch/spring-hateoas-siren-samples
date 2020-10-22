package de.ingogriebsch.spring.hateoas.siren.samples.internationalization;

import static java.lang.String.valueOf;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import static de.ingogriebsch.spring.hateoas.siren.MediaTypes.SIREN_JSON;
import static de.ingogriebsch.spring.hateoas.siren.samples.internationalization.PersonController.BASE_PATH;
import static de.ingogriebsch.spring.hateoas.siren.samples.internationalization.PersonController.DELETE_PATH;
import static de.ingogriebsch.spring.hateoas.siren.samples.internationalization.PersonController.FIND_ONE_PATH;
import static de.ingogriebsch.spring.hateoas.siren.samples.internationalization.PersonController.SEARCH_PATH;
import static de.ingogriebsch.spring.hateoas.siren.samples.internationalization.PersonController.UPDATE_PATH;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.ingogriebsch.spring.hateoas.siren.SirenMediaTypeConfiguration;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void findAll_should_return_a_matching_response_if_no_resource_is_available() throws Exception {
        List<Person> content = newArrayList();
        Pageable pageable = PageRequest.of(0, 20);
        doReturn(new PageImpl<>(content, pageable, content.size())).when(personService).findAll(pageable);
        String responsePayload = jsonSource("findAll_should_return_a_matching_response_if_no_resource_is_available");

        ResultActions resultActions = mockMvc.perform(get(BASE_PATH) //
            .accept(SIREN_JSON) //
            .param("page", valueOf(pageable.getPageNumber())) //
            .param("size", valueOf(pageable.getPageSize())));

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
        Pageable pageable = PageRequest.of(0, 20);
        doReturn(new PageImpl<>(content, pageable, content.size())).when(personService).findAll(pageable);
        String responsePayload = jsonSource("findAll_should_return_a_matching_response_if_some_resources_are_available");

        ResultActions resultActions = mockMvc.perform(get(BASE_PATH) //
            .accept(SIREN_JSON));

        resultActions.andExpect(status().isOk()) //
            .andExpect(content().contentType(SIREN_JSON)) //
            .andExpect(content().json(responsePayload, true));
    }

    @Test
    void findOne_should_return_a_matching_response_if_the_resource_is_not_available() throws Exception {
        String id = "3";
        doReturn(empty()).when(personService).findOne(id);

        ResultActions resultActions = mockMvc.perform(get(BASE_PATH + FIND_ONE_PATH, id) //
            .accept(SIREN_JSON));

        resultActions.andExpect(status().isNotFound()) //
            .andExpect(content().string(EMPTY));
    }

    @Test
    void findOne_should_return_a_matching_response_if_the_resource_is_available() throws Exception {
        String responsePayload = jsonSource("findOne_should_return_a_matching_response_if_the_resource_is_available");
        Person person = new Person(Integer.toString(3), "Max Musterman", 42, "max@mustermann.de");
        doReturn(Optional.of(person)).when(personService).findOne(person.getId());

        ResultActions resultActions = mockMvc.perform(get(BASE_PATH + FIND_ONE_PATH, person.getId()) //
            .accept(SIREN_JSON));

        resultActions.andExpect(status().isOk()) //
            .andExpect(content().contentType(SIREN_JSON)) //
            .andExpect(content().json(responsePayload, true));

    }

    @Test
    void search_should_return_a_matching_response_if_no_resource_is_found() throws Exception {
        String responsePayload = jsonSource("search_should_return_a_matching_response_if_no_resource_is_found");

        ResultActions resultActions = mockMvc.perform(get(BASE_PATH + SEARCH_PATH) //
            .accept(SIREN_JSON) //
            .param("namePattern", ".*"));

        resultActions.andExpect(status().isOk()) //
            .andExpect(content().contentType(SIREN_JSON)) //
            .andExpect(content().json(responsePayload, true));
    }

    @Test
    void search_should_return_a_matching_response_if_some_resources_are_found() throws Exception {
        String responsePayload = jsonSource("search_should_return_a_matching_response_if_some_resources_are_found");

        ResultActions resultActions = mockMvc.perform(get(BASE_PATH + SEARCH_PATH) //
            .accept(SIREN_JSON) //
            .param("namePattern", ".*"));

        resultActions.andExpect(status().isOk()) //
            .andExpect(content().contentType(SIREN_JSON)) //
            .andExpect(content().json(responsePayload, true));
    }

    @Test
    void insert_should_return_a_matching_response() throws Exception {
        PersonInput input = new PersonInput("Max Mustermann", 42, "max@mustermann.de");
        Person person = new Person("1", input.getName(), input.getAge(), input.getEmail());
        doReturn(person).when(personService).insert(input);

        ResultActions resultActions = mockMvc.perform(post(BASE_PATH) //
            .accept(SIREN_JSON) //
            .contentType(APPLICATION_JSON) //
            .content(objectMapper.writeValueAsString(input)));

        resultActions.andExpect(status().isCreated()) //
            .andExpect(content().string(EMPTY)) //
            .andExpect(header().string("location", "http://localhost/persons/" + person.getId()));
    }

    @Test
    void update_should_return_a_matching_response_if_the_resource_is_not_available() throws Exception {
        String id = "3";
        PersonInput input = new PersonInput("Max Mustermann", 42, "max@mustermann.de");
        doReturn(empty()).when(personService).update(id, input);

        ResultActions resultActions = mockMvc.perform(put(BASE_PATH + UPDATE_PATH, id) //
            .accept(SIREN_JSON) //
            .contentType(APPLICATION_JSON) //
            .content(objectMapper.writeValueAsString(input)));

        resultActions.andExpect(status().isNotFound()) //
            .andExpect(content().string(EMPTY));
    }

    @Test
    void update_should_return_a_matching_response_if_the_resource_is_available() throws JsonProcessingException, Exception {
        PersonInput input = new PersonInput("Max Mustermann", 42, "max@mustermann.de");
        Person person = new Person("3", input.getName(), input.getAge(), input.getEmail());
        doReturn(of(person)).when(personService).update(person.getId(), input);
        String responsePayload = jsonSource("update_should_return_a_matching_response_if_the_resource_is_available");

        ResultActions resultActions = mockMvc.perform(put(BASE_PATH + UPDATE_PATH, person.getId()) //
            .accept(SIREN_JSON) //
            .contentType(APPLICATION_JSON) //
            .content(objectMapper.writeValueAsString(input)));

        resultActions.andExpect(status().isOk()) //
            .andExpect(content().contentType(SIREN_JSON)) //
            .andExpect(content().json(responsePayload, true));
    }

    @Test
    void delete_should_return_a_matching_response_if_the_resource_is_not_available() throws Exception {
        String id = "3";
        doReturn(false).when(personService).delete(id);

        ResultActions resultActions = mockMvc.perform(delete(BASE_PATH + DELETE_PATH, id) //
            .accept(SIREN_JSON));

        resultActions.andExpect(status().isNotFound()) //
            .andExpect(content().string(EMPTY));
    }

    @Test
    void delete_should_return_a_matching_response_if_the_resource_is_available() throws Exception {
        String id = "3";
        doReturn(true).when(personService).delete(id);

        ResultActions resultActions = mockMvc.perform(delete(BASE_PATH + DELETE_PATH, id) //
            .accept(SIREN_JSON));

        resultActions.andExpect(status().isOk()) //
            .andExpect(content().string(EMPTY));
    }

    private static String jsonSource(String source) throws IOException {
        try (InputStream is = PersonControllerTest.class.getResourceAsStream(source + ".json")) {
            return IOUtils.toString(is, UTF_8);
        }
    }

}
