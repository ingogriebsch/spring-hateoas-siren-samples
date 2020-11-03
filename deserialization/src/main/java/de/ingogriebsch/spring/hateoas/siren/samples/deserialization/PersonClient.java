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

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;

import static de.ingogriebsch.spring.hateoas.siren.MediaTypes.SIREN_JSON;

import java.net.URI;
import java.util.Optional;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * A client to perform HTTP requests against a remote API that provides Siren like hypermedia resources.
 * <p>
 * HINT Here you see that it is possible to interpret the representation models contained in the respective response. This is
 * possible because the given RestTemplate is configured in a way that it understands the respective hypermedia type.
 * 
 * @author i.griebsch
 */
@RequiredArgsConstructor
class PersonClient {

    private static final ParameterizedTypeReference<CollectionModel<EntityModel<Person>>> FIND_ALL_RESPONSE_TYPE =
        new ParameterizedTypeReference<CollectionModel<EntityModel<Person>>>() {
        };
    private static final ParameterizedTypeReference<EntityModel<Person>> FIND_ONE_RESPONSE_TYPE =
        new ParameterizedTypeReference<EntityModel<Person>>() {
        };

    @NonNull
    private final RestTemplate restTemplate;

    Iterable<Person> findAll() {
        URI requestUri = new DefaultUriBuilderFactory(baseUri()).builder() //
            .pathSegment("persons") //
            .queryParam("sink", true) //
            .build();

        RequestEntity<Void> requestEntity = RequestEntity.get(requestUri) //
            .accept(SIREN_JSON) //
            .build();

        ResponseEntity<CollectionModel<EntityModel<Person>>> response =
            restTemplate.exchange(requestEntity, FIND_ALL_RESPONSE_TYPE);

        return response.getBody().getContent().stream().map(em -> em.getContent()).collect(toList());
    }

    Optional<Person> findOne(@NonNull String id) {
        URI requestUri = new DefaultUriBuilderFactory(baseUri()).builder() //
            .pathSegment("persons") //
            .pathSegment("{id}") //
            .queryParam("sink", true) //
            .build(id);

        RequestEntity<Void> requestEntity = RequestEntity.get(requestUri) //
            .accept(SIREN_JSON) //
            .build();

        ResponseEntity<EntityModel<Person>> response;
        try {
            response = restTemplate.exchange(requestEntity, FIND_ONE_RESPONSE_TYPE);
        } catch (NotFound e) {
            return empty();
        }

        return of(response.getBody().getContent());
    }

    private static UriComponentsBuilder baseUri() {
        return UriComponentsBuilder.newInstance() //
            .scheme("http") //
            .host("localhost") //
            .port(8080);
    }
}
