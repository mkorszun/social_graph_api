package format

import spray.json.DefaultJsonProtocol

object CommonFriendsJsonFormat extends DefaultJsonProtocol {
    implicit val commonFriendsFormat = jsonFormat1(model.CommonFriends)
}
