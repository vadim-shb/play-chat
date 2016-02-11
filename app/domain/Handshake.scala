package domain

import play.api.libs.json.Json

case class Handshake(room: String, user:String)

object Handshake {

  implicit val reads = Json.reads[Handshake]

  def parse(msg: String): Handshake = reads.reads(Json.parse(msg)).get //todo: add validation and/or try/catch
}