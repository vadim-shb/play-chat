package domain

import play.api.libs.json.Json

case class OutcomeTextMessage(author: String, message: String) {

  def toJsonString: String = OutcomeTextMessage.writes.writes(this).toString()

}

object OutcomeTextMessage {

  implicit val writes = Json.writes[OutcomeTextMessage]

}
