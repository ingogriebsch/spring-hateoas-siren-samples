package de.ingogriebsch.spring.hateoas.siren.samples.setup.boot;

import static java.util.stream.Collectors.toSet;

import static de.ingogriebsch.spring.hateoas.siren.MediaTypes.SIREN_JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import de.ingogriebsch.spring.hateoas.siren.SirenLinkDiscoverer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.hateoas.config.HypermediaMappingInformation;
import org.springframework.http.MediaType;

@SpringBootTest
class ApplicationTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void application_context_should_contain_siren_link_discoverer() {
        assertThat(context.getBean(SirenLinkDiscoverer.class)).isNotNull();
    }

    @Test
    void hypermedia_mapping_informations_should_contain_media_type_siren_json() {
        Map<String, HypermediaMappingInformation> mappingInformationBeans =
            context.getBeansOfType(HypermediaMappingInformation.class);
        Collection<HypermediaMappingInformation> mappingInformations = mappingInformationBeans.values();

        Set<MediaType> mediaTypes = mappingInformations.stream().flatMap(mi -> mi.getMediaTypes().stream()).collect(toSet());
        assertThat(mediaTypes).contains(SIREN_JSON);
    }

    @Test
    void hypermedia_mapping_informations_should_contain_jackson_2_siren_module() {
        Map<String, HypermediaMappingInformation> mappingInformationBeans =
            context.getBeansOfType(HypermediaMappingInformation.class);
        Collection<HypermediaMappingInformation> mappingInformations = mappingInformationBeans.values();

        Optional<Module> module = mappingInformations.stream().map(mi -> mi.getJacksonModule())
            .filter(m -> m.getModuleName().equals("siren-module")).findAny();

        assertThat(module).get().extracting("_version", type(Version.class))
            .hasFieldOrPropertyWithValue("_groupId", "de.ingogriebsch.hateoas")
            .hasFieldOrPropertyWithValue("_artifactId", "spring-hateoas-siren");
    }
}
