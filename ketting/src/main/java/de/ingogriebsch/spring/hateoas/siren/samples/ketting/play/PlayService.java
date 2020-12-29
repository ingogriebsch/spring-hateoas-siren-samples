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

import static java.util.stream.Collectors.toSet;

import java.util.Optional;

import de.ingogriebsch.spring.hateoas.siren.samples.ketting.play.Play.Participant;
import de.ingogriebsch.spring.hateoas.siren.samples.ketting.play.PlayInput.ParticipantInput;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 *
 * @author Ingo Griebsch
 */
@RequiredArgsConstructor
@Service
class PlayService {

    @NonNull
    private final PlayRepository playRepository;

    @Transactional(readOnly = true)
    public Page<Play> findAll(@NonNull Pageable pageable) {
        return playRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Play> findOne(@NonNull Long id) {
        return playRepository.findById(id);
    }

    @Transactional
    public Play create(@NonNull PlayInput input) {
        return playRepository.save(createPlay(input));
    }

    @Transactional
    public Optional<Play> update(@NonNull Long id, @NonNull PlayInput input) {
        Optional<Play> maybe = playRepository.findById(id);
        if (!maybe.isPresent()) {
            return Optional.empty();
        }

        Play play = updatePlay(maybe.get(), input);
        return Optional.of(play);
    }

    @Transactional
    public boolean delete(@NonNull Long id) {
        playRepository.deleteById(id);
        return true;
    }

    private Play createPlay(@NonNull PlayInput input) {
        return new Play( //
            input.getGameId(), //
            input.getDate(), //
            input.getLocation(), //
            input.getComment(), //
            input.getParticipants().stream().map(PlayService::createParticipant).collect(toSet()) //
        );
    }

    private static Participant createParticipant(ParticipantInput input) {
        return new Participant( //
            input.getPlayer(), //
            input.getColor(), //
            input.getScore(), //
            input.getStartPosition(), //
            input.isWinner(), //
            input.isFirstTime() //
        );
    }

    private static Play updatePlay(Play play, PlayInput input) {
        return play;
    }
}
