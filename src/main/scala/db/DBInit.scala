package db

import scala.io.Source

/**
 * Helper methods to build nodes and edges
 */
object DBInit extends App {

    val dbService: DBService = new DBService

    createNodesFromFile("facebook_nodes")
    linkNodesFromFile("facebook_combined.txt")

    def createNodesFromFile(filename: String) {
        for (line <- Source.fromFile(filename).getLines()) {
            dbService.createNode(line.toInt)
        }
    }

    def linkNodesFromFile(filename: String) {
        for (line <- Source.fromFile(filename).getLines()) {
            val connectedNodes = line.split(" +")
            val id1 = connectedNodes(0).toInt
            val id2 = connectedNodes(1).toInt
            dbService.connect(id1, id2)
        }
    }
}
