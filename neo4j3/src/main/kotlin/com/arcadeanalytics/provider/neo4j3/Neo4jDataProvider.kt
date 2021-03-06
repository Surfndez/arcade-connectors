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
package com.arcadeanalytics.provider.neo4j3

import com.arcadeanalytics.provider.CytoData
import com.arcadeanalytics.provider.DataSourceGraphDataProvider
import com.arcadeanalytics.provider.DataSourceInfo
import com.arcadeanalytics.provider.GraphData
import com.arcadeanalytics.provider.neo4j3.Neo4jDialect.NEO4J
import com.arcadeanalytics.provider.neo4j3.Neo4jDialect.NEO4J_MEMGRAPH
import org.neo4j.driver.v1.AccessMode
import org.neo4j.driver.v1.Session
import org.slf4j.LoggerFactory

/**
 *
 * @author Roberto Franchini
 */

class Neo4jDataProvider : DataSourceGraphDataProvider {

    private val log = LoggerFactory.getLogger(Neo4jDataProvider::class.java)

    override fun fetchData(datasource: DataSourceInfo,
                           query: String,
                           limit: Int): GraphData {
        getDriver(datasource).use { driver ->
            driver.session(AccessMode.READ).use { session ->

                log.info("API:: fetching data from query '{}' with limit {}  ", query, limit)

                val graphData = fetchData(session, datasource, query, limit, Neo4jStatementResultMapper(datasource, limit))

                session.closeAsync()
                driver.closeAsync()
                log.info("API:: totals: nodes {} - edges {} - truncated {} ", graphData.nodes.size, graphData.edges.size, graphData.truncated)

                return graphData

            }
        }
    }


    override fun expand(datasource: DataSourceInfo,
                        nodeIds: Array<String>,
                        direction: String,
                        edgeLabel: String,
                        maxTraversal: Int): GraphData {


        val label: String = if (edgeLabel.isEmpty()) "[rel]" else "[rel:$edgeLabel]"

        val rel = when (direction) {
            "out" -> "-$label->"
            "in" -> "<-$label-"
            else -> "<-$label->"
        }

        val cleanedIds = nodeIds.asSequence()
                .map { id -> toNeo4jId(datasource, id) }
                .distinct()
                .joinToString(",")


        val query = "MATCH (node)$rel(target) WHERE id(node) IN [$cleanedIds] return node, rel, target"


        getDriver(datasource).use { driver ->
            driver.session(AccessMode.READ).use { session ->

                val graphData = fetchData(session, datasource, query, maxTraversal, Neo4jStatementResultMapper(datasource, maxTraversal))
                session.closeAsync()
                driver.closeAsync()

                log.info("totals expanded: nodes {} - edges {} - truncated {} ", graphData.nodes.size, graphData.edges.size, graphData.truncated)
                return graphData
            }
        }


    }


    override fun load(datasource: DataSourceInfo, ids: Array<String>): GraphData {

        val cleanedIds = ids.asSequence()
                .map { id -> toNeo4jId(datasource, id) }
                .joinToString(",")

        val query = """MATCH (n)
            WHERE id(n) IN [${cleanedIds}]
            RETURN n"""


        getDriver(datasource).use { driver ->

            driver.session(AccessMode.READ).use { session ->

                val graphData = fetchData(session, datasource, query, ids.size, Neo4jStatementResultMapper(datasource, ids.size))
                session.closeAsync()
                driver.closeAsync()

                log.info("totals loaded: nodes {} - edges {} - truncated {} ", graphData.nodes.size, graphData.edges.size, graphData.truncated)
                return graphData
            }

        }


    }

    override fun loadFromClass(dataSource: DataSourceInfo, className: String, limit: Int): GraphData {
        val query = "MATCH (element:$className) RETURN element LIMIT $limit"
        return this.fetchData(dataSource, query, limit)
    }

    override fun loadFromClass(dataSource: DataSourceInfo, className: String, propName: String, propValue: String, limit: Int): GraphData {
        val query = "MATCH (element:$className) WHERE element.$propName = '$propValue' RETURN element LIMIT $limit"
        return this.fetchData(dataSource, query, limit)
    }


    override fun testConnection(dataSource: DataSourceInfo): Boolean {
        val connectionUrl = createConnectionUrl(dataSource)

        getDriver(dataSource).use { driver ->

            log.info("testing connection to :: '{}' ", connectionUrl)

            try {
                driver.session(AccessMode.READ).use { session ->

                    log.info("connection works fine:: '{}' ", connectionUrl)
                    return session.isOpen

                }
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }


    }


    private fun fetchData(session: Session,
                          dataSource: DataSourceInfo,
                          query: String,
                          limit: Int,
                          mapper: Neo4jStatementResultMapper): GraphData {


        log.info("INT:: fetching data from query '{}' with limit {}  ", query, limit)

        val (nodesClasses,
                edgesClasses,
                nodes,
                edges,
                truncated)
                = runQueryAndMapResult(session, query, mapper)

        countInAndOutOnNode(session, dataSource, nodes)

        val graphData = GraphData(nodesClasses,
                edgesClasses,
                nodes,
                edges,
                truncated)
        log.info("INT:: totals: nodes {} - edges {} - truncated {} ", graphData.nodes.size, graphData.edges.size, graphData.truncated)
        return graphData
    }

    private fun runQueryAndMapResult(session: Session,
                                     query: String,
                                     mapper: Neo4jStatementResultMapper): GraphData {


        log.info("run query and map:: '{}' ", query)

        val result = session.run(query)

        val graphData = mapper.map(result)

        log.info("run query and map -results:: nodes {} - edges {} - truncated {} ", graphData.nodes.size, graphData.edges.size, graphData.truncated)

        return graphData
    }


    private fun countInAndOutOnNode(session: Session, dataSource: DataSourceInfo, nodes: Set<CytoData>): Set<CytoData> {


        nodes.asSequence()
                .forEach { data ->
                    val id = toNeo4jId(dataSource, data.data.id)

                    val inQury = """MATCH (a)<-[r]-(o)
                                        WHERE id(a) IN [$id]
                                        WITH a, o, type(r) as type
                                        RETURN type, count(type) as in"""

                    val record = data.data.record
                    session.run(inQury).forEach { res ->

                        val type = res["type"].asString()
                        val count = res["in"].asInt()

                        val entry: MutableMap<String, Int> = record["@in"] as MutableMap<String, Int>

                        entry[type] = count

                        record["@edgeCount"] = record["@edgeCount"] as Int + count
                    }
                    val outQury = """MATCH (a)-[r]->(o)
                                        WHERE id(a) IN [$id]
                                        WITH a, o, type(r) as type
                                        RETURN type, count(type) as out"""

                    session.run(outQury).forEach { res ->

                        val type = res["type"].asString()
                        val count = res["out"].asInt()

                        val entry: MutableMap<String, Int> = record["@out"] as MutableMap<String, Int>

                        entry[type] = count
                        record["@edgeCount"] = record["@edgeCount"] as Int + count

                    }


                }

        return nodes
    }


    override fun supportedDataSourceTypes(): Set<String> {
        return setOf(NEO4J.name, NEO4J_MEMGRAPH.name)
    }

}
