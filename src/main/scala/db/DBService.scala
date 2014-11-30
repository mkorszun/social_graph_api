package db

import org.anormcypher._
import scala.collection.mutable.ListBuffer

class PathNotFoundException(msg: String) extends RuntimeException(msg)

class DBService {

    //implicit val connection = Neo4jREST.setURL("http://54.245.119.57:7474/db/data/")

    /**
     * Find shortest path between two nodes
     * @param id1
     * @param id2
     * @return list of node ids
     */
    def shortestPath(id1: Int, id2: Int): List[Int] = {
        val p = Cypher(
            f"MATCH (a:Person { id: $id1%d }),(b:Person { id: $id2%d }), p = shortestPath((a)-[*..]-(b)) RETURN p")()
            .map {
            case CypherRow(p: Map[String, Any]) => p
        }


        if (p.size == 0) {
            throw new PathNotFoundException("there is no path possible between the two nodes given")
        }

        val value: Any = p.apply(0).get("nodes").get
        val x: ListBuffer[String] = value.asInstanceOf[ListBuffer[String]]

        val nodeIds = x.map {
            v => getPersonId(v)
        }

        return nodeIds.toList
    }

    /**
     * Get node id for given url
     * @param node url
     * @return id
     */
    def getPersonId(node: String): Int = {
        val elem: Array[String] = node.split("/")
        val nodeId = elem.apply(elem.length - 1)
        val q: CypherStatement = Cypher(f"start n=node($nodeId%s) return n.id as id")
        return q.apply().head[BigDecimal]("id").toInt
    }

    /**
     * Create node with given id
     * @param id
     */
    def createNode(id: Int) {
        Cypher(f"CREATE (n:Person { id : $id%d})").execute()
    }

    /**
     * Connect two nodes
     * @param id1
     * @param id2
     */
    def connect(id1: Int, id2: Int) {
        Cypher(
            f"MATCH (a:Person),(b:Person) WHERE a.id = $id1%d AND b.id = $id2%d CREATE (a)-[r:KNOWS]->(b) RETURN r")
            .execute()
        Cypher(
            f"MATCH (a:Person),(b:Person) WHERE a.id = $id2%d AND b.id = $id1%d CREATE (a)-[r:KNOWS]->(b) RETURN r")
            .execute()
    }
}

