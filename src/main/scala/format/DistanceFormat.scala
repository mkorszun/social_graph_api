package format

import spray.json.DefaultJsonProtocol

object DistanceJsonFormat extends DefaultJsonProtocol {
    implicit val distanceFormat = jsonFormat2(model.Distance)
}
