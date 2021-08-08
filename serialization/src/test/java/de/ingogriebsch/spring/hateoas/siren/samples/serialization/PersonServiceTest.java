package de.ingogriebsch.spring.hateoas.siren.samples.serialization;

import static java.util.stream.Collectors.toSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.assertj.core.util.Lists.newArrayList;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

class PersonServiceTest {

    @Test
    void findAll_should_return_empty_page_if_no_objects_are_available() {
        Page<Person> page = new PersonService().findAll(PageRequest.of(0, 20));
        assertThat(page.isEmpty()).isEqualTo(true);
        assertThat(page.getContent()).isEmpty();
    }

    @Test
    void findAll_should_return_a_matching_page_if_some_objects_are_available() {
        PersonService service = new PersonService();
        List<PersonInput> persons = newArrayList(new PersonInput("Max Mustermann", "max@mustermann.de", LocalDate.of(1970, 1, 1)),
            new PersonInput("Petra Poster", "petra@poster.de", LocalDate.of(1970, 1, 1)));
        persons.stream().forEach(service::insert);

        Page<Person> page = service.findAll(PageRequest.of(0, 20));
        assertThat(page.isEmpty()).isEqualTo(false);

        List<Person> content = page.getContent();
        assertThat(content).extracting("name", "email", "birthday").containsExactlyInAnyOrderElementsOf(
            persons.stream().map(i -> tuple(i.getName(), i.getEmail(), i.getBirthday())).collect(toSet()));
    }

    @Test
    void findOne_should_return_filled_optional_if_object_is_available() {
        PersonService service = new PersonService();
        Person person = service.insert(new PersonInput("Max Mustermann", "max@mustermann.de", LocalDate.of(1970, 1, 1)));

        assertThat(service.findOne(person.getId())).contains(person);
    }

    @Test
    void findOne_should_return_empty_optional_if_object_is_not_available() {
        PersonService service = new PersonService();
        Person person = service.insert(new PersonInput("Max Mustermann", "max@mustermann.de", LocalDate.of(1970, 1, 1)));

        assertThat(service.delete(person.getId())).isTrue();
        assertThat(service.findOne(person.getId())).isEmpty();
    }

    @Test
    void search_should_return_empty_iterable_if_no_matching_objects_are_available() {
        PersonService service = new PersonService();
        List<PersonInput> persons = newArrayList(new PersonInput("Max Mustermann", "max@mustermann.de", LocalDate.of(1970, 1, 1)),
            new PersonInput("Petra Poster", "petra@poster.de", LocalDate.of(1970, 1, 1)));
        persons.stream().forEach(service::insert);

        assertThat(service.search("Paul.*")).isEmpty();
    }

    @Test
    void search_should_return_matching_iterable_if_matching_objects_are_available() {
        PersonService service = new PersonService();
        List<PersonInput> persons = newArrayList(new PersonInput("Max Mustermann", "max@mustermann.de", LocalDate.of(1970, 1, 1)),
            new PersonInput("Petra Poster", "petra@poster.de", LocalDate.of(1970, 1, 1)));
        persons.stream().forEach(service::insert);

        assertThat(service.search("Max.*")).extracting("name", "email", "birthday")
            .containsExactlyInAnyOrder(tuple("Max Mustermann", "max@mustermann.de", LocalDate.of(1970, 1, 1)));
    }

    @Test
    void insert_should_add_and_return_new_instance() {
        PersonService service = new PersonService();
        Person person = service.insert(new PersonInput("Max Mustermann", "max@mustermann.de", LocalDate.of(1970, 1, 1)));

        assertThat(person).isNotNull();
        assertThat(service.exists(person.getId())).isTrue();
    }

    @Test
    void update_should_update_instance_and_return_filled_optional_if_object_is_available() {
        PersonInput insert = new PersonInput("Max Mustermann", "max@mustermann.de", LocalDate.of(1970, 1, 1));
        PersonService service = new PersonService();
        Person person = service.insert(insert);

        PersonInput update = new PersonInput(null, null, null);
        assertThat(service.update(person.getId(), update)).get() //
            .hasFieldOrPropertyWithValue("id", person.getId()) //
            .hasFieldOrPropertyWithValue("name", insert.getName()) //
            .hasFieldOrPropertyWithValue("email", insert.getEmail()) //
            .hasFieldOrPropertyWithValue("birthday", insert.getBirthday());
    }

    @Test
    void update_should_return_empty_optional_if_object_is_not_available() {
        assertThat(new PersonService().update("1", new PersonInput(null, null, null))).isEmpty();
    }

    @Test
    void delete_should_return_true_if_object_is_available() {
        PersonService service = new PersonService();
        Person person = service.insert(new PersonInput("Max Mustermann", "max@mustermann.de", LocalDate.of(1970, 1, 1)));

        assertThat(service.delete(person.getId())).isTrue();
    }

    @Test
    void delete_should_return_false_if_object_is_not_available() {
        PersonService service = new PersonService();
        Person person = service.insert(new PersonInput("Max Mustermann", "max@mustermann.de", LocalDate.of(1970, 1, 1)));

        assertThat(service.delete(person.getId())).isTrue();
        assertThat(service.delete(person.getId())).isFalse();
    }
}
