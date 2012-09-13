package code.view

import omniauth.Omniauth
import omniauth.AuthInfo
import dispatch._
import xml.{ Text, NodeSeq }
import net.liftweb.common._
import net.liftweb.http._
import code.model._

class Auth extends LiftView with Loggable {

  override def dispatch = {
    case "success" => doSuccess _
    case "failure" => doFailure _
  }
  
  def createNewUser(auth: AuthInfo): Boolean = {
    println("createNewUser")
    //User.createRecord
    true
  }
  
  def loginUser(record: User): Boolean = {
    println("loginUser")
    true
  }
  
  def processUser(auth: AuthInfo): Boolean = {
    val search = User.findAll("fbid", auth.uid).headOption
    search match {
      case Some(record) => loginUser(record)
      case None => createNewUser(auth)
    }
  }

  def doSuccess: NodeSeq = {
    val success = Omniauth.currentAuth match {
      case Full(omni) => processUser(omni)
      case Empty => doFailure
      case Failure(_,_,_) => doFailure
    }
    if (success == true) {
      S.notice("Authentication Success");
      S.redirectTo("/")  
    }
    doFailure
  }

  def doFailure: NodeSeq = {
    S.notice("Authentication Failed");
    S.redirectTo("/")
  }

}