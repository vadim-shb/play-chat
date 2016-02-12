package actors

import akka.actor.{Actor, ActorRef, PoisonPill, Props}
import config.Constants
import domain.OutcomeTextMessage
import persistence.DbConnected
import protocol.WsContainer
import scalikejdbc._

import scala.collection.mutable

class RoomActor(firstConnection: ActorRef) extends Actor with DbConnected {

  val connections = new mutable.ListBuffer[ActorRef]
  connections += firstConnection
  var cachedRoomName: Option[String] = Option.empty


  override def receive = {

    case (Constants.setRoomName, roomName: String) => {
      cachedRoomName = Option(roomName)
    }
    case outcomeTextMessage: OutcomeTextMessage => {
      //todo: we could put timestamp of message here
      persistTextMessage(outcomeTextMessage)
      connections.foreach(_ ! WsContainer("BROADCAST_TEXT_MESSAGE", outcomeTextMessage.toJsonString).toJsonString)
    }
    case newConnectionToTheRoom: ActorRef => {
      connections += newConnectionToTheRoom
      loadRoomHistory.foreach(outcomeTextMessage =>
        newConnectionToTheRoom ! WsContainer("BROADCAST_TEXT_MESSAGE", outcomeTextMessage.toJsonString).toJsonString
      )
    }
    case (Constants.disconnectMessage, disconnectedConnection: ActorRef) => {
      connections -= disconnectedConnection
      if (connections.isEmpty) {
        //todo: think about race conditions
        self ! PoisonPill
      }
    }
  }

  private def persistTextMessage(textMessage: OutcomeTextMessage) = {
    using(dbFromPool) { db =>
      db localTx { implicit session =>
        sql"""INSERT INTO messages (room,author,message) VALUES (
              ${cachedRoomName.get},
              ${textMessage.author},
              ${textMessage.message}
             )""".execute().apply()
      }
    }
  }

  def loadRoomHistory = { //todo: blocking. Think about creating UserActor and do long things there
    insideReadOnly { implicit session =>
      sql"SELECT author, message FROM messages WHERE room = ${cachedRoomName.get} ORDER BY id".map(rs =>
        OutcomeTextMessage(rs.string("author"),
          rs.string("message")))
        .list.apply()
    }
  }
}

object RoomActor {

  def props(connection: ActorRef) = Props(classOf[RoomActor], connection)

}