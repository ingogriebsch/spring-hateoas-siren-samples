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

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A {@link RestController} implementation that controls the access to the available state resources.
 * <p>
 * HINT This controller returns representation-models. This is all you need to do to produce a hypermedia like output. If a client
 * now asks for resources and expects Siren as content-type (means, sending 'application/vnd.siren+json' as Accept header), the
 * library converts the representation-models returned through the controller methods into Siren entities.
 * 
 * @author Ingo Griebsch
 */
@RequestMapping(StateController.BASE_PATH)
@RequiredArgsConstructor
@RestController
class StateController {

    static final String BASE_PATH = "/countries/{countryId}/states";
    static final String FIND_ONE_PATH = "/{id}";

    @NonNull
    private final StateService stateService;

    @NonNull
    private final StateModelAssembler stateModelAssembler;

    @GetMapping
    ResponseEntity<? extends RepresentationModel<?>> findAll(@PathVariable String countryId, Pageable pageable) {
        return ok(stateModelAssembler.toPagedModel(stateService.findAll(countryId, pageable)));
    }

    @GetMapping(FIND_ONE_PATH)
    ResponseEntity<? extends RepresentationModel<?>> findOne(@PathVariable String countryId, @PathVariable String id) {
        return stateService.findOne(countryId, id).map(c -> ok(stateModelAssembler.toModel(c))).orElse(notFound().build());
    }
}
