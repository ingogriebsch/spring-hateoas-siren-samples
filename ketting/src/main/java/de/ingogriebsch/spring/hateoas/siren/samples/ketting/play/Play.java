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

import static javax.persistence.FetchType.EAGER;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * A domain object representing a play.
 *
 * @author Ingo Griebsch
 */
@Data
@Entity
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
class Play {

    @GeneratedValue
    @Id
    Long id;
    @Version
    Long revision;

    @Column(nullable = false)
    Long gameId;

    LocalDate date;
    @Column(length = 1024)
    String location;
    @Column(length = 16384)
    String comment;

    @ElementCollection(fetch = EAGER)
    Set<Participant> pariticpants;

    Play(@NonNull Long gameId, @NonNull LocalDate date, String location, String comment, @NonNull Set<Participant> pariticpants) {
        this.gameId = gameId;
        this.date = date;
        this.location = location;
        this.comment = comment;
        this.pariticpants = pariticpants;
    }

    @AllArgsConstructor
    @Data
    @Embeddable
    @NoArgsConstructor
    static class Participant {

        @Column(nullable = false)
        Long playerId;

        String color;
        Integer score;
        Integer startPosition;

        boolean winner;
        boolean firstTime;
    }
}
