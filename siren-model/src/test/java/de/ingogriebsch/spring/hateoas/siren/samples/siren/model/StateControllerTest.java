package de.ingogriebsch.spring.hateoas.siren.samples.siren.model;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import static com.google.common.collect.Lists.newArrayList;
import static de.ingogriebsch.spring.hateoas.siren.MediaTypes.SIREN_JSON;
import static de.ingogriebsch.spring.hateoas.siren.samples.siren.model.StateController.BASE_PATH;
import static de.ingogriebsch.spring.hateoas.siren.samples.siren.model.StateController.FIND_ONE_PATH;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.substringBefore;
import static org.apache.commons.lang3.StringUtils.uncapitalize;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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

@Import({ HypermediaConfiguration.class, SirenMediaTypeConfiguration.class })
@WebMvcTest(StateController.class)
class StateControllerTest {

    @MockBean
    private StateService stateService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void findAll_should_return_a_matching_response_if_no_resource_is_available() throws Exception {
        String id = "1";
        List<State> content = newArrayList();
        Pageable pageable = PageRequest.of(0, 20);
        doReturn(new PageImpl<>(content, pageable, content.size())).when(stateService).findAll(id, pageable);
        String responsePayload = jsonSource("findAll_should_return_a_matching_response_if_no_resource_is_available");

        ResultActions resultActions = mockMvc.perform(get(BASE_PATH, id) //
            .accept(SIREN_JSON) //
            .param("page", valueOf(pageable.getPageNumber())) //
            .param("size", valueOf(pageable.getPageSize())));

        resultActions.andExpect(status().isOk()) //
            .andExpect(content().contentType(SIREN_JSON)) //
            .andExpect(content().json(responsePayload, true));
    }

    @Test
    void findAll_should_return_a_matching_response_if_some_resources_are_available() throws Exception {
        String id = "1";
        List<State> content = newArrayList( //
            new State(id, "1", "New York", null), //
            new State(id, "2", "Texas", null) //
        );
        Pageable pageable = PageRequest.of(0, 20);
        doReturn(new PageImpl<>(content, pageable, content.size())).when(stateService).findAll(id, pageable);
        String responsePayload = jsonSource("findAll_should_return_a_matching_response_if_some_resources_are_available");

        ResultActions resultActions = mockMvc.perform(get(BASE_PATH, id) //
            .accept(SIREN_JSON));

        resultActions.andExpect(status().isOk()) //
            .andExpect(content().contentType(SIREN_JSON)) //
            .andExpect(content().json(responsePayload, true));
    }

    @Test
    void findOne_should_return_a_matching_response_if_the_resource_is_not_available() throws Exception {
        String id = "1";
        doReturn(empty()).when(stateService).findOne(id, id);

        ResultActions resultActions = mockMvc.perform(get(BASE_PATH + FIND_ONE_PATH, id, id) //
            .accept(SIREN_JSON));

        resultActions.andExpect(status().isNotFound()) //
            .andExpect(content().string(EMPTY));
    }

    @Test
    void findOne_should_return_a_matching_response_if_the_resource_is_available() throws Exception {
        String id = "1";
        doReturn(of(new State(id, id, "New York", new Capital(id, id, "New York")))).when(stateService).findOne(id, id);
        String content = jsonSource("findOne_should_return_a_matching_response_if_the_resource_is_available");

        ResultActions resultActions = mockMvc.perform(get(BASE_PATH + FIND_ONE_PATH, id, id) //
            .accept(SIREN_JSON));

        resultActions.andExpect(status().isOk()) //
            .andExpect(content().contentType(SIREN_JSON)) //
            .andExpect(content().json(content, true));
    }

    private static String jsonSource(String source) throws IOException {
        Class<?> clazz = StateControllerTest.class;
        String location = format("%s/%s.json", uncapitalize(substringBefore(clazz.getSimpleName(), "Test")), source);

        try (InputStream is = clazz.getResourceAsStream(location)) {
            return IOUtils.toString(is, UTF_8);
        }
    }
}
