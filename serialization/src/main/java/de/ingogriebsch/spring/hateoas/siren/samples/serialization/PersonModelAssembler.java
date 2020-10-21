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
package de.ingogriebsch.spring.hateoas.siren.samples.serialization;

import static org.springframework.hateoas.IanaLinkRelations.SELF;
import static org.springframework.hateoas.Links.MergeMode.REPLACE_BY_REL;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.afford;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;

/**
 * A {@link RepresentationModelAssembler} implementation that is responsible for converting domain objects of type {@link Person}
 * into corresponding {@link RepresentationModel}s.
 * <p>
 * HINT Implementing and using a RepresentationModelAssembler allows us to encapsulate the code that is responsible to create
 * representation-models based on the given domain objects.
 * 
 * @author i.griebsch
 */
@RequiredArgsConstructor
class PersonModelAssembler implements SimpleRepresentationModelAssembler<Person> {

    private static final Class<PersonController> controllerType = PersonController.class;

    @NonNull
    private final PagedResourcesAssembler<Person> pagedResourcesAssembler;

    public PagedModel<EntityModel<Person>> toPagedModel(@NonNull Page<Person> page) {
        PagedModel<EntityModel<Person>> model = pagedResourcesAssembler.toModel(page, this);
        addLinks(model);
        return model;
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<Person>> model) {
        Link selfLink = linkTo(methodOn(controllerType).search(null)).withSelfRel() //
            .andAffordance(afford(methodOn(controllerType).insert(null)));

        model.add(selfLink);
    }

    @Override
    public void addLinks(EntityModel<Person> model) {
        String id = model.getContent().getId();

        Link selfLink = linkTo(methodOn(controllerType).findOne(id)).withSelfRel() //
            .andAffordance(afford(methodOn(controllerType).update(id, null))) //
            .andAffordance(afford(methodOn(controllerType).delete(id)));

        model.add(selfLink);
    }

    private static void addLinks(PagedModel<EntityModel<Person>> model) {
        Link selfLink = model.getRequiredLink(SELF);
        selfLink = selfLink.andAffordance(afford(methodOn(controllerType).insert(null)));

        Links links = model.getLinks();
        links = links.merge(REPLACE_BY_REL, selfLink);

        model.removeLinks();
        model.add(links);
    }

}
