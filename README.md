[Social Graph API](http://socialgraphapi.cloudcontrolled.com)
================

## Purpose 

Query samples of real social graph data. Application exposes two endpoints:

### Find shortest path between two persons:
~~~http
Request:
GET /distance/4017/3982
 
Response:
200 OK
{
  "distance" : 3,
  "possible_path": ['4017', '3986', '3982']
}
~~~

### Find common friends between two persons:
~~~http
Request:
GET /common_friends/4017/3982
 
Response:
200 OK
{
  "common_friends" : ['3980', '3986', '4014', '4021', '4026', '4030']
}
~~~

## Software stack

* Scala + [Spray](http://spray.io/) framework for REST API
* [Neo4j](http://neo4j.com/) Graph DB with [Cypher](http://neo4j.com/docs/stable/cypher-query-lang.html) query language
* [cloudControl](www.cloudcontrol.com) PaaS

## Social graph

### Graph initialisation

Export Neo4j url:
~~~
$ export NEO_URL=http://111.111.111.111:7474/db/data/
~~~

Load dump:
~~~scala
$ sbt compile stage
$ sbt ./target/start db.DBInit
~~~

### Graph size

* **4039** - nodes
* **176468** - edges (in Neo4j all edges are directed so number of relations is doubled comparing to dump file)

### Shortest path - Cypher query

~~~
MATCH (a:Person { id: ID1 }),(b:Person { id: ID2 }), p = shortestPath((a)-[*..]-(b)) RETURN p
~~~

### Common friends - Cypher query

~~~
MATCH (a:Person { id: ID1 }),(b:Person { id: ID2 }), p = allShortestPaths((a)-[*..3]-(b)) RETURN p
~~~

### Application is deployed on cloudControl PaaS:
`http://socialgraphapi.cloudcontrolled.com/`

### Discussion
1. [Algorithm used by Neo4j](https://github.com/neo4j/neo4j/tree/master/community/graph-algo/src/main/java/org/neo4j/graphalgo/impl): single shortest path (BFS/Dijkstra), multiple shortest paths (FloydWarshall - assumption). Social graps are expected to be rather sparse in general and dense locally, that is why finding most optimal algorithm is challenging. Stronly believe that social network science ([clique](http://en.wikipedia.org/wiki/Clique_(graph_theory)), [network communities](http://en.wikipedia.org/wiki/Community_structure)) + heuristic needs to be applied here for the most efficient solution.
2. Neo4j single instance data capacity: ~34 billion nodes / ~34 billion edges - so it can handle quite big data volumes. Graph sharding with [communities detection](http://horicky.blogspot.de/2012/11/detecting-communities-in-social-graph.html) could be considered for the scaling/clustering purposes.
3. Possible tests:
	* Test api with mocked db responses - validate result serialization and error handling
	* Test Neo4j queries using in memory database with loaded fixtures
	* Use Neo4j [test libraries](http://neo4j.com/docs/stable/tutorials-java-unit-testing.html)
	* Create docker container with provisioned Neo4j instance and API application together with test data. Use for integration and load tests.