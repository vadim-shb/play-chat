import actors.ConnectionActor
import play.api.mvc.WebSocket
import play.api.{Application, GlobalSettings}
import play.api.Play.current

object Global extends GlobalSettings {

  override def onStart(app: Application): Unit = {
//    WebSocket.acceptWithActor[String, String] { request => out =>
//      ConnectionActor.props(out)
//    }
  }
}
