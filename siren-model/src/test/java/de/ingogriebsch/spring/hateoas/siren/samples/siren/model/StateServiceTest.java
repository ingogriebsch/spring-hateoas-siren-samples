package de.ingogriebsch.spring.hateoas.siren.samples.siren.model;

import static java.util.Optional.of;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

class StateServiceTest {

    @Test
    void findAll_should_return_empty_page_if_no_objects_are_available() {
        String countryId = "1";

        CountryService countryService = mock(CountryService.class);
        doReturn(of(new Country(countryId, "America", newArrayList()))).when(countryService).findOne(countryId);

        Page<State> page = new StateService(countryService).findAll(countryId, PageRequest.of(0, 20));

        assertThat(page.isEmpty()).isTrue();
        assertThat(page.getContent()).isEmpty();
    }

    @Test
    void findAll_should_return_a_matching_page_if_some_objects_are_available() {
        String countryId = "1";
        List<State> states = newArrayList(new State(countryId, "1", "New York", null), new State(countryId, "2", "Texas", null));

        CountryService countryService = mock(CountryService.class);
        doReturn(of(new Country(countryId, "America", states))).when(countryService).findOne(countryId);

        Page<State> page = new StateService(countryService).findAll(countryId, PageRequest.of(0, 20));
        assertThat(page.isEmpty()).isEqualTo(false);

        assertThat(page.getContent()).containsExactlyInAnyOrderElementsOf(states);
    }

    @Test
    void findOne_should_return_filled_optional_if_object_is_available() {
        String countryId = "1";
        List<State> states = newArrayList(new State(countryId, "1", "New York", null), new State(countryId, "2", "Texas", null));

        CountryService countryService = mock(CountryService.class);
        doReturn(of(new Country(countryId, "America", states))).when(countryService).findOne(countryId);

        State expected = states.iterator().next();
        assertThat(new StateService(countryService).findOne(countryId, expected.getId())).contains(expected);
    }

    @Test
    void findOne_should_return_empty_optional_if_object_is_not_available() {
        String countryId = "1";
        List<State> states = newArrayList(new State(countryId, "1", "New York", null), new State(countryId, "2", "Texas", null));

        CountryService countryService = mock(CountryService.class);
        doReturn(of(new Country(countryId, "America", states))).when(countryService).findOne(countryId);

        assertThat(new StateService(countryService).findOne(countryId, "3")).isEmpty();
    }

}
