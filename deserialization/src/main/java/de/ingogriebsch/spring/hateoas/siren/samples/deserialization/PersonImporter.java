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

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * This importer is executed during the startup of the service and imports some sample data. This data is then available through
 * the REST API of this service.
 *
 * @author i.griebsch
 */
@Component
@RequiredArgsConstructor
class PersonImporter implements CommandLineRunner {

    @NonNull
    private final PersonService personService;

    @Override
    public void run(String... args) throws Exception {
        List<Person> persons = newArrayList( //
            new Person("1", "Max Mustermann", 42, "max@mustermann.de"), //
            new Person("2", "Petra Poster", 21, "petra@poster.de"), //
            new Person("3", "Mack the Knife", 92, "mack@knife.com") //
        );
        persons.forEach(personService::insert);
    }
}
