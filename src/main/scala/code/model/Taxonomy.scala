package code.model

import net.liftweb.record.field._
import net.liftweb.mongodb._
import net.liftweb.mongodb.record._
import net.liftweb.mongodb.record.field._
import net.liftweb.util._
import net.liftweb.common._
import net.liftweb.sitemap._
import net.liftweb.json._
import xml.{Text, NodeSeq}
import com.foursquare.rogue.Rogue._

class Taxonomy extends MongoRecord[Taxonomy] with ObjectIdPk[Taxonomy] {
  def meta = Taxonomy
  
  object c_type extends StringField(this, 200)
  object machine extends StringField(this, 200)
  object name extends StringField(this, 200)
  object weight extends IntField(this)
  object created extends DateTimeField(this)
  object updated extends DateTimeField(this)
  object author_created extends StringField(this, 200)
  object author_updated extends StringField(this, 200)
  
  def getBy(t: String) = meta.where(_.c_type eqs t).fetch()
  
  def getCategories = getBy("category").map( x => x.machine.toString -> x.name.toString )
  
  def getMethods = getBy("method").map( x => x.machine.toString -> x.name.toString )
  
  def getSettings = getBy("setting").map( x => x.machine.toString -> x.name.toString )
  
}

object Taxonomy extends Taxonomy with MongoMetaRecord[Taxonomy] {}