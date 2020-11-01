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

import java.util.List;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * This importer is execute during the startup of the service to import some sample data that is then available through the REST
 * API.
 *
 * @author i.griebsch
 */
@Component
@RequiredArgsConstructor
class CountryImporter implements CommandLineRunner {

    @NonNull
    private final CountryService countryService;

    @Override
    public void run(String... args) throws Exception {
        List<Country> countries = newArrayList( //
            new Country("1", "Austria", newArrayList( //
                new State("1", "1", "Vienna", new Capital("1", "1", "Vienna")), //
                new State("1", "2", "Lower Austria", new Capital("1", "2", "Sankt Pölten")), //
                new State("1", "3", "Upper Austria", new Capital("1", "3", "Linz")), //
                new State("1", "4", "Styria", new Capital("1", "4", "Graz")), //
                new State("1", "5", "Tyrol", new Capital("1", "5", "Innsbruck")), //
                new State("1", "6", "Carinthia", new Capital("1", "6", "Klagenfurt")), //
                new State("1", "7", "Salzburg", new Capital("1", "7", "Salzburg")), //
                new State("1", "8", "Vorarlberg", new Capital("1", "8", "Bregenz")), //
                new State("1", "9", "Burgenland", new Capital("1", "9", "Eisenstadt")))), //

            new Country("2", "Belgian", newArrayList( //
                new State("2", "1", "Antwerp", new Capital("2", "1", "Antwerp")),
                new State("2", "2", "East Flanders", new Capital("2", "2", "Ghent")),
                new State("2", "3", "Flemish Brabant", new Capital("2", "3", "Leuven")),
                new State("2", "4", "Limburg", new Capital("2", "4", "  Hasselt")),
                new State("2", "5", "West Flanders", new Capital("2", "5", "Bruges")),
                new State("2", "6", "Hainaut", new Capital("2", "6", "Mons")),
                new State("2", "7", "Liège", new Capital("2", "7", "Liège")),
                new State("2", "8", "Luxembourg", new Capital("2", "8", "Arlon")),
                new State("2", "9", "Namur", new Capital("2", "9", "Namur")),
                new State("2", "10", "Walloon Brabant", new Capital("2", "10", "Wavre")),
                new State("2", "11", "Brussels Capital Region", new Capital("2", "11", "Brussels City")))), //

            new Country("3", "Germany", newArrayList( //
                new State("3", "1", "Baden-Württemberg", new Capital("3", "1", "Stuttgart")), //
                new State("3", "2", "Bavaria", new Capital("3", "2", "Munich")), //
                new State("3", "3", "Berlin", new Capital("3", "3", "Berlin")), //
                new State("3", "4", "Brandenburg", new Capital("3", "4", "Potsdam")), //
                new State("3", "5", "Bremen", new Capital("3", "5", "Bremen")), //
                new State("3", "6", "Hamburg", new Capital("3", "6", "Hamburg")), //
                new State("3", "7", "Hesse", new Capital("3", "7", "Wiesbaden")), //
                new State("3", "8", "Lower Saxony", new Capital("3", "8", "Hanover")), //
                new State("3", "9", "Mecklenburg-Vorpommern", new Capital("3", "9", "Schwerin")), //
                new State("3", "10", "North Rhine-Westphalia", new Capital("3", "10", "Düsseldorf")), //
                new State("3", "11", "Rhineland-Palatinate", new Capital("3", "11", "Mainz")), //
                new State("3", "12", "Saarland", new Capital("3", "12", "Saarbrücken")), //
                new State("3", "13", "Saxony", new Capital("3", "13", " Dresden")), //
                new State("3", "14", "Saxony-Anhalt", new Capital("3", "14", "Magdeburg")), //
                new State("3", "15", "Schleswig-Holstein", new Capital("3", "15", " Kiel")), //
                new State("3", "16", "Thuringia", new Capital("3", "16", "Erfurt")))));
        countries.forEach(countryService::insert);
    }
}
