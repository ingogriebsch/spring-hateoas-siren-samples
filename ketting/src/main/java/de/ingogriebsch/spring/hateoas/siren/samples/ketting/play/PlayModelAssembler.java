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
package de.ingogriebsch.spring.hateoas.siren.samples.ketting.play;

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
import de.ingogriebsch.spring.hateoas.siren.samples.ketting.play.Play.Participant;
import de.ingogriebsch.spring.hateoas.siren.samples.ketting.player.PlayerLinkProvider;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

/**
 *
 *
 * @author Ingo Griebsch
 */
class PlayModelAssembler implements RepresentationModelAssembler<Play, RepresentationModel<?>> {

    @NonNull
    private final PagedResourcesAssembler<Play> pagedResourceAssembler;
    @NonNull
    private final ParticipantModelAssembler participantModelAssembler;

    PlayModelAssembler(@NonNull PagedResourcesAssembler<Play> pagedResourcesAssembler,
        @NonNull PlayerLinkProvider playerLinkProvider) {
        pagedResourceAssembler = pagedResourcesAssembler;
        participantModelAssembler = new ParticipantModelAssembler(playerLinkProvider);
    }

    @Override
    public RepresentationModel<?> toModel(Play source) {
        Long gameId = source.getGameId();

        Builder<Object, Object> pb = builder().put("date", source.getDate());
        ofNullable(source.getLocation()).map(l -> pb.put("location", l));
        ofNullable(source.getComment()).map(c -> pb.put("comment", c));
        Map<Object, Object> properties = pb.build();

        CollectionModel<RepresentationModel<?>> participants =
            participantModelAssembler.toCollectionModel(source.getPariticpants());

        Long id = source.getId();
        Link selfLink = linkTo(methodOn(PlayController.class).findOne(id)).withSelfRel() //
            .andAffordance(afford(methodOn(PlayController.class).update(id, null)))
            .andAffordance(afford(methodOn(PlayController.class).delete(id)));
        Links linksAndActions =
            Links.of(selfLink, Link.of(format("https://boardgamegeek.com/boardgame/%d", gameId), "boardgame"));

        return sirenModel() //
            .classes("play") //
            .properties(properties) //
            .entities(LinkRelation.of("participants"), participants) //
            .linksAndActions(linksAndActions) //
            .build();
    }

    RepresentationModel<?> toPagedModel(Page<Play> source) {
        PagedModel<RepresentationModel<?>> model = pagedResourceAssembler.toModel(source, this);
        Link selfLink = model.getRequiredLink(SELF).andAffordance(afford(methodOn(PlayController.class).create(null)));

        return sirenModel() //
            .classes("plays") //
            .properties(model.getMetadata()) //
            .entities(model.getContent()) //
            .linksAndActions(selfLink) //
            .build();
    }

    @RequiredArgsConstructor
    static class ParticipantModelAssembler implements RepresentationModelAssembler<Participant, RepresentationModel<?>> {

        @NonNull
        private final PlayerLinkProvider playerLinkProvider;

        @Override
        public RepresentationModel<?> toModel(Participant source) {
            Builder<Object, Object> pb = builder() //
                .put("firstTime", source.isFirstTime()) //
                .put("winner", source.isWinner());
            ofNullable(source.getColor()).map(c -> pb.put("color", c));
            ofNullable(source.getScore()).map(s -> pb.put("score", s));
            ofNullable(source.getStartPosition()).map(sp -> pb.put("startPosition", sp));
            Map<Object, Object> properties = pb.build();

            Links linksAndActions = Links.of(playerLinkProvider.findOne(LinkRelation.of("player"), source.getPlayerId()));

            return sirenModel() //
                .classes("participant") //
                .properties(properties) //
                .linksAndActions(linksAndActions) //
                .build();
        }
    }
}
