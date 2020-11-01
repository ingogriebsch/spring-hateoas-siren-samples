/*-
 * Copyright 2019-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.ingogriebsch.spring.hateoas.siren.samples.siren.model;

import static java.util.Collections.singletonMap;

import static de.ingogriebsch.spring.hateoas.siren.SirenModelBuilder.sirenModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Collection;

import de.ingogriebsch.spring.hateoas.siren.SirenModelBuilder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

/**
 * A {@link RepresentationModelAssembler} implementation that is responsible for converting domain objects of type {@link Country}
 * into corresponding {@link RepresentationModel}s.
 * <p>
 * HINT Implementing and using a RepresentationModelAssembler allows us to encapsulate the code that is responsible to create
 * representation-models based on the given domain objects.
 *
 * @author i.griebsch
 */
@RequiredArgsConstructor
class CountryModelAssembler implements RepresentationModelAssembler<Country, RepresentationModel<?>> {

    @NonNull
    private final StateModelAssembler stateModelAssembler;

    @NonNull
    private final PagedResourcesAssembler<Country> pagedResourcesAssembler;

    /**
     * HINT Here we are creating a respective representation-model (based on the given source) with the {@link SirenModelBuilder}.
     * This allows great flexibility in how to create the representation-model.
     */
    @Override
    public RepresentationModel<?> toModel(Country source) {
        Link selfLink = linkTo(methodOn(CountryController.class).findOne(source.getId())).withSelfRel();
        Collection<RepresentationModel<?>> states = stateModelAssembler.toCollectionModel(source.getStates()).getContent();

        return sirenModel().classes("country") //
            .title(source.getName()) //
            .properties(singletonMap("name", source.getName())) //
            .linksAndActions(selfLink) //
            .entities("state", states) //
            .build();
    }

    public PagedModel<RepresentationModel<?>> toPagedModel(@NonNull Page<Country> page) {
        return pagedResourcesAssembler.toModel(page, this);
    }
}
