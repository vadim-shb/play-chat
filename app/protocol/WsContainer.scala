package protocol

import play.api.libs.json._

case class WsContainer(msgType: String, data: String)

//  extends WsMessage {
//
//  override def toJson: JsObject = Json.obj(
//    "msgType" -> msgType,
//    "data" -> data
//  )
//
//}
//
object WsContainer {

  implicit val reads = Json.reads[WsContainer]
  implicit val writes = Json.writes[WsContainer]

  def parse(msg: String): WsContainer = reads.reads(Json.parse(msg)).get //todo: add validation and/or try/catch
  def toJsonString(wsContainer: WsContainer): String = writes.writes(wsContainer).toString()


}
