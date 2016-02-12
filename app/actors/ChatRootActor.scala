package actors

import akka.actor.{Actor, ActorRef, Props}
import config.Constants

class ChatRootActor extends Actor {

  override def receive = {
    case (room: String, connection: ActorRef) => {
      val newRoom = context.actorOf(RoomActor.props, room) //fixme: check if child actor already exists. Possible race condition.
      newRoom ! (Constants.setRoomName, room)
      newRoom ! connection
      sender ! newRoom
    }
  }

}

object ChatRootActor {

  def props() = Props[ChatRootActor]

}