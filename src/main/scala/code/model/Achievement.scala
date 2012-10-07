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
import net.liftweb.widgets.autocomplete.AutoComplete

/**
* The singleton that has methods for accessing the database
*/
object Achievement extends Achievement with MongoMetaRecord[Achievement] with MongoCRUDify[Achievement] {
    
  override def pageWrapper(body: NodeSeq) =
      <lift:surround with="_layout_single" at="content">
        <div class="lift:Msgs?showAll=true"></div>
        {body}
      </lift:surround>
  
  override def addlMenuLocParams = List(MenuGroups.TopBarGroup)
  
  override def fieldOrder = List(
      title,
      description,
      tags,
      image,
      setting,
      method,
      location,
      category,
      deadline,
      allow_comments,
      only_approved_comments,
      can_vote,
      can_view_ratings,
      is_private,
      created,
      updated,
      author_created,
      author_updated
      )
  
}

/**
* An O-R mapped "Achievement" class
*/
class Achievement private() extends MongoRecord[Achievement] with ObjectIdPk[Achievement] {
  def meta = Achievement
  
  object title extends StringField(this, 200) {
    override def displayName = "Title"
  }
  object description extends TextareaField(this, 12) {
    override def displayName = "Description"
  }
  object tags extends StringField(this, 200) {
    override def displayName = "Tags"
  }
  object image extends StringField(this, 200) {
    override def displayName = "Image"
  }
  object setting extends StringField(this, 200) {
    override def displayName = "Post Setting"
  }
  object method extends StringField(this, 200) {
    override def displayName = "Accomplishment Method"
  }
  object location extends StringField(this, 200) {
    override def displayName = "Location"
  }
  object category extends StringField(this, 200) {
    override def displayName = "Category"
  }
  object deadline extends DateTimeField(this) {
    override def displayName = "Deadline"
  }
  object allow_comments extends BooleanField(this) {
    override def displayName = "Allow Comments?"
  }
  object only_approved_comments extends BooleanField(this) {
    override def displayName = "Only Approved Comments?"
  }
  object can_vote extends BooleanField(this) {
    override def displayName = "Can Vote?"
  }
  object can_view_ratings extends BooleanField(this) {
    override def displayName = "Can View Ratings?"
  }
  object is_private extends BooleanField(this) {
    override def displayName = "Is Private?"
  }
  object created extends DateTimeField(this)
  object updated extends DateTimeField(this)
  object author_created extends StringField(this, 200)
  object author_updated extends StringField(this, 200)
  
  /*
  object SettingTypes extends Enumeration {
    val Option1         = new Val("Option 1")
    val Option2         = new Val("Option 2")
  }
  
  object MethodTypes extends Enumeration {
    val Option1         = new Val("Option 1")
    val Option2         = new Val("Option 2")
  }
  
  object CategoryTypes extends Enumeration {
    val Option1         = new Val("Option 1")
    val Option2         = new Val("Option 2")
  }
  */
  
}