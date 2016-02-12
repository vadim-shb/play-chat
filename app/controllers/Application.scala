package controllers

import actors.ConnectionActor
import play.api.mvc.{Action, WebSocket, Controller}
import play.api.Play.current

object Application extends Controller {

  def indexRedirect = Action {
    Redirect("/index.html")
  }

  def wsHandler = WebSocket.acceptWithActor[String, String] { request => out => {
    ConnectionActor.props(out)
  }}

}
