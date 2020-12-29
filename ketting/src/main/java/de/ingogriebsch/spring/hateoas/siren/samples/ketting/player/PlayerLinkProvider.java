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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import lombok.NonNull;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;

/**
 *
 *
 * @author Ingo Griebsch
 */
public class PlayerLinkProvider {

    public Link findOne(@NonNull LinkRelation rel, @NonNull Long id) {
        return linkTo(methodOn(PlayerController.class).findOne(id)).withRel(rel);
    }

}
