package de.ingogriebsch.spring.hateoas.siren.samples.deserialization;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;

import org.junit.jupiter.api.Test;

class PersonServiceTest {

    @Test
    void findAll_should_return_empty_page_if_no_objects_are_available() {
        Iterable<Person> persons = new PersonService().findAll();
        assertThat(persons).isEmpty();
    }

    @Test
    void findAll_should_return_a_matching_page_if_some_objects_are_available() {
        List<Person> expected = newArrayList(new Person("1", "Max Mustermann", 42, "max@mustermann.de"),
            new Person("2", "Petra Poster", 21, "petra@poster.de"));

        PersonService service = new PersonService();
        expected.stream().forEach(service::insert);

        Iterable<Person> actual = service.findAll();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findOne_should_return_filled_optional_if_object_is_available() {
        PersonService service = new PersonService();
        Person person = service.insert(new Person("1", "Max Mustermann", 42, "max@mustermann.de"));

        assertThat(service.findOne(person.getId())).contains(person);
    }

    @Test
    void findOne_should_return_empty_optional_if_object_is_not_available() {
        PersonService service = new PersonService();
        assertThat(service.findOne("1")).isEmpty();
    }
}
