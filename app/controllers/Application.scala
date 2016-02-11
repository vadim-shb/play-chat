package controllers

import actors.ConnectionActor
import play.api.mvc.{WebSocket, Controller}
import play.api.Play.current

@Singleton
object Application extends Controller {

  def wsHandler = WebSocket.acceptWithActor[String, String] { request => out => {
    ConnectionActor.props(out)
  }}

}
