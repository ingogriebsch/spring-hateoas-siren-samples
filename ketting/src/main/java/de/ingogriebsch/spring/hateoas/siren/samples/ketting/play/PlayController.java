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

import static org.springframework.hateoas.IanaLinkRelations.SELF;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

import java.net.URI;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 *
 * @author Ingo Griebsch
 */
@RequestMapping("/api/plays")
@RequiredArgsConstructor
@RestController
class PlayController {

    @NonNull
    private final PlayService playService;
    @NonNull
    private final PlayModelAssembler playModelAssembler;

    @GetMapping
    ResponseEntity<RepresentationModel<?>> findAll(Pageable pageable) {
        return ok(playModelAssembler.toPagedModel(playService.findAll(pageable)));
    }

    @GetMapping("/{id}")
    ResponseEntity<? extends RepresentationModel<?>> findOne(@PathVariable Long id) {
        return playService.findOne(id).map(p -> ok(playModelAssembler.toModel(p))).orElse(notFound().build());
    }

    @PostMapping
    ResponseEntity<Void> create(@RequestBody PlayInput input) {
        Play play = playService.create(input);
        URI location = playModelAssembler.toModel(play).getRequiredLink(SELF).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    ResponseEntity<? extends RepresentationModel<?>> update(@PathVariable Long id, @RequestBody PlayInput input) {
        return playService.update(id, input).map(p -> ok(playModelAssembler.toModel(p))).orElse(notFound().build());
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id) {
        return playService.delete(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

}
