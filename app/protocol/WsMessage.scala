package protocol

import play.api.libs.json.JsObject

trait WsMessage {
  def toJson: JsObject
}
