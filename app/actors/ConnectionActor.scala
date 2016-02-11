package actors

import akka.actor._
import domain.{IncomeTextMessage, Handshake}
import protocol.WsContainer

class ConnectionActor(out: ActorRef) extends Actor {

  override def receive = {
    case msg: String => {
      println(msg)
      val wsContainer: WsContainer = WsContainer.parse(msg)
      wsContainer.msgType match {
        case "HANDSHAKE" => {
          val handshake = Handshake.parse(wsContainer.data)
//          context.system.
        }
        case "SEND_TEXT_MESSAGE" => {
          val incomeTextMessage = IncomeTextMessage.parse(wsContainer.data)

        }
      }
      out ! "I received your message: " + msg
    }
  }

}

object ConnectionActor {

  def props(out: ActorRef) = Props(new ConnectionActor(out))

}
