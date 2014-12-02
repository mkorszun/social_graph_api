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