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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;

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
// FIXME It is necessary to configure at least one HypermediaType at the moment because of
// https://github.com/spring-projects/spring-hateoas/issues/1521
@EnableHypermediaSupport(type = { HypermediaType.HAL })
class HypermediaConfiguration {

    @Bean
    PersonModelAssembler personModelAssembler() {
        return new PersonModelAssembler();
    }
}
