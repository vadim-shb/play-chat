package actors

import akka.actor.{Actor, ActorRef, Props}

class ChatRootActor extends Actor {

  override def receive = {
    case (room: String, connection: ActorRef) => {
      val newRoom = context.actorOf(RoomActor.props(connection), room) //fixme: check if child actor already exists. Possible race condition.
      sender ! newRoom
    }
  }

}

object ChatRootActor {

  def props() = Props[ChatRootActor]

}