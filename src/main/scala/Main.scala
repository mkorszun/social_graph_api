import db.{PathNotFoundException, DBService}
import model.{CommonFriends, Distance, Error}
import spray.routing.SimpleRoutingApp
import akka.actor._

object Main extends App with SimpleRoutingApp {

    val service: DBService = new DBService()
    implicit val system = ActorSystem("my-system")

    startServer(interface = "0.0.0.0", port = System.getenv("PORT").toInt) {

        path("") {
            get {
                complete("OK")
            }
        } ~
            pathPrefix("distance" / IntNumber / IntNumber) {
                (a, b) =>
                    pathEnd {
                        get {
                            import format.DistanceJsonFormat._
                            import format.ErrorJsonFormat._
                            import spray.json._

                            try {
                                val nodes: List[Int] = service.shortestPath(a, b)
                                complete(Distance(nodes.size, nodes).toJson.toString())
                            } catch {
                                case e: PathNotFoundException => complete(404,
                                    Error(e.getMessage).toJson.toString())
                                case e: Exception => complete(500,
                                    Error("Internal server error").toJson.toString())
                            }
                        }
                    }
            } ~
            pathPrefix("common_friends" / IntNumber / IntNumber) {
                (a, b) =>
                    pathEnd {
                        get {
                            import format.CommonFriendsJsonFormat._
                            import format.ErrorJsonFormat._
                            import spray.json._

                            try {
                                val nodes: List[Int] = List(1, 2, 3)
                                complete(CommonFriends(nodes).toJson.toString())
                            } catch {
                                case e: Exception => complete(500,
                                    Error("Internal server error").toJson.toString())
                            }
                        }
                    }
            }
    }
}
