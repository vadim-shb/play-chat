package persistence

import java.sql.Connection

import scalikejdbc.{ConnectionPool, DB, DBSession}
import scalikejdbc._

trait DbConnected {
  def connectionFromPool : Connection = ConnectionPool.borrow('chat_db)
  def dbFromPool : DB = DB(connectionFromPool)
  def insideLocalTx[A](sqlRequest: DBSession => A): A = {
    using(dbFromPool) { db =>
      db localTx { session =>
        sqlRequest(session)
      }
    }
  }

  def insideReadOnly[A](sqlRequest: DBSession => A): A = {
    using(dbFromPool) { db =>
      db readOnly { session =>
        sqlRequest(session)
      }
    }
  }
}
