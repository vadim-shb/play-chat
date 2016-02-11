package domain

import play.api.libs.json.Json

case class OutcomeTextMessage(author: String, message: String)

object OutcomeTextMessage {

  implicit val writes = Json.writes[OutcomeTextMessage]

  def toJsonString(outcomeTextMessage: OutcomeTextMessage): String = writes.writes(outcomeTextMessage).toString()
}
