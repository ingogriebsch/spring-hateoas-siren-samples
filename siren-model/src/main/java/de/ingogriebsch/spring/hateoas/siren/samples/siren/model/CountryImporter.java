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

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 *
 *
 * @author i.griebsch
 */
@Component
@RequiredArgsConstructor
class CountryImporter implements CommandLineRunner {

    @NonNull
    private final CountryService countryService;

    @Override
    public void run(String... args) throws Exception {
        List<Country> countries = newArrayList( //
            new Country("1", "America", newArrayList( //
                new State("1", "1", "New York", new Capital("1", "1", "New York")))), //
            new Country("1", "America", newArrayList( //
                new State("1", "1", "New York", new Capital("1", "1", "New York")))));
        countries.forEach(countryService::insert);
    }
}
