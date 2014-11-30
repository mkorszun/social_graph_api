package format

import spray.json.DefaultJsonProtocol

object CommonFriendsJsonFormat extends DefaultJsonProtocol {
    implicit val commonFriendsFormat = jsonFormat1(model.CommonFriends)
}

object DistanceJsonFormat extends DefaultJsonProtocol {
    implicit val distanceFormat = jsonFormat2(model.Distance)
}

object ErrorJsonFormat extends DefaultJsonProtocol {
    implicit val errorFormat = jsonFormat1(model.Error)
}

