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
import code.lib.{MongoCRUDify}
import bootstrap.liftweb.MenuGroups
import xml.{ Text, NodeSeq }

/**
* The singleton that has methods for accessing the database
*/
object File extends File with MongoMetaRecord[File] with MongoCRUDify[File] {
    
  override def pageWrapper(body: NodeSeq) =
      <lift:surround with="_layout_single" at="content">
        <div class="lift:Msgs?showAll=true"></div>
        {body}
      </lift:surround>
  
  override def addlMenuLocParams = List(MenuGroups.TopBarGroup)
  
  override def fieldOrder = List(
      filename,
      fileurl,
      fileuri,
      filetype,
      fileaccess,
      created,
      updated,
      author_created,
      author_updated
      )
  
}

/**
* An O-R mapped class
*/
class File private() extends MongoRecord[File] with ObjectIdPk[File] {
  def meta = File
  
  object filename extends StringField(this, 200) {
    override def displayName = "Filename"
  }
  object fileurl extends StringField(this, 200) {
    override def displayName = "URL"
  }
  object fileuri extends StringField(this, 200) {
    override def displayName = "URI"
  }
  object filetype extends StringField(this, 200) {
    override def displayName = "Filetype"
  }
  object fileaccess extends EnumField(this, AccessTypes) {
    override def displayName = "Access"
  }
  object created extends DateTimeField(this)
  object updated extends DateTimeField(this)
  object author_created extends StringField(this, 200)
  object author_updated extends StringField(this, 200)

  object AccessTypes extends Enumeration {
    val Public   = new Val("Public")
    val Private  = new Val("Private")
  }
  
}