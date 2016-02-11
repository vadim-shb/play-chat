package domain

import play.api.libs.json.Json

case class IncomeTextMessage(message: String)

object IncomeTextMessage {

  implicit val reads = Json.reads[IncomeTextMessage]

  def parse(msg: String): IncomeTextMessage = reads.reads(Json.parse(msg)).get //todo: add validation and/or try/catch
}