package protocol

import play.api.libs.json._

case class WsContainer(msgType: String, data: String) {

  def toJsonString: String = WsContainer.writes.writes(this).toString()

}

object WsContainer {

  implicit val reads = Json.reads[WsContainer]
  implicit val writes = Json.writes[WsContainer]

  def parse(msg: String): WsContainer = reads.reads(Json.parse(msg)).get //todo: add validation and/or try/catch

}
