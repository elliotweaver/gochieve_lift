package code.model

import net.liftweb.record.field._
import net.liftweb.mongodb._
import net.liftweb.mongodb.record._
import net.liftweb.mongodb.record.field._
import net.liftweb.util._
import net.liftweb.common._
import net.liftweb.sitemap._
import net.liftweb.json._
import com.restfb._
import xml.{Text, NodeSeq}
import net.liftweb.mongodb.BsonDSL._

object Action extends Action with MongoMetaRecord[Action] {
  
  override def fieldOrder = List(
      c_type,
      uid,
      gcid,
      created,
      updated,
      author_created,
      author_updated
      )
  
}

class Action extends MongoRecord[Action] with ObjectIdPk[Action] {
  def meta = Action
  
  object c_type extends StringField(this, 200)
  object uid extends StringField(this, 200)
  object gcid extends StringField(this, 200)
  object created extends DateTimeField(this)
  object updated extends DateTimeField(this)
  object author_created extends StringField(this, 200)
  object author_updated extends StringField(this, 200)
  
  def getAction(ctype: String, uid: String, gc: String) = Action.find(("c_type" -> ctype)~("uid" -> uid)~("gcid" -> gc))
  
}