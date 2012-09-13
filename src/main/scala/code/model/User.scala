package code.model

//import net.liftweb.record._
import net.liftweb.record.field._
import net.liftweb.mongodb._
import net.liftweb.mongodb.record._
import net.liftweb.mongodb.record.field._
import net.liftweb.util._
import net.liftweb.common._
import net.liftweb.sitemap._
import code.lib._

/**
* The singleton that has methods for accessing the database
*/
object User extends User with MongoMetaRecord[User] with MetaMegaProtoUser[User] {
  
  override def screenWrap = Full(
      <lift:surround with="default" at="content">
        <div class="wrap user-screens">
		  <div class="wrap-inside">
            <div class="lift:Msgs?showAll=true"></div>
		    <lift:bind></lift:bind>
		  </div>
        </div>
      </lift:surround>
    )

  override def skipEmailValidation = true
      
  override val basePath = List("user")
      
  override def createUserMenuLoc = Empty
             
}

/**
* An O-R mapped "User" class that includes first name, last name, password and we add a "Personal Essay" to it
*/
class User private() extends MongoRecord[User] with MegaProtoUser[User] {
	def meta = User
	
	lazy val fbid = new FacebookIdField(meta)
	
	protected class FacebookIdField(obj: User) extends StringField(this, 12)
	
	protected def findUserByFbId(fbId: String): Box[User] = {
    var search = User.findAll("fbid", fbId).headOption
    search match {
      case Some(x) => Full(x)
      case None => return Empty
    }
  }
	
}
