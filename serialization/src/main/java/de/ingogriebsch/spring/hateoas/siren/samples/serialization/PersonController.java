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
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * A {@link RestController} implementation that controls the access to the available person resources.
 * <p>
 * HINT This controller returns different types of representation-models. This is all you need to do to produce a hypermedia like
 * output. If a client now asks for resources and expects Siren as content-type (means, sending 'application/vnd.siren+json' as
 * Accept header), the library converts the representation-models returned through the controller methods into Siren entities.
 * 
 * @author Ingo Griebsch
 */
@RequestMapping("/persons")
@RequiredArgsConstructor
@RestController
class PersonController {

    static final String BASE_PATH = "/persons";
    static final String FIND_ONE_PATH = "/{id}";
    static final String SEARCH_PATH = "/search";
    static final String UPDATE_PATH = "/{id}";
    static final String DELETE_PATH = "/{id}";

    @NonNull
    private final PersonService personService;
    @NonNull
    private final PersonModelAssembler personModelAssembler;

    @GetMapping
    ResponseEntity<PagedModel<EntityModel<Person>>> findAll(Pageable pageable) {
        return ok(personModelAssembler.toPagedModel(personService.findAll(pageable)));
    }

    @GetMapping("/{id}")
    ResponseEntity<EntityModel<Person>> findOne(@PathVariable String id) {
        return personService.findOne(id).map(p -> ok(personModelAssembler.toModel(p))).orElse(notFound().build());
    }

    @GetMapping("/search")
    ResponseEntity<CollectionModel<EntityModel<Person>>> search(@RequestParam String namePattern) {
        return ok(personModelAssembler.toCollectionModel(personService.search(namePattern)));
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    ResponseEntity<Void> insert(@RequestBody PersonInput input) {
        return created(personModelAssembler.toModel(personService.insert(input)).getRequiredLink(SELF).toUri()).build();
    }

    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE)
    ResponseEntity<EntityModel<Person>> update(@PathVariable String id, @RequestBody PersonInput input) {
        return personService.update(id, input).map(p -> ok(personModelAssembler.toModel(p))).orElse(notFound().build());
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable String id) {
        return personService.delete(id) ? ok().build() : notFound().build();
    }
}
