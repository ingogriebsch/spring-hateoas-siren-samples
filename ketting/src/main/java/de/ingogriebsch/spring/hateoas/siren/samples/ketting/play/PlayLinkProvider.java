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

import static org.springframework.hateoas.UriTemplate.of;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.web.util.UriComponentsBuilder.fromUri;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.UriTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 *
 * @author Ingo Griebsch
 */
@RequiredArgsConstructor
public class PlayLinkProvider {

    @NonNull
    private final HateoasPageableHandlerMethodArgumentResolver pageableResolver;

    public Link findAll(@NonNull LinkRelation rel, Pageable pageable) {
        Link link = linkTo(methodOn(PlayController.class).findAll(pageable)).withRel(rel);

        if (pageable == null) {
            UriComponentsBuilder builder = fromUri(link.getTemplate().expand((Object) null));
            TemplateVariables templateVariables = pageableResolver.getPaginationTemplateVariables(null, builder.build());

            UriTemplate template = of(link.getHref()).with(templateVariables);
            link = Link.of(template.toString(), link.getRel());
        }

        return link;
    }
}
