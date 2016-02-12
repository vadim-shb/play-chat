import play.api.{Application, GlobalSettings}
import scalikejdbc.config.DBs

object Global extends GlobalSettings {

  override def onStart(app: Application): Unit = {
    DBs.setup('chat_db)
  }
}
