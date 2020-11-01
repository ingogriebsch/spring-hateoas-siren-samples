package de.ingogriebsch.spring.hateoas.siren.samples.siren.model;

import static java.util.Optional.of;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class CapitalServiceTest {

    @Test
    void findOne_should_return_filled_optional_if_object_is_available() {
        State state = new State("1", "1", "New York", new Capital("1", "1", "New York"));

        StateService stateService = mock(StateService.class);
        doReturn(of(state)).when(stateService).findOne(state.getCountryId(), state.getId());

        assertThat(new CapitalService(stateService).findOne(state.getCountryId(), state.getId())).contains(state.getCapital());
    }

    @Test
    void findOne_should_return_empty_optional_if_object_is_not_available() {
        String id = "1";

        StateService stateService = mock(StateService.class);
        doReturn(Optional.empty()).when(stateService).findOne(id, id);

        assertThat(new CapitalService(stateService).findOne(id, id)).isEmpty();
    }
}
