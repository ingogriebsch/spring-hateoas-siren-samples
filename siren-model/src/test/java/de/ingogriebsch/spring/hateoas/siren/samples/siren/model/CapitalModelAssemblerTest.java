package de.ingogriebsch.spring.hateoas.siren.samples.siren.model;

import static de.ingogriebsch.spring.hateoas.siren.MediaTypes.SIREN_JSON;
import static de.ingogriebsch.spring.hateoas.siren.samples.siren.model.CapitalController.BASE_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.list;
import static org.assertj.core.groups.Tuple.tuple;
import static org.springframework.hateoas.IanaLinkRelations.SELF;
import static org.springframework.http.HttpMethod.GET;

import java.net.URI;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Affordance;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.util.DefaultUriBuilderFactory;

class CapitalModelAssemblerTest {

    @Test
    void toModel_should_return_matching_model() {
        String id = "1";
        Capital capital = new Capital(id, id, "New York");

        CapitalModelAssembler assembler = new CapitalModelAssembler();
        RepresentationModel<?> model = assembler.toModel(capital);

        LinkRelation parentRel = LinkRelation.of("parent");
        assertThat(model.getLinks()).extracting("rel").containsExactly(SELF, parentRel);

        Optional<Link> selfLink = model.getLinks().getLink(SELF);
        URI selfLinkHref = new DefaultUriBuilderFactory().builder() //
            .path(BASE_PATH) //
            .build(capital.getCountryId(), capital.getStateId());
        assertThat(selfLink).get().hasFieldOrPropertyWithValue("href", selfLinkHref.toString());

        assertThat(selfLink).get() //
            .extracting("affordances", list(Affordance.class)) //
            .extracting(a -> a.getAffordanceModel(SIREN_JSON)) //
            .extracting("name", "httpMethod") //
            .containsExactlyInAnyOrder(tuple("findOne", GET));

        Optional<Link> parentLink = model.getLinks().getLink(parentRel);
        URI parentLinkHref = new DefaultUriBuilderFactory().builder() //
            .path(StateController.BASE_PATH + StateController.FIND_ONE_PATH) //
            .build(capital.getCountryId(), capital.getStateId());
        assertThat(parentLink).get().hasFieldOrPropertyWithValue("href", parentLinkHref.toString());

        assertThat(parentLink).get() //
            .extracting("affordances", list(Affordance.class)) //
            .extracting(a -> a.getAffordanceModel(SIREN_JSON)) //
            .extracting("name", "httpMethod") //
            .containsExactlyInAnyOrder(tuple("findOne", GET));
    }
}
