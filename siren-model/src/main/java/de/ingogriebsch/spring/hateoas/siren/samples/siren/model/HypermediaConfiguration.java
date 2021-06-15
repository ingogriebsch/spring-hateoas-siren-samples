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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.config.EnableHypermediaSupport;

/**
 * A {@link Configuration} class that configures all Hypermedia related things.
 * <p>
 * HINT Declaring @EnableHypermediaSupport here is not strictly necessary because the hypermedia related auto-configuration
 * already defines it with some defaults. But defining it here gives you the chance to tweak and override the defaults the way you
 * prefer in your project.
 *
 * @author i.griebsch
 */
@Configuration
@EnableHypermediaSupport(type = {})
class HypermediaConfiguration {

    @Bean
    CapitalModelAssembler capitalModelAssembler() {
        return new CapitalModelAssembler();
    }

    @Bean
    StateModelAssembler stateModelAssembler(CapitalModelAssembler capitalModelAssembler,
        PagedResourcesAssembler<State> pagedResourcesAssembler) {
        return new StateModelAssembler(capitalModelAssembler, pagedResourcesAssembler);
    }

    @Bean
    CountryModelAssembler countryModelAssembler(StateModelAssembler stateModelAssembler,
        PagedResourcesAssembler<Country> pagedResourcesAssembler) {
        return new CountryModelAssembler(stateModelAssembler, pagedResourcesAssembler);
    }

}
