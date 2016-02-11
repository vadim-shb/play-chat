package config

import akka.util.Timeout
import scala.concurrent.duration._

object Constants {
  implicit val AKKA_MEESSAGE_TIMEOUT = Timeout(3 seconds) //todo: is it used?
  val disconnectMessage = "WS_DISCONNECTED"
}
