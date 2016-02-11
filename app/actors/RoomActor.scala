package actors

import akka.actor.{ActorRef, Actor, Props}
import config.Constants
import domain.OutcomeTextMessage

import scala.collection.mutable

class RoomActor(firstConnection: ActorRef) extends Actor {

  val connections = new mutable.ListBuffer[ActorRef]
  connections += firstConnection

  override def receive = {
    case outcomeTextMessage: OutcomeTextMessage => { //todo: we could put timestamp of message here
      connections.foreach(_ ! outcomeTextMessage) //todo: save history of messages to DB
      connections(0) ! outcomeTextMessage //todo: save history of messages to DB
    }
    case newConnectionToTheRoom: ActorRef => {
      connections += newConnectionToTheRoom //todo: push history messages to new connection
    }
    case (Constants.disconnectMessage, disconnectedConnection:ActorRef) => {
      connections -= disconnectedConnection
    }
  }

}

object RoomActor {

  def props(connection: ActorRef) = Props(classOf[RoomActor], connection)

}