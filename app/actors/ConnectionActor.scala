package actors

import akka.actor._
import akka.pattern.ask
import config.Constants
import config.Constants._
import domain.{OutcomeTextMessage, Handshake, IncomeTextMessage}
import protocol.WsContainer

import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

class ConnectionActor(out: ActorRef) extends Actor {

  var cachedRoomActor : Option[ActorRef] = Option.empty
  var cachedUser: Option[String] = Option.empty


  @throws[Exception](classOf[Exception])
  override def postStop() = {
    cachedRoomActor.foreach(_ ! (Constants.disconnectMessage, out))
  }

  override def receive = {
    case msg: String => {
      println(msg)
      val wsContainer: WsContainer = WsContainer.parse(msg)
      wsContainer.msgType match {
        case "HANDSHAKE" => {
          val handshake = Handshake.parse(wsContainer.data)
          cachedUser = Option(handshake.user)
          sendConnectionToRoomActor(handshake.room)
        }
        case "SEND_TEXT_MESSAGE" => {
          val incomeTextMessage = IncomeTextMessage.parse(wsContainer.data)
          //Protocol require handshake before interchange messages
          for {
            roomActor <- cachedRoomActor
            user <- cachedUser
          } yield roomActor ! OutcomeTextMessage(user, incomeTextMessage.message)
        }
      }
    }
  }

  private def sendConnectionToRoomActor(room: String) = {
    context.system.actorSelection("akka://application/user/chat-rooms-root/" + room).resolveOne().onComplete {
      case Success(roomActor) => {
        cachedRoomActor = Option(roomActor)
        roomActor ! out
      }
      case Failure(ex) => {
        if (ex.getClass.getSimpleName == "ActorNotFound") {
            createRoomActorAndSendConnectionToIt(room)
        }
      }
    }
  }

  private def createRoomActorAndSendConnectionToIt(room: String) = {
    context.system.actorSelection("akka://application/user/chat-rooms-root/").resolveOne().onComplete {
      case Success(chatRootActor) => {
        registerRoomInChatRoot(chatRootActor, room)
      }
      case Failure(ex) => { //todo: create chat-root actor on startup time
        if (ex.getClass.getSimpleName == "ActorNotFound") {
            createChatRootAndCreateRoomWithConnectionInIt(room)
        }
      }
    }
  }

  private def createChatRootAndCreateRoomWithConnectionInIt(room: String) = {
    val chatRootActor = context.system.actorOf(ChatRootActor.props(), "chat-rooms-root")
    registerRoomInChatRoot(chatRootActor, room)
  }

  private def registerRoomInChatRoot(chatRootActor: ActorRef, room: String): Unit = {
    for (
      roomActor <- (chatRootActor ? (room, out)).mapTo[ActorRef]
    ) yield cachedRoomActor = Option(roomActor)
  }

}

object ConnectionActor {

  def props(out: ActorRef) = Props(classOf[ConnectionActor], out)

}
