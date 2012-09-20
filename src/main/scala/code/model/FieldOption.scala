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
object FieldOption extends FieldOption with MongoMetaRecord[FieldOption] with MongoCRUDify[FieldOption] {
    
  override def pageWrapper(body: NodeSeq) =
      <lift:surround with="_layout_single" at="content">
        <div class="lift:Msgs?showAll=true"></div>
        {body}
      </lift:surround>
  
  override def addlMenuLocParams = List(MenuGroups.TopBarGroup)
  
  override def fieldOrder = List(
      name,
      slug,
      optionType,
      created,
      updated,
      author_created,
      author_updated
      )
  
}

/**
* An O-R mapped class
*/
class FieldOption private() extends MongoRecord[FieldOption] with ObjectIdPk[FieldOption] {
  def meta = FieldOption
  
  object name extends StringField(this, 200) {
    override def displayName = "Name"
  }
  object slug extends StringField(this, 200) {
    override def displayName = "Slug"
  }
  object optionType extends EnumField(this, OptionTypes) {
    override def displayName = "Option Type"
  }
  object created extends DateTimeField(this)
  object updated extends DateTimeField(this)
  object author_created extends StringField(this, 200)
  object author_updated extends StringField(this, 200)
  
  object OptionTypes extends Enumeration {
    val GochieveTag                     = new Val("GoChieve Tag")
    val GochieveCategory                = new Val("GoChieve Category")
    val GochieveAccomplishmentMethod    = new Val("GoChieve Accomplishment Method")
    val GochievePostSetting             = new Val("GoChieve Post Setting")
    val GochieveOption                  = new Val("GoChieve Option")
  }

}