package de.ingogriebsch.spring.hateoas.siren.samples.deserialization;

import static de.ingogriebsch.spring.hateoas.siren.MediaTypes.SIREN_JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.list;
import static org.assertj.core.groups.Tuple.tuple;
import static org.springframework.hateoas.IanaLinkRelations.SELF;
import static org.springframework.http.HttpMethod.GET;

import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Affordance;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

class PersonModelAssemblerTest {

    @Test
    void toModel_should_return_matching_model() {
        PersonModelAssembler assembler = new PersonModelAssembler();

        Person person = new Person("1", "Max Mustermann", 42, "max@mustermann.de");
        EntityModel<Person> model = assembler.toModel(person);

        assertThat(model.getContent()).isEqualTo(person);
        assertThat(model.getLinks()).extracting("rel").containsExactlyInAnyOrder(SELF);

        Optional<Link> selfLink = model.getLinks().getLink(SELF);

        assertThat(selfLink).get().hasFieldOrPropertyWithValue("href", "/persons/1{?sink}");

        assertThat(selfLink).get() //
            .extracting("affordances", list(Affordance.class)) //
            .extracting(a -> a.getAffordanceModel(SIREN_JSON)) //
            .extracting("name", "httpMethod") //
            .containsExactlyInAnyOrder(tuple("findOne", GET));
    }

    @Test
    void toCollectionModel_should_return_matching_model() {
        PersonModelAssembler assembler = new PersonModelAssembler();

        List<Person> persons = Lists.newArrayList(new Person("1", "Max Mustermann", 42, "max@mustermann.de"),
            new Person("2", "Petra Poster", 21, "petra@poster.de"));
        CollectionModel<EntityModel<Person>> model = assembler.toCollectionModel(persons);

        assertThat(model).hasSize(2);
        assertThat(model.getContent()).extracting("content").containsExactlyInAnyOrderElementsOf(persons);
        assertThat(model.getLinks()).extracting("rel").containsExactlyInAnyOrder(SELF);

        Optional<Link> selfLink = model.getLinks().getLink(SELF);
        assertThat(selfLink).get().hasFieldOrPropertyWithValue("href", "/persons{?sink}");

        assertThat(selfLink).get() //
            .extracting("affordances", list(Affordance.class)) //
            .extracting(a -> a.getAffordanceModel(SIREN_JSON)) //
            .extracting("name", "httpMethod") //
            .containsExactlyInAnyOrder(tuple("findAll", GET));
    }
}
