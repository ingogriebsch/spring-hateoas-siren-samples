package de.ingogriebsch.spring.hateoas.siren.samples.deserialization;

import static java.nio.charset.StandardCharsets.UTF_8;

import static de.ingogriebsch.spring.hateoas.siren.MediaTypes.SIREN_JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import de.ingogriebsch.spring.hateoas.siren.SirenMediaTypeConfiguration;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.client.MockRestServiceServer;

/**
 * HINT If you want to test your client through a test slice (@RestClientTest in this case) you need to explicitly import the
 * configuration of the Siren media-type. Otherwise the library is not included in the reduced application context and therefore
 * not active while requesting resources through the client.
 * If testing the code through @SpringBootTest it is not necessary to import the configuration explicitly because in this case the
 * whole application context is prepared in the same way as during the regular runtime of the application.
 *
 * @author i.griebsch
 */
@Import({ SirenMediaTypeConfiguration.class, HypermediaConfiguration.class, ClientConfiguration.class })
@RestClientTest(PersonClient.class)
class PersonClientTest {

    @Autowired
    private PersonClient personClient;

    @Autowired
    private MockRestServiceServer server;

    @Test
    void findAll_should_return_a_matching_response_if_no_resource_is_available() throws Exception {
        String content = jsonSource("findAll_should_return_a_matching_response_if_no_resource_is_available");
        server.expect(requestTo("http://localhost:8080/persons?sink=true")).andRespond(withSuccess(content, SIREN_JSON));

        assertThat(personClient.findAll()).isEmpty();
    }

    @Test
    void findAll_should_return_a_matching_response_if_some_resources_are_available() throws Exception {
        String content = jsonSource("findAll_should_return_a_matching_response_if_some_resources_are_available");
        server.expect(requestTo("http://localhost:8080/persons?sink=true")).andRespond(withSuccess(content, SIREN_JSON));

        List<Person> expected = newArrayList( //
            new Person("1", "Max Mustermann", 42, "max@mustermann.de"), //
            new Person("2", "Petra Poster", 21, "petra@poster.de") //
        );
        assertThat(personClient.findAll()).isEqualTo(expected);
    }

    @Test
    void findOne_should_return_a_matching_response_if_the_resource_is_not_available() throws Exception {
        server.expect(requestTo("http://localhost:8080/persons/1?sink=true")).andRespond(withStatus(NOT_FOUND));

        assertThat(personClient.findOne("1")).isEmpty();
    }

    @Test
    void findOne_should_return_a_matching_response_if_the_resource_is_available() throws Exception {
        String content = jsonSource("findOne_should_return_a_matching_response_if_the_resource_is_available");
        server.expect(requestTo("http://localhost:8080/persons/3?sink=true")).andRespond(withSuccess(content, SIREN_JSON));

        Person expected = new Person("3", "Max Mustermann", 42, "max@mustermann.de");
        assertThat(personClient.findOne("3")).get().isEqualTo(expected);
    }

    private static String jsonSource(String source) throws IOException {
        try (InputStream is = PersonClientTest.class.getResourceAsStream(source + ".json")) {
            return IOUtils.toString(is, UTF_8);
        }
    }
}
