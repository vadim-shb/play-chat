name := "play_chat"

version := "1.0"

lazy val `play_chat` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq( jdbc , cache , ws   , specs2 % Test )

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

val scalikejdbcV = "2.2.5"

libraryDependencies ++= Seq(
  "org.postgresql"    %   "postgresql"          % "9.4-1201-jdbc41",
  "org.scalikejdbc"   %%  "scalikejdbc"         % scalikejdbcV,
  "org.scalikejdbc"   %%  "scalikejdbc-config"  % scalikejdbcV
)