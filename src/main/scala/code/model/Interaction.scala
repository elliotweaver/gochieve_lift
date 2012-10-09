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

class Interaction extends MongoRecord[Interaction] with ObjectIdPk[Interaction] {
  def meta = Interaction
  
  object c_type extends StringField(this, 200)
  object uid extends StringField(this, 200)
  object gcid extends StringField(this, 200)
  object subject extends StringField(this, 200)
  object message extends TextareaField(this, 200)
  object link extends StringField(this, 200)
  object video extends StringField(this, 200)
  object image extends StringField(this, 200)
  object created extends DateTimeField(this)
  object updated extends DateTimeField(this)
  object author_created extends StringField(this, 200)
  object author_updated extends StringField(this, 200)
  
}

object Interaction extends Interaction with MongoMetaRecord[Interaction] {
  override def fieldOrder = List(
      c_type,
      uid,
      gcid,
      subject,
      message,
      link,
      video,
      image,
      created,
      updated,
      author_created,
      author_updated
      )
}