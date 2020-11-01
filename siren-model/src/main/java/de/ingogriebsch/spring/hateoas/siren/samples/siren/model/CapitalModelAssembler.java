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

import static de.ingogriebsch.spring.hateoas.siren.SirenModelBuilder.sirenModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import de.ingogriebsch.spring.hateoas.siren.SirenModelBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

/**
 *
 *
 * @author i.griebsch
 */
@RequiredArgsConstructor
class CapitalModelAssembler implements RepresentationModelAssembler<Capital, RepresentationModel<?>> {

    /**
     * HINT Here we are creating a respective representation-model (based on the given source) with the {@link SirenModelBuilder}.
     * This allows great flexibility in how to create the representation-model.
     */
    @Override
    public RepresentationModel<?> toModel(Capital source) {
        Link selfLink =
            linkTo(methodOn(CapitalController.class).findOne(source.getCountryId(), source.getStateId())).withSelfRel();
        Link parentLink = linkTo(methodOn(StateController.class).findOne(source.getCountryId(), source.getStateId()))
            .withRel(LinkRelation.of("parent"));

        return sirenModel() //
            .classes("capital") //
            .title(source.getName()) //
            .properties(source) //
            .linksAndActions(selfLink, parentLink) //
            .build();
    }
}
