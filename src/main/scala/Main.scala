import model.{CommonFriends, Distance}
import spray.routing.SimpleRoutingApp
import akka.actor._

object Main extends App with SimpleRoutingApp {

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
                            import spray.json._

                            complete(Distance(3, Array(1, 2, 3)).toJson.toString())
                        }
                    }
            } ~
            pathPrefix("common_friends" / IntNumber / IntNumber) {
                (a, b) =>
                    pathEnd {
                        get {
                            import format.CommonFriendsJsonFormat._
                            import spray.json._

                            complete(CommonFriends(Array(1, 2, 3)).toJson.toString())
                        }
                    }
            }
    }
}

