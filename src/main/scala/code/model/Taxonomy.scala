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
import org.bson.types.ObjectId
import java.util.{Calendar, Date}
import java.text.SimpleDateFormat

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
  
  def getCategories = getBy("category").map( x => x.id.is.toString -> x.name.toString )
  
  def getMethods = getBy("method").map( x => x.id.is.toString -> x.name.toString )
  
  def getSettings = getBy("setting").map( x => x.id.is.toString -> x.name.toString )
  
  def createTags(t: String) = {
    parseTerms(t).foreach(
      a => {
        meta.where(_.c_type eqs "tag").and(_.machine eqs a._2).fetch match {
          case List(x) => a
          case _ => {
            meta.createRecord.c_type("tag").name(a._1).machine(a._2).save
            a
          }
        }
      })
  }
  
    
  def viewTags(t: String) = parseTerms(t).map(a => <span><a href={"tags?term="+a._2}>{a._1}</a>  </span>)
  
  def viewCategory(t: String) = {
    meta.where(_.id eqs new ObjectId(t)).fetch match {
      case List(x) => <span><a href={"category?term="+x.machine.toString}>{x.name.toString}</a></span>
      case _ => "No category selected"
    }
  }
  
  //TODO: Find a better replace algorithm to replace all non-alphanumerics with empty
  def parseTerms(t: String) = {
    t.split(",").map(s => {
      (s.trim, s.trim.toLowerCase.replace(" ", "-").replaceAll("/|\\\\|_|\\?|\\@", ""))
    }).toList
  }
  
}

object Taxonomy extends Taxonomy with MongoMetaRecord[Taxonomy] {}