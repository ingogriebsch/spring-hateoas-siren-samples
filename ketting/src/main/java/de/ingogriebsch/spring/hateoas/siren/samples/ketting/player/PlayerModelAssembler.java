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
package de.ingogriebsch.spring.hateoas.siren.samples.ketting.player;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

import static com.google.common.collect.ImmutableMap.builder;
import static de.ingogriebsch.spring.hateoas.siren.SirenModelBuilder.sirenModel;
import static org.springframework.hateoas.IanaLinkRelations.SELF;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.afford;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Map;

import com.google.common.collect.ImmutableMap.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

/**
 *
 *
 * @author Ingo Griebsch
 */
@RequiredArgsConstructor
class PlayerModelAssembler implements RepresentationModelAssembler<Player, RepresentationModel<?>> {

    @NonNull
    private final PagedResourcesAssembler<Player> pagedResourceAssembler;

    @Override
    public RepresentationModel<?> toModel(Player source) {
        Builder<Object, Object> pb = builder().put("name", source.getName());
        ofNullable(source.getUsername()).map(u -> pb.put("username", u));
        Map<Object, Object> properties = pb.build();

        Long id = source.getId();
        Link selfLink = linkTo(methodOn(PlayerController.class).findOne(id)).withSelfRel() //
            .andAffordance(afford(methodOn(PlayerController.class).update(id, null)))
            .andAffordance(afford(methodOn(PlayerController.class).delete(id)));

        Links linksAndActions = Links.of(selfLink);
        if (source.getUsername() != null) {
            linksAndActions =
                linksAndActions.and(Link.of(format("https://boardgamegeek.com/user/%s", source.getUsername()), "user"));
        }

        return sirenModel() //
            .classes("player") //
            .properties(properties) //
            .linksAndActions(linksAndActions) //
            .build();
    }

    RepresentationModel<?> toPagedModel(Page<Player> source) {
        PagedModel<RepresentationModel<?>> model = pagedResourceAssembler.toModel(source, this);
        Link selfLink = model.getRequiredLink(SELF).andAffordance(afford(methodOn(PlayerController.class).create(null)));

        return sirenModel() //
            .classes("players") //
            .properties(model.getMetadata()) //
            .entities(model.getContent()) //
            .linksAndActions(selfLink) //
            .build();
    }
}
