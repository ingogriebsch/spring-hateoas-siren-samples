package de.ingogriebsch.spring.hateoas.siren.samples.siren.model;

import static de.ingogriebsch.spring.hateoas.siren.MediaTypes.SIREN_JSON;
import static de.ingogriebsch.spring.hateoas.siren.samples.siren.model.CountryController.BASE_PATH;
import static de.ingogriebsch.spring.hateoas.siren.samples.siren.model.CountryController.FIND_ONE_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.list;
import static org.assertj.core.groups.Tuple.tuple;
import static org.assertj.core.util.Lists.newArrayList;
import static org.springframework.hateoas.IanaLinkRelations.SELF;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Affordance;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponents;

class CountryModelAssemblerTest {

    @Test
    void toModel_should_return_matching_model() {
        String id = "1";
        Country country = new Country(id, "America", newArrayList());

        UriComponents baseUri = fromPath(BASE_PATH).build();
        StateModelAssembler stateModelAssembler = new StateModelAssembler(new CapitalModelAssembler(),
            new PagedResourcesAssembler<>(new HateoasPageableHandlerMethodArgumentResolver(), baseUri));
        CountryModelAssembler assembler = new CountryModelAssembler(stateModelAssembler,
            new PagedResourcesAssembler<>(new HateoasPageableHandlerMethodArgumentResolver(), baseUri));

        RepresentationModel<?> model = assembler.toModel(country);

        assertThat(model.getLinks()).extracting("rel").containsExactlyInAnyOrder(SELF);

        Optional<Link> selfLink = model.getLinks().getLink(SELF);
        URI selfLinkHref = new DefaultUriBuilderFactory().builder() //
            .path(BASE_PATH) //
            .path(FIND_ONE_PATH) //
            .build(country.getId());
        assertThat(selfLink).get().hasFieldOrPropertyWithValue("href", selfLinkHref.toString());

        assertThat(selfLink).get() //
            .extracting("affordances", list(Affordance.class)) //
            .extracting(a -> a.getAffordanceModel(SIREN_JSON)) //
            .extracting("name", "httpMethod") //
            .containsExactlyInAnyOrder(tuple("findOne", GET));
    }

    @Test
    void toPagedModel_should_return_matching_model() {
        String id = "1";
        Pageable pageable = PageRequest.of(0, 20);
        List<Country> states =
            newArrayList(new Country(id, "America", newArrayList()), new Country("2", "America", newArrayList()));

        UriComponents baseUri = fromPath(BASE_PATH).build();
        StateModelAssembler stateModelAssembler = new StateModelAssembler(new CapitalModelAssembler(),
            new PagedResourcesAssembler<>(new HateoasPageableHandlerMethodArgumentResolver(), baseUri));
        CountryModelAssembler assembler = new CountryModelAssembler(stateModelAssembler,
            new PagedResourcesAssembler<>(new HateoasPageableHandlerMethodArgumentResolver(), baseUri));

        RepresentationModel<?> model = assembler.toPagedModel(PageUtils.toPage(states, pageable));

        assertThat(model.getLinks()).extracting("rel").containsExactlyInAnyOrder(SELF);

        Optional<Link> selfLink = model.getLinks().getLink(SELF);
        URI selfLinkHref = new DefaultUriBuilderFactory().builder() //
            .path(BASE_PATH) //
            .queryParam("page", pageable.getPageNumber()) //
            .queryParam("size", pageable.getPageSize()) //
            .build(id);
        assertThat(selfLink).get().hasFieldOrPropertyWithValue("href", selfLinkHref.toString());

        assertThat(selfLink).get().extracting("affordances", list(Affordance.class)).isEmpty();
    }

}
