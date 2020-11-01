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

import static com.google.common.collect.Lists.newArrayList;
import static lombok.AccessLevel.PRIVATE;

import java.util.Collection;
import java.util.List;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

/**
 * Utility handling {@link Page} related cases.
 *
 * @author i.griebsch
 */
@NoArgsConstructor(access = PRIVATE)
final class PageUtils {

    static <T> Page<T> toPage(Collection<T> objects, Pageable pageable) {
        int from = pageable.getPageNumber() * pageable.getPageSize();

        List<T> content = newArrayList();
        if (from < objects.size()) {
            int to = from + pageable.getPageSize();
            to = to > objects.size() ? objects.size() : to;
            content = newArrayList(objects).subList(from, to);
        }

        return new PageImpl<>(content, pageable, objects.size());
    }
}
