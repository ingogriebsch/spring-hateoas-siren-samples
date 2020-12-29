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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * A domain object representing a user.
 *
 * @author Ingo Griebsch
 */
@Data
@Entity
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
class Player {

    @GeneratedValue
    @Id
    Long id;
    @Version
    Long revision;

    @Column(length = 1024, nullable = false, unique = true)
    String name;
    @Column(length = 1024, unique = true)
    String username;

    Player(@NonNull String name, String username) {
        this.name = name;
        this.username = username;
    }
}
