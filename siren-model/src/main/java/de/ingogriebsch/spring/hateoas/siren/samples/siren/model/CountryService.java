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

import static java.util.Optional.ofNullable;

import static com.google.common.collect.Maps.newHashMap;
import static de.ingogriebsch.spring.hateoas.siren.samples.siren.model.PageUtils.toPage;

import java.util.Map;
import java.util.Optional;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * A {@link Service} implementation that is responsible for all use cases related to domain objects of type {@link Country}.
 *
 * @author i.griebsch
 */
@Service
class CountryService {

    private final Map<String, Country> countries = newHashMap();

    Optional<Country> findOne(@NonNull String id) {
        return ofNullable(countries.get(id));
    }

    Page<Country> findAll(@NonNull Pageable pageable) {
        return toPage(countries.values(), pageable);
    }

    Country insert(@NonNull Country country) {
        countries.put(country.getId(), country);
        return country;
    }
}
