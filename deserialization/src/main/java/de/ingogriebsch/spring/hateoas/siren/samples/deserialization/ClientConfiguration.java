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

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.HypermediaRestTemplateConfigurer;

/**
 * A {@link Configuration} class that configures all (remote) client related things.
 *
 * @author i.griebsch
 */
@Configuration
class ClientConfiguration {

    /**
     * HINT If you want to be able to deserialize different types of representation.models through a RestTemplate, you need to add
     * such a RestTemplateCustomizer to the application-context. This customizer is responsible for configuring the
     * RestTemplateBuilder (that is also available in the application-context) with the given hypermedia types.
     */
    @Bean
    RestTemplateCustomizer hypermediaRestTemplateCustomizer(HypermediaRestTemplateConfigurer configurer) {
        return restTemplate -> {
            configurer.registerHypermediaTypes(restTemplate);
        };
    }

    @Bean
    PersonClient personClient(RestTemplateBuilder restTemplateBuilder) {
        return new PersonClient(restTemplateBuilder.build());
    }

}
