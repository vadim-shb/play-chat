package actors

import akka.actor.Status.{Failure, Success}
import akka.actor._
import domain.{Handshake, IncomeTextMessage}
import protocol.WsContainer

class ConnectionActor(out: ActorRef) extends Actor {

  override def receive = {
    case msg: String => {
      println(msg)
      val wsContainer: WsContainer = WsContainer.parse(msg)
      wsContainer.msgType match {
        case "HANDSHAKE" => {
          val handshake = Handshake.parse(wsContainer.data)
          roomActor = getRoomActor(handshake.room)
          roomActor ! out
        }
        case "SEND_TEXT_MESSAGE" => {
          val incomeTextMessage = IncomeTextMessage.parse(wsContainer.data)

        }
      }
      out ! "I received your message: " + msg
    }
  }

  private def getRoomActor(room: String) : ActorRef= {
    context.system.actorSelection("chat-room/" + room).resolveOne().onComplete {
      case Success(actorRef) => {}
      case Failure(ex) => {}
    }
  }
}

object ConnectionActor {

  def props(out: ActorRef) = Props(new ConnectionActor(out))

}
