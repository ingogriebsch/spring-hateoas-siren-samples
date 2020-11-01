package de.ingogriebsch.spring.hateoas.siren.samples.siren.model;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import static de.ingogriebsch.spring.hateoas.siren.MediaTypes.SIREN_JSON;
import static de.ingogriebsch.spring.hateoas.siren.samples.siren.model.CapitalController.BASE_PATH;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.substringBefore;
import static org.apache.commons.lang3.StringUtils.uncapitalize;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.InputStream;

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
@WebMvcTest(CapitalController.class)
class CapitalControllerTest {

    @MockBean
    private CapitalService capitalService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void findOne_should_return_a_matching_response_if_the_resource_is_not_available() throws Exception {
        String id = "3";
        doReturn(empty()).when(capitalService).findOne(id, id);

        ResultActions resultActions = mockMvc.perform(get(BASE_PATH, id, id) //
            .accept(SIREN_JSON));

        resultActions.andExpect(status().isNotFound()) //
            .andExpect(content().string(EMPTY));
    }

    @Test
    void findOne_should_return_a_matching_response_if_the_resource_is_available() throws Exception {
        String id = "1";
        doReturn(of(new Capital(id, id, "New York"))).when(capitalService).findOne(id, id);
        String content = jsonSource("findOne_should_return_a_matching_response_if_the_resource_is_available");

        ResultActions resultActions = mockMvc.perform(get(BASE_PATH, id, id) //
            .accept(SIREN_JSON));

        resultActions.andExpect(status().isOk()) //
            .andExpect(content().contentType(SIREN_JSON)) //
            .andExpect(content().json(content, true));
    }

    private static String jsonSource(String source) throws IOException {
        Class<?> clazz = CapitalControllerTest.class;
        String location = format("%s/%s.json", uncapitalize(substringBefore(clazz.getSimpleName(), "Test")), source);

        try (InputStream is = clazz.getResourceAsStream(location)) {
            return IOUtils.toString(is, UTF_8);
        }
    }
}
