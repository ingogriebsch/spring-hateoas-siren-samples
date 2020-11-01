package de.ingogriebsch.spring.hateoas.siren.samples.siren.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

class CountryServiceTest {

    @Test
    void findAll_should_return_empty_page_if_no_objects_are_available() {
        Page<Country> page = new CountryService().findAll(PageRequest.of(0, 20));

        assertThat(page.isEmpty()).isTrue();
        assertThat(page.getContent()).isEmpty();
    }

    @Test
    void findAll_should_return_a_matching_page_if_some_objects_are_available() {
        List<Country> countries = newArrayList(new Country("1", "America", newArrayList()),
            new Country("2", "Europe", newArrayList()), new Country("3", "Asia", newArrayList()));

        CountryService countryService = new CountryService();
        countries.forEach(countryService::insert);

        Page<Country> page = countryService.findAll(PageRequest.of(0, 20));
        assertThat(page.isEmpty()).isFalse();
        assertThat(page.getContent()).containsExactlyInAnyOrderElementsOf(countries);
    }

    @Test
    void findOne_should_return_filled_optional_if_object_is_available() {
        Country country = new Country("1", "America", newArrayList());

        CountryService countryService = new CountryService();
        countryService.insert(country);

        assertThat(countryService.findOne(country.getId())).contains(country);
    }

    @Test
    void findOne_should_return_empty_optional_if_object_is_not_available() {
        assertThat(new CountryService().findOne("3")).isEmpty();
    }
}
