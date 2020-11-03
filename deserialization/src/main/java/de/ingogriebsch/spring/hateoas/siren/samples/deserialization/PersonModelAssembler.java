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
package de.ingogriebsch.spring.hateoas.siren.samples.deserialization;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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

    @Override
    public void addLinks(CollectionModel<EntityModel<Person>> model) {
        model.add(linkTo(methodOn(PersonController.class).findAll(null)).withSelfRel());
    }

    @Override
    public void addLinks(EntityModel<Person> model) {
        model.add(linkTo(methodOn(PersonController.class).findOne(model.getContent().getId(), null)).withSelfRel());
    }
}
