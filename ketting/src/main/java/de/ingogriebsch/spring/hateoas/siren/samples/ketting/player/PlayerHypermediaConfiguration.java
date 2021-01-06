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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PagedResourcesAssembler;

/**
 *
 * 
 * @author i.griebsch
 */
@Configuration
class PlayerHypermediaConfiguration {

    @Bean
    PlayerModelAssembler playerModelAssembler(PagedResourcesAssembler<Player> pagedResourceAssembler) {
        return new PlayerModelAssembler(pagedResourceAssembler);
    }

    @Bean
    PlayerLinkProvider playerLinkProvider(HateoasPageableHandlerMethodArgumentResolver pageableResolver) {
        return new PlayerLinkProvider(pageableResolver);
    }
}
