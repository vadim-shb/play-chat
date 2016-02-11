package actors

import akka.actor.{PoisonPill, ActorRef, Actor, Props}
import config.Constants
import domain.OutcomeTextMessage
import protocol.WsContainer

import scala.collection.mutable

class RoomActor(firstConnection: ActorRef) extends Actor {

  val connections = new mutable.ListBuffer[ActorRef]
  connections += firstConnection

  override def receive = {
    case outcomeTextMessage: OutcomeTextMessage => { //todo: we could put timestamp of message here
      connections.foreach(_ ! WsContainer("BROADCAST_TEXT_MESSAGE", outcomeTextMessage.toJsonString).toJsonString) //todo: save history of messages to DB
    }
    case newConnectionToTheRoom: ActorRef => {
      connections += newConnectionToTheRoom //todo: push history messages to new connection
    }
    case (Constants.disconnectMessage, disconnectedConnection:ActorRef) => {
      connections -= disconnectedConnection
      if (connections.isEmpty) { //todo: think about race conditions
        self ! PoisonPill
      }
    }
  }

}

object RoomActor {

  def props(connection: ActorRef) = Props(classOf[RoomActor], connection)

}