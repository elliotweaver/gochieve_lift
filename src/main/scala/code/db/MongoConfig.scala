package code.db

import net.liftweb._
import util.Props
import com.mongodb.{Mongo, MongoOptions, ServerAddress}
import net.liftweb.mongodb.{DefaultMongoIdentifier, MongoDB}

object MongoConfig {
  def init: Unit = {
    val srvr = new ServerAddress(
       Props.get("mongo.host", "ds035257.mongolab.com"),
       Props.getInt("mongo.port", 35257)
    )
	val mo = new MongoOptions
	mo.socketTimeout = 10
	MongoDB.defineDbAuth(DefaultMongoIdentifier, new Mongo(srvr), "gochieve", "eweaver", "5tgb6yhn")
  }
}