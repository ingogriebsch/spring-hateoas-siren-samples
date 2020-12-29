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

import static java.util.Optional.empty;

import java.util.Optional;

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
class PlayerService {

    @NonNull
    private final PlayerRepository playerRepository;

    @Transactional(readOnly = true)
    public Page<Player> findAll(@NonNull Pageable pageable) {
        return playerRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Player> findOne(@NonNull Long id) {
        return playerRepository.findById(id);
    }

    @Transactional
    public Player create(@NonNull PlayerInput input) {
        return playerRepository.save(createPlayer(input));
    }

    @Transactional
    public Optional<Player> update(@NonNull Long id, @NonNull PlayerInput input) {
        Optional<Player> maybe = playerRepository.findById(id);
        if (!maybe.isPresent()) {
            return empty();
        }

        Player player = updatePlayer(maybe.get(), input);
        return Optional.of(player);
    }

    @Transactional
    public boolean delete(@NonNull Long id) {
        playerRepository.deleteById(id);
        return true;
    }

    private static Player createPlayer(PlayerInput input) {
        return new Player(input.getName(), input.getUsername());
    }

    private static Player updatePlayer(Player player, PlayerInput input) {
        String name = input.getName();
        if (name != null) {
            player.setName(name);
        }
        String username = input.getUsername();
        if (username != null) {
            player.setUsername(username);
        }
        return player;
    }
}
