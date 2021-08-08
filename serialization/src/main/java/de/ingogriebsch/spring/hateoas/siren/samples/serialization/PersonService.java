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

import static java.lang.String.valueOf;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;

import static com.google.common.collect.Maps.newHashMap;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * A {@link Service} implementation that is responsible for all use cases related to domain objects of type {@link Person}.
 *
 * @author i.griebsch
 */
@Service
class PersonService {

    private final Map<String, Person> persons = newHashMap();

    Page<Person> findAll(@NonNull Pageable pageable) {
        return PageUtils.toPage(persons.values(), pageable);
    }

    Optional<Person> findOne(@NonNull String id) {
        return Optional.ofNullable(persons.get(id));
    }

    Iterable<Person> search(String namePattern) {
        return persons.values().stream().filter(p -> p.getName().matches(namePattern)).collect(toList());
    }

    boolean exists(@NonNull String id) {
        return persons.containsKey(id);
    }

    Person insert(@NonNull PersonInput input) {
        Person person = new Person(valueOf(persons.size() + 1), input.getName(), input.getEmail(), input.getBirthday());
        persons.put(person.getId(), person);
        return person;
    }

    Optional<Person> update(@NonNull String id, @NonNull PersonInput input) {
        Person person = persons.get(id);
        if (person == null) {
            return empty();
        }
        return of(update(person, input));
    }

    boolean delete(@NonNull String id) {
        return persons.remove(id) != null;
    }

    private static Person update(Person person, PersonInput input) {
        String name = input.getName();
        if (name != null) {
            person.setName(name);
        }
        String email = input.getEmail();
        if (email != null) {
            person.setEmail(email);
        }
        LocalDate birthday = input.getBirthday();
        if (birthday != null) {
            person.setBirthday(birthday);
        }
        return person;
    }

}
