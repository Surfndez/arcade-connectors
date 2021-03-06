package com.arcadeanalytics.provider.rdbms.dataprovider;

/*-
 * #%L
 * Arcade Data
 * %%
 * Copyright (C) 2018 - 2019 ArcadeAnalytics
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.arcadeanalytics.provider.CytoData;
import com.arcadeanalytics.provider.DataSourceInfo;
import com.arcadeanalytics.provider.GraphData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class PostgreSQLDataProviderWithAggregationTest extends AbstractRDBMSProviderWithAggregationTest {

    private DataSourceInfo dataSource;

    @BeforeEach
    public void setUp() {

        final PostgreSQLContainer container = PostgreSQLContainerHolder.container;

        this.dataSource = new DataSourceInfo(
                1L,
                "RDBMS_POSTGRESQL",
                "testDataSource",
                "desc",
                container.getContainerIpAddress(),
                container.getFirstMappedPort(),
                container.getDatabaseName(),
                container.getUsername(),
                container.getPassword(),
                true,
                "{}",
                false,
                "",
                22,
                ""
        );


        provider = new RDBMSDataProvider();
    }


    @Override
    @Test
    public void fetchDataThroughTableScanTest() {

        try {
            provider.fetchData(dataSource, "select * from film_actor limit 5", 5);
            fail();
        } catch (Exception e) {
            String message = e.getMessage();
            assertTrue(message.contains("Wrong query content: the requested table was aggregated into"));
        }

    }

    @Override
    @Test
    public void expandN2NRelationship() {

        /**
         * Get movies by actor
         */

        // expanding 1-N relationship: actor -[film_actor]-> film
        String[] rootIds = {"1_1"};
        GraphData data = provider.expand(dataSource, rootIds, "out", "film_actor", 300);

        assertEquals(data.getNodesClasses().size(), 1);
        assertEquals(data.getNodes().size(), 19);
        assertEquals(data.getEdgesClasses().size(), 1);
        assertEquals(data.getEdges().size(), 19);

        // Node classes checks
        assertTrue(data.getNodesClasses().containsKey("film"));

        Map<String, Object> filmClass = data.getNodesClasses().get("film");
        assertTrue(filmClass.containsKey("film_id"));
        assertTrue(filmClass.containsKey("title"));
        assertTrue(filmClass.containsKey("description"));
        assertTrue(filmClass.containsKey("release_year"));
        assertTrue(filmClass.containsKey("language_id"));
        assertTrue(filmClass.containsKey("rental_duration"));
        assertTrue(filmClass.containsKey("rental_rate"));
        assertTrue(filmClass.containsKey("length"));
        assertTrue(filmClass.containsKey("replacement_cost"));
        assertTrue(filmClass.containsKey("rating"));
        assertTrue(filmClass.containsKey("last_update"));
        assertTrue(filmClass.containsKey("special_features"));
        assertTrue(filmClass.containsKey("fulltext"));

        // nodes checks
        Iterator<CytoData> it = data.getNodes().iterator();
        CytoData currNodeContent;
        Map<String, Object> currRecord;

        currNodeContent = it.next();
        assertNotNull(currNodeContent.getData());
        assertNotNull(currNodeContent.getData().getRecord());
        currRecord = currNodeContent.getData().getRecord();
        assertTrue(currRecord.containsKey("film_id"));
        assertEquals(currRecord.get("film_id"), 1);
        assertTrue(currRecord.containsKey("title"));
        assertEquals(currRecord.get("title"), "Academy Dinosaur");
        assertTrue(currRecord.containsKey("description"));
        assertNotNull(currRecord.get("description"));

        currNodeContent = it.next();
        assertNotNull(currNodeContent.getData());
        assertNotNull(currNodeContent.getData().getRecord());
        currRecord = currNodeContent.getData().getRecord();
        assertTrue(currRecord.containsKey("film_id"));
        assertEquals(currRecord.get("film_id"), 23);
        assertTrue(currRecord.containsKey("title"));
        assertEquals(currRecord.get("title"), "Anaconda Confessions");
        assertTrue(currRecord.containsKey("description"));
        assertNotNull(currRecord.get("description"));

        currNodeContent = it.next();
        assertNotNull(currNodeContent.getData());
        assertNotNull(currNodeContent.getData().getRecord());
        currRecord = currNodeContent.getData().getRecord();
        assertTrue(currRecord.containsKey("film_id"));
        assertEquals(currRecord.get("film_id"), 25);
        assertTrue(currRecord.containsKey("title"));
        assertEquals(currRecord.get("title"), "Angels Life");
        assertTrue(currRecord.containsKey("description"));
        assertNotNull(currRecord.get("description"));

        currNodeContent = it.next();
        assertNotNull(currNodeContent.getData());
        assertNotNull(currNodeContent.getData().getRecord());
        currRecord = currNodeContent.getData().getRecord();
        assertTrue(currRecord.containsKey("film_id"));
        assertEquals(currRecord.get("film_id"), 106);
        assertTrue(currRecord.containsKey("title"));
        assertEquals(currRecord.get("title"), "Bulworth Commandments");
        assertTrue(currRecord.containsKey("description"));
        assertNotNull(currRecord.get("description"));

        currNodeContent = it.next();
        assertNotNull(currNodeContent.getData());
        assertNotNull(currNodeContent.getData().getRecord());
        currRecord = currNodeContent.getData().getRecord();
        assertTrue(currRecord.containsKey("film_id"));
        assertEquals(currRecord.get("film_id"), 140);
        assertTrue(currRecord.containsKey("title"));
        assertEquals(currRecord.get("title"), "Cheaper Clyde");
        assertTrue(currRecord.containsKey("description"));
        assertNotNull(currRecord.get("description"));

        currNodeContent = it.next();
        assertNotNull(currNodeContent.getData());
        assertNotNull(currNodeContent.getData().getRecord());
        currRecord = currNodeContent.getData().getRecord();
        assertTrue(currRecord.containsKey("film_id"));
        assertEquals(currRecord.get("film_id"), 166);
        assertTrue(currRecord.containsKey("title"));
        assertEquals(currRecord.get("title"), "Color Philadelphia");
        assertTrue(currRecord.containsKey("description"));
        assertNotNull(currRecord.get("description"));

        currNodeContent = it.next();
        assertNotNull(currNodeContent.getData());
        assertNotNull(currNodeContent.getData().getRecord());
        currRecord = currNodeContent.getData().getRecord();
        assertTrue(currRecord.containsKey("film_id"));
        assertEquals(currRecord.get("film_id"), 277);
        assertTrue(currRecord.containsKey("title"));
        assertEquals(currRecord.get("title"), "Elephant Trojan");
        assertTrue(currRecord.containsKey("description"));
        assertNotNull(currRecord.get("description"));

        currNodeContent = it.next();
        assertNotNull(currNodeContent.getData());
        assertNotNull(currNodeContent.getData().getRecord());
        currRecord = currNodeContent.getData().getRecord();
        assertTrue(currRecord.containsKey("film_id"));
        assertEquals(currRecord.get("film_id"), 361);
        assertTrue(currRecord.containsKey("title"));
        assertEquals(currRecord.get("title"), "Gleaming Jawbreaker");
        assertTrue(currRecord.containsKey("description"));
        assertNotNull(currRecord.get("description"));

        currNodeContent = it.next();
        assertNotNull(currNodeContent.getData());
        assertNotNull(currNodeContent.getData().getRecord());
        currRecord = currNodeContent.getData().getRecord();
        assertTrue(currRecord.containsKey("film_id"));
        assertEquals(currRecord.get("film_id"), 438);
        assertTrue(currRecord.containsKey("title"));
        assertEquals(currRecord.get("title"), "Human Graffiti");
        assertTrue(currRecord.containsKey("description"));
        assertNotNull(currRecord.get("description"));

        currNodeContent = it.next();
        assertNotNull(currNodeContent.getData());
        assertNotNull(currNodeContent.getData().getRecord());
        currRecord = currNodeContent.getData().getRecord();
        assertTrue(currRecord.containsKey("film_id"));
        assertEquals(currRecord.get("film_id"), 499);
        assertTrue(currRecord.containsKey("title"));
        assertEquals(currRecord.get("title"), "King Evolution");
        assertTrue(currRecord.containsKey("description"));
        assertNotNull(currRecord.get("description"));

        currNodeContent = it.next();
        assertNotNull(currNodeContent.getData());
        assertNotNull(currNodeContent.getData().getRecord());
        currRecord = currNodeContent.getData().getRecord();
        assertTrue(currRecord.containsKey("film_id"));
        assertEquals(currRecord.get("film_id"), 506);
        assertTrue(currRecord.containsKey("title"));
        assertEquals(currRecord.get("title"), "Lady Stage");
        assertTrue(currRecord.containsKey("description"));
        assertNotNull(currRecord.get("description"));

        currNodeContent = it.next();
        assertNotNull(currNodeContent.getData());
        assertNotNull(currNodeContent.getData().getRecord());
        currRecord = currNodeContent.getData().getRecord();
        assertTrue(currRecord.containsKey("film_id"));
        assertEquals(currRecord.get("film_id"), 509);
        assertTrue(currRecord.containsKey("title"));
        assertEquals(currRecord.get("title"), "Language Cowboy");
        assertTrue(currRecord.containsKey("description"));
        assertNotNull(currRecord.get("description"));

        currNodeContent = it.next();
        assertNotNull(currNodeContent.getData());
        assertNotNull(currNodeContent.getData().getRecord());
        currRecord = currNodeContent.getData().getRecord();
        assertTrue(currRecord.containsKey("film_id"));
        assertEquals(currRecord.get("film_id"), 605);
        assertTrue(currRecord.containsKey("title"));
        assertEquals(currRecord.get("title"), "Mulholland Beast");
        assertTrue(currRecord.containsKey("description"));
        assertNotNull(currRecord.get("description"));

        currNodeContent = it.next();
        assertNotNull(currNodeContent.getData());
        assertNotNull(currNodeContent.getData().getRecord());
        currRecord = currNodeContent.getData().getRecord();
        assertTrue(currRecord.containsKey("film_id"));
        assertEquals(currRecord.get("film_id"), 635);
        assertTrue(currRecord.containsKey("title"));
        assertEquals(currRecord.get("title"), "Oklahoma Jumanji");
        assertTrue(currRecord.containsKey("description"));
        assertNotNull(currRecord.get("description"));

        currNodeContent = it.next();
        assertNotNull(currNodeContent.getData());
        assertNotNull(currNodeContent.getData().getRecord());
        currRecord = currNodeContent.getData().getRecord();
        assertTrue(currRecord.containsKey("film_id"));
        assertEquals(currRecord.get("film_id"), 749);
        assertTrue(currRecord.containsKey("title"));
        assertEquals(currRecord.get("title"), "Rules Human");
        assertTrue(currRecord.containsKey("description"));
        assertNotNull(currRecord.get("description"));

        currNodeContent = it.next();
        assertNotNull(currNodeContent.getData());
        assertNotNull(currNodeContent.getData().getRecord());
        currRecord = currNodeContent.getData().getRecord();
        assertTrue(currRecord.containsKey("film_id"));
        assertEquals(currRecord.get("film_id"), 832);
        assertTrue(currRecord.containsKey("title"));
        assertEquals(currRecord.get("title"), "Splash Gump");
        assertTrue(currRecord.containsKey("description"));
        assertNotNull(currRecord.get("description"));

        currNodeContent = it.next();
        assertNotNull(currNodeContent.getData());
        assertNotNull(currNodeContent.getData().getRecord());
        currRecord = currNodeContent.getData().getRecord();
        assertTrue(currRecord.containsKey("film_id"));
        assertEquals(currRecord.get("film_id"), 939);
        assertTrue(currRecord.containsKey("title"));
        assertEquals(currRecord.get("title"), "Vertigo Northwest");
        assertTrue(currRecord.containsKey("description"));
        assertNotNull(currRecord.get("description"));

        currNodeContent = it.next();
        assertNotNull(currNodeContent.getData());
        assertNotNull(currNodeContent.getData().getRecord());
        currRecord = currNodeContent.getData().getRecord();
        assertTrue(currRecord.containsKey("film_id"));
        assertEquals(currRecord.get("film_id"), 970);
        assertTrue(currRecord.containsKey("title"));
        assertEquals(currRecord.get("title"), "Westward Seabiscuit");
        assertTrue(currRecord.containsKey("description"));
        assertNotNull(currRecord.get("description"));

        currNodeContent = it.next();
        assertNotNull(currNodeContent.getData());
        assertNotNull(currNodeContent.getData().getRecord());
        currRecord = currNodeContent.getData().getRecord();
        assertTrue(currRecord.containsKey("film_id"));
        assertEquals(currRecord.get("film_id"), 980);
        assertTrue(currRecord.containsKey("title"));
        assertEquals(currRecord.get("title"), "Wizard Coldblooded");
        assertTrue(currRecord.containsKey("description"));
        assertNotNull(currRecord.get("description"));

        // edges checks
        it = data.getEdges().iterator();
        CytoData currEdgeContent;

        currEdgeContent = it.next();
        assertNotNull(currEdgeContent.getData());
        assertNotNull(currEdgeContent.getData().getRecord());
        currRecord = currEdgeContent.getData().getRecord();
        assertEquals(currRecord.size(), 1);
        assertTrue(currRecord.containsKey("last_update"));
        assertEquals(currEdgeContent.getClasses(), "film_actor");
        assertEquals(currEdgeContent.getGroup(), "edges");
        assertEquals(currEdgeContent.getData().getSource(), "1_1");
        assertEquals(currEdgeContent.getData().getTarget(), "7_1");
        assertEquals(currEdgeContent.getData().getId(), "8_1_1");

        currEdgeContent = it.next();
        assertNotNull(currEdgeContent.getData());
        assertNotNull(currEdgeContent.getData().getRecord());
        currRecord = currEdgeContent.getData().getRecord();
        assertEquals(currRecord.size(), 1);
        assertTrue(currRecord.containsKey("last_update"));
        assertEquals(currEdgeContent.getClasses(), "film_actor");
        assertEquals(currEdgeContent.getGroup(), "edges");
        assertEquals(currEdgeContent.getData().getSource(), "1_1");
        assertEquals(currEdgeContent.getData().getTarget(), "7_23");
        assertEquals(currEdgeContent.getData().getId(), "8_1_23");

        currEdgeContent = it.next();
        assertNotNull(currEdgeContent.getData());
        assertNotNull(currEdgeContent.getData().getRecord());
        currRecord = currEdgeContent.getData().getRecord();
        assertEquals(currRecord.size(), 1);
        assertTrue(currRecord.containsKey("last_update"));
        assertEquals(currEdgeContent.getClasses(), "film_actor");
        assertEquals(currEdgeContent.getGroup(), "edges");
        assertEquals(currEdgeContent.getData().getSource(), "1_1");
        assertEquals(currEdgeContent.getData().getTarget(), "7_25");
        assertEquals(currEdgeContent.getData().getId(), "8_1_25");

        currEdgeContent = it.next();
        assertNotNull(currEdgeContent.getData());
        assertNotNull(currEdgeContent.getData().getRecord());
        currRecord = currEdgeContent.getData().getRecord();
        assertEquals(currRecord.size(), 1);
        assertTrue(currRecord.containsKey("last_update"));
        assertEquals(currEdgeContent.getClasses(), "film_actor");
        assertEquals(currEdgeContent.getGroup(), "edges");
        assertEquals(currEdgeContent.getData().getSource(), "1_1");
        assertEquals(currEdgeContent.getData().getTarget(), "7_106");
        assertEquals(currEdgeContent.getData().getId(), "8_1_106");

        currEdgeContent = it.next();
        assertNotNull(currEdgeContent.getData());
        assertNotNull(currEdgeContent.getData().getRecord());
        currRecord = currEdgeContent.getData().getRecord();
        assertEquals(currRecord.size(), 1);
        assertTrue(currRecord.containsKey("last_update"));
        assertEquals(currEdgeContent.getClasses(), "film_actor");
        assertEquals(currEdgeContent.getGroup(), "edges");
        assertEquals(currEdgeContent.getData().getSource(), "1_1");
        assertEquals(currEdgeContent.getData().getTarget(), "7_140");
        assertEquals(currEdgeContent.getData().getId(), "8_1_140");

        currEdgeContent = it.next();
        assertNotNull(currEdgeContent.getData());
        assertNotNull(currEdgeContent.getData().getRecord());
        currRecord = currEdgeContent.getData().getRecord();
        assertEquals(currRecord.size(), 1);
        assertTrue(currRecord.containsKey("last_update"));
        assertEquals(currEdgeContent.getClasses(), "film_actor");
        assertEquals(currEdgeContent.getGroup(), "edges");
        assertEquals(currEdgeContent.getData().getSource(), "1_1");
        assertEquals(currEdgeContent.getData().getTarget(), "7_166");
        assertEquals(currEdgeContent.getData().getId(), "8_1_166");

        currEdgeContent = it.next();
        assertNotNull(currEdgeContent.getData());
        assertNotNull(currEdgeContent.getData().getRecord());
        currRecord = currEdgeContent.getData().getRecord();
        assertEquals(currRecord.size(), 1);
        assertTrue(currRecord.containsKey("last_update"));
        assertEquals(currEdgeContent.getClasses(), "film_actor");
        assertEquals(currEdgeContent.getGroup(), "edges");
        assertEquals(currEdgeContent.getData().getSource(), "1_1");
        assertEquals(currEdgeContent.getData().getTarget(), "7_277");
        assertEquals(currEdgeContent.getData().getId(), "8_1_277");

        currEdgeContent = it.next();
        assertNotNull(currEdgeContent.getData());
        assertNotNull(currEdgeContent.getData().getRecord());
        currRecord = currEdgeContent.getData().getRecord();
        assertEquals(currRecord.size(), 1);
        assertTrue(currRecord.containsKey("last_update"));
        assertEquals(currEdgeContent.getClasses(), "film_actor");
        assertEquals(currEdgeContent.getGroup(), "edges");
        assertEquals(currEdgeContent.getData().getSource(), "1_1");
        assertEquals(currEdgeContent.getData().getTarget(), "7_361");
        assertEquals(currEdgeContent.getData().getId(), "8_1_361");

        currEdgeContent = it.next();
        assertNotNull(currEdgeContent.getData());
        assertNotNull(currEdgeContent.getData().getRecord());
        currRecord = currEdgeContent.getData().getRecord();
        assertEquals(currRecord.size(), 1);
        assertTrue(currRecord.containsKey("last_update"));
        assertEquals(currEdgeContent.getClasses(), "film_actor");
        assertEquals(currEdgeContent.getGroup(), "edges");
        assertEquals(currEdgeContent.getData().getSource(), "1_1");
        assertEquals(currEdgeContent.getData().getTarget(), "7_438");
        assertEquals(currEdgeContent.getData().getId(), "8_1_438");

        currEdgeContent = it.next();
        assertNotNull(currEdgeContent.getData());
        assertNotNull(currEdgeContent.getData().getRecord());
        currRecord = currEdgeContent.getData().getRecord();
        assertEquals(currRecord.size(), 1);
        assertTrue(currRecord.containsKey("last_update"));
        assertEquals(currEdgeContent.getClasses(), "film_actor");
        assertEquals(currEdgeContent.getGroup(), "edges");
        assertEquals(currEdgeContent.getData().getSource(), "1_1");
        assertEquals(currEdgeContent.getData().getTarget(), "7_499");
        assertEquals(currEdgeContent.getData().getId(), "8_1_499");

        currEdgeContent = it.next();
        assertNotNull(currEdgeContent.getData());
        assertNotNull(currEdgeContent.getData().getRecord());
        currRecord = currEdgeContent.getData().getRecord();
        assertEquals(currRecord.size(), 1);
        assertTrue(currRecord.containsKey("last_update"));
        assertEquals(currEdgeContent.getClasses(), "film_actor");
        assertEquals(currEdgeContent.getGroup(), "edges");
        assertEquals(currEdgeContent.getData().getSource(), "1_1");
        assertEquals(currEdgeContent.getData().getTarget(), "7_506");
        assertEquals(currEdgeContent.getData().getId(), "8_1_506");

        currEdgeContent = it.next();
        assertNotNull(currEdgeContent.getData());
        assertNotNull(currEdgeContent.getData().getRecord());
        currRecord = currEdgeContent.getData().getRecord();
        assertEquals(currRecord.size(), 1);
        assertTrue(currRecord.containsKey("last_update"));
        assertEquals(currEdgeContent.getClasses(), "film_actor");
        assertEquals(currEdgeContent.getGroup(), "edges");
        assertEquals(currEdgeContent.getData().getSource(), "1_1");
        assertEquals(currEdgeContent.getData().getTarget(), "7_509");
        assertEquals(currEdgeContent.getData().getId(), "8_1_509");

        currEdgeContent = it.next();
        assertNotNull(currEdgeContent.getData());
        assertNotNull(currEdgeContent.getData().getRecord());
        currRecord = currEdgeContent.getData().getRecord();
        assertEquals(currRecord.size(), 1);
        assertTrue(currRecord.containsKey("last_update"));
        assertEquals(currEdgeContent.getClasses(), "film_actor");
        assertEquals(currEdgeContent.getGroup(), "edges");
        assertEquals(currEdgeContent.getData().getSource(), "1_1");
        assertEquals(currEdgeContent.getData().getTarget(), "7_605");
        assertEquals(currEdgeContent.getData().getId(), "8_1_605");

        currEdgeContent = it.next();
        assertNotNull(currEdgeContent.getData());
        assertNotNull(currEdgeContent.getData().getRecord());
        currRecord = currEdgeContent.getData().getRecord();
        assertEquals(currRecord.size(), 1);
        assertTrue(currRecord.containsKey("last_update"));
        assertEquals(currEdgeContent.getClasses(), "film_actor");
        assertEquals(currEdgeContent.getGroup(), "edges");
        assertEquals(currEdgeContent.getData().getSource(), "1_1");
        assertEquals(currEdgeContent.getData().getTarget(), "7_635");
        assertEquals(currEdgeContent.getData().getId(), "8_1_635");

        currEdgeContent = it.next();
        assertNotNull(currEdgeContent.getData());
        assertNotNull(currEdgeContent.getData().getRecord());
        currRecord = currEdgeContent.getData().getRecord();
        assertEquals(currRecord.size(), 1);
        assertTrue(currRecord.containsKey("last_update"));
        assertEquals(currEdgeContent.getClasses(), "film_actor");
        assertEquals(currEdgeContent.getGroup(), "edges");
        assertEquals(currEdgeContent.getData().getSource(), "1_1");
        assertEquals(currEdgeContent.getData().getTarget(), "7_749");
        assertEquals(currEdgeContent.getData().getId(), "8_1_749");

        currEdgeContent = it.next();
        assertNotNull(currEdgeContent.getData());
        assertNotNull(currEdgeContent.getData().getRecord());
        currRecord = currEdgeContent.getData().getRecord();
        assertEquals(currRecord.size(), 1);
        assertTrue(currRecord.containsKey("last_update"));
        assertEquals(currEdgeContent.getClasses(), "film_actor");
        assertEquals(currEdgeContent.getGroup(), "edges");
        assertEquals(currEdgeContent.getData().getSource(), "1_1");
        assertEquals(currEdgeContent.getData().getTarget(), "7_832");
        assertEquals(currEdgeContent.getData().getId(), "8_1_832");

        currEdgeContent = it.next();
        assertNotNull(currEdgeContent.getData());
        assertNotNull(currEdgeContent.getData().getRecord());
        currRecord = currEdgeContent.getData().getRecord();
        assertEquals(currRecord.size(), 1);
        assertTrue(currRecord.containsKey("last_update"));
        assertEquals(currEdgeContent.getClasses(), "film_actor");
        assertEquals(currEdgeContent.getGroup(), "edges");
        assertEquals(currEdgeContent.getData().getSource(), "1_1");
        assertEquals(currEdgeContent.getData().getTarget(), "7_939");
        assertEquals(currEdgeContent.getData().getId(), "8_1_939");

        currEdgeContent = it.next();
        assertNotNull(currEdgeContent.getData());
        assertNotNull(currEdgeContent.getData().getRecord());
        currRecord = currEdgeContent.getData().getRecord();
        assertEquals(currRecord.size(), 1);
        assertTrue(currRecord.containsKey("last_update"));
        assertEquals(currEdgeContent.getClasses(), "film_actor");
        assertEquals(currEdgeContent.getGroup(), "edges");
        assertEquals(currEdgeContent.getData().getSource(), "1_1");
        assertEquals(currEdgeContent.getData().getTarget(), "7_970");
        assertEquals(currEdgeContent.getData().getId(), "8_1_970");

        currEdgeContent = it.next();
        assertNotNull(currEdgeContent.getData());
        assertNotNull(currEdgeContent.getData().getRecord());
        currRecord = currEdgeContent.getData().getRecord();
        assertEquals(currRecord.size(), 1);
        assertTrue(currRecord.containsKey("last_update"));
        assertEquals(currEdgeContent.getClasses(), "film_actor");
        assertEquals(currEdgeContent.getGroup(), "edges");
        assertEquals(currEdgeContent.getData().getSource(), "1_1");
        assertEquals(currEdgeContent.getData().getTarget(), "7_980");
        assertEquals(currEdgeContent.getData().getId(), "8_1_980");


        /**
         * Get actors by movie
         */

        // expanding 1-N relationship: film <-[has_film]- actor

        rootIds[0] = "7_2";
        data = provider.expand(dataSource, rootIds, "in", "film_actor", 300);

        assertEquals(data.getNodesClasses().size(), 1);
        assertEquals(data.getNodes().size(), 4);
        assertEquals(data.getEdgesClasses().size(), 1);
        assertEquals(data.getEdges().size(), 4);

        // Node classes checks
        assertTrue(data.getNodesClasses().containsKey("actor"));

        Map<String, Object> actorClass = data.getNodesClasses().get("actor");
        assertTrue(actorClass.containsKey("actor_id"));
        assertTrue(actorClass.containsKey("first_name"));
        assertTrue(actorClass.containsKey("last_name"));
        assertTrue(actorClass.containsKey("last_update"));

        // nodes checks
        it = data.getNodes().iterator();

        currNodeContent = it.next();
        assertNotNull(currNodeContent.getData());
        assertNotNull(currNodeContent.getData().getRecord());
        currRecord = currNodeContent.getData().getRecord();
        assertTrue(currRecord.containsKey("actor_id"));
        assertEquals(currRecord.get("actor_id"), 19);
        assertTrue(currRecord.containsKey("first_name"));
        assertEquals(currRecord.get("first_name"), "Bob");
        assertTrue(currRecord.containsKey("last_name"));
        assertEquals(currRecord.get("last_name"), "Fawcett");
        assertTrue(currRecord.containsKey("last_update"));
        assertEquals(currRecord.get("last_update").toString(), "2013-05-26 14:47:57.62");

        currNodeContent = it.next();
        assertNotNull(currNodeContent.getData());
        assertNotNull(currNodeContent.getData().getRecord());
        currRecord = currNodeContent.getData().getRecord();
        assertTrue(currRecord.containsKey("actor_id"));
        assertEquals(currRecord.get("actor_id"), 85);
        assertTrue(currRecord.containsKey("first_name"));
        assertEquals(currRecord.get("first_name"), "Minnie");
        assertTrue(currRecord.containsKey("last_name"));
        assertEquals(currRecord.get("last_name"), "Zellweger");
        assertTrue(currRecord.containsKey("last_update"));
        assertEquals(currRecord.get("last_update").toString(), "2013-05-26 14:47:57.62");

        currNodeContent = it.next();
        assertNotNull(currNodeContent.getData());
        assertNotNull(currNodeContent.getData().getRecord());
        currRecord = currNodeContent.getData().getRecord();
        assertTrue(currRecord.containsKey("actor_id"));
        assertEquals(currRecord.get("actor_id"), 90);
        assertTrue(currRecord.containsKey("first_name"));
        assertEquals(currRecord.get("first_name"), "Sean");
        assertTrue(currRecord.containsKey("last_name"));
        assertEquals(currRecord.get("last_name"), "Guiness");
        assertTrue(currRecord.containsKey("last_update"));
        assertEquals(currRecord.get("last_update").toString(), "2013-05-26 14:47:57.62");

        currNodeContent = it.next();
        assertNotNull(currNodeContent.getData());
        assertNotNull(currNodeContent.getData().getRecord());
        currRecord = currNodeContent.getData().getRecord();
        assertTrue(currRecord.containsKey("actor_id"));
        assertEquals(currRecord.get("actor_id"), 160);
        assertTrue(currRecord.containsKey("first_name"));
        assertEquals(currRecord.get("first_name"), "Chris");
        assertTrue(currRecord.containsKey("last_name"));
        assertEquals(currRecord.get("last_name"), "Depp");
        assertTrue(currRecord.containsKey("last_update"));
        assertEquals(currRecord.get("last_update").toString(), "2013-05-26 14:47:57.62");

        // edges checks
        it = data.getEdges().iterator();

        currEdgeContent = it.next();
        assertNotNull(currEdgeContent.getData());
        assertNotNull(currEdgeContent.getData().getRecord());
        currRecord = currEdgeContent.getData().getRecord();
        assertEquals(currRecord.size(), 1);
        assertTrue(currRecord.containsKey("last_update"));
        assertEquals(currEdgeContent.getClasses(), "film_actor");
        assertEquals(currEdgeContent.getGroup(), "edges");
        assertEquals(currEdgeContent.getData().getSource(), "1_19");
        assertEquals(currEdgeContent.getData().getTarget(), "7_2");
        assertEquals(currEdgeContent.getData().getId(), "8_19_2");

        currEdgeContent = it.next();
        assertNotNull(currEdgeContent.getData());
        assertNotNull(currEdgeContent.getData().getRecord());
        currRecord = currEdgeContent.getData().getRecord();
        assertEquals(currRecord.size(), 1);
        assertTrue(currRecord.containsKey("last_update"));
        assertEquals(currEdgeContent.getClasses(), "film_actor");
        assertEquals(currEdgeContent.getGroup(), "edges");
        assertEquals(currEdgeContent.getData().getSource(), "1_85");
        assertEquals(currEdgeContent.getData().getTarget(), "7_2");
        assertEquals(currEdgeContent.getData().getId(), "8_85_2");

        currEdgeContent = it.next();
        assertNotNull(currEdgeContent.getData());
        assertNotNull(currEdgeContent.getData().getRecord());
        currRecord = currEdgeContent.getData().getRecord();
        assertEquals(currRecord.size(), 1);
        assertTrue(currRecord.containsKey("last_update"));
        assertEquals(currEdgeContent.getClasses(), "film_actor");
        assertEquals(currEdgeContent.getGroup(), "edges");
        assertEquals(currEdgeContent.getData().getSource(), "1_90");
        assertEquals(currEdgeContent.getData().getTarget(), "7_2");
        assertEquals(currEdgeContent.getData().getId(), "8_90_2");

        currEdgeContent = it.next();
        assertNotNull(currEdgeContent.getData());
        assertNotNull(currEdgeContent.getData().getRecord());
        currRecord = currEdgeContent.getData().getRecord();
        assertEquals(currRecord.size(), 1);
        assertTrue(currRecord.containsKey("last_update"));
        assertEquals(currEdgeContent.getClasses(), "film_actor");
        assertEquals(currEdgeContent.getGroup(), "edges");
        assertEquals(currEdgeContent.getData().getSource(), "1_160");
        assertEquals(currEdgeContent.getData().getTarget(), "7_2");
        assertEquals(currEdgeContent.getData().getId(), "8_160_2");

    }

}
