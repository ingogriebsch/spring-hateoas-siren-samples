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

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

import java.time.LocalDate;

import de.ingogriebsch.spring.hateoas.siren.samples.ketting.play.Play.Participant;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 *
 *
 * @author Ingo Griebsch
 */
@Component
@RequiredArgsConstructor
class PlayImporter implements CommandLineRunner {

    @NonNull
    private final PlayRepository playRepository;

    @Override
    public void run(String... args) throws Exception {
        newArrayList(new Play( //
            1234L, //
            LocalDate.now(), //
            "home", //
            null, //
            newHashSet(new Participant(2L, "green", 123, 1, true, false)) //
        )).forEach(playRepository::save);
    }

}
