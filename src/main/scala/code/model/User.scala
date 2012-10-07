package code.model

import net.liftweb.record.field._
import net.liftweb.mongodb._
import net.liftweb.mongodb.record._
import net.liftweb.mongodb.record.field._
import net.liftweb.http.{SessionVar}
import net.liftweb.util._
import net.liftweb.common._
import net.liftweb.sitemap._
import code.lib._
import omniauth.AuthInfo
import net.liftweb.json._
import com.restfb._

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
* An O-R mapped "User" class
*/
class User private() extends MongoRecord[User] with MegaProtoUser[User] {
	def meta = User
	
	object fbid extends StringField(this, 100)
	object fbtoken extends StringField(this, 100)
	object created extends DateTimeField(this)
  object updated extends DateTimeField(this)
  object author_updated extends StringField(this, 200)
	
	private object fbClient extends SessionVar[Box[FacebookClient]](Empty)
	def facebookClient: Box[FacebookClient] = fbClient.is
  def setFacebookClient(fb: FacebookClient){ fbClient(Full(fb)) }
	
	private object fbProfile extends SessionVar[Box[FbProfile]](Empty)
	def facebookProfile: Box[FbProfile] = fbProfile.is
  def setFacebookProfile(fb: FbProfile){ fbProfile(Full(fb)) }
	
	def findUserByFbId(fbId: String): Box[User] = {
    var search = meta.findAll("fbid", fbId).headOption
    search match {
      case Some(x) => Full(x)
      case None => return Empty
    }
  }
	
	def getFb(token: String): Unit = {
	  getFbClient(token)
    getFbData
	}
	
	def getFbClient(token: String): Unit = setFacebookClient(new DefaultFacebookClient(token))
	
	def getFbData: Unit = {
	  implicit val formats = net.liftweb.json.DefaultFormats
	  facebookClient match {
      case Full(client) => setFacebookProfile(parse(client.fetchObject("me", classOf[com.restfb.json.JsonObject]).toString).extract[FbProfile])
      case Empty => Empty
      case Failure(_,_,_) => Empty
    }
	}
	
	def createNewUser(auth: AuthInfo): Unit = facebookProfile match {
      case Full(profile) => {
        meta.createRecord
          .email(profile.email)
          //.timezone(User.fbProfile.timezone)
          .validated(profile.verified)
          .locale(profile.locale)
          .lastName(profile.last_name)
          .firstName(profile.first_name)
          .superUser(false)
          .fbid(profile.id)
          .fbtoken(auth.token)
          .save
        meta.logUserIn(meta)
        true
      } 
      case Empty => false
      case Failure(_,_,_) => false
    }
	
	def loginUser(record: User): Unit = meta.logUserIdIn(record.id.toString)
	
	def processUser(auth: AuthInfo): Unit = {
    getFb(auth.token) 
    meta.findUserByFbId(auth.uid) match {
      case Full(record) => loginUser(record)
      case _ => createNewUser(auth)
    }
  }
	
}

case class Hometown(
    id:                 Option[String]   = None,
    name:               Option[String]   = None)
case class FbProfile(
    hometown:           Option[Hometown] = None,
    link:               Option[String]   = None,
    locale:             Option[String]   = None,
    updated_time:       Option[String]   = None, 
    id:                 Option[String]   = None,
    first_name:         Option[String]   = None,
    username:           Option[String]   = None,
    timezone:           Option[Int]      = None, 
    email:              Option[String]   = None,
    verified:           Option[Boolean]  = None,
    name:               Option[String]   = None,
    last_name:          Option[String]   = None, 
    gender:             Option[String]   = None)

    
/*
{"hometown":{"id":"106277849402612","name":"Santa Cruz, California"},"link":"http://www.facebook.com/ewcalma","locale":"en_US",
  "updated_time":"2012-08-06T19:16:39+0000","id":"6714993","first_name":"Elliot","username":"ewcalma","timezone":-4,
  "email":"elliot@oxygenproductions.com","verified":true,"name":"Elliot Weaver","last_name":"Weaver","gender":"male"}
*/