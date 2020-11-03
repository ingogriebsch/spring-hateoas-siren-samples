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

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

import java.util.Optional;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @NonNull
    private final PersonService personService;
    @NonNull
    private final PersonClient personClient;
    @NonNull
    private final PersonModelAssembler personModelAssembler;

    @GetMapping
    ResponseEntity<CollectionModel<EntityModel<Person>>> findAll(@RequestParam(defaultValue = "false") Boolean sink) {
        return ok(personModelAssembler.toCollectionModel(sink ? personService.findAll() : personClient.findAll()));
    }

    @GetMapping("/{id}")
    ResponseEntity<EntityModel<Person>> findOne(@PathVariable String id, @RequestParam(defaultValue = "false") Boolean sink) {
        Optional<Person> person = sink ? personService.findOne(id) : personClient.findOne(id);
        return person.map(p -> ok(personModelAssembler.toModel(p))).orElse(notFound().build());
    }
}
