package code.view

import omniauth.Omniauth
import omniauth.AuthInfo
import dispatch._
import xml.{ Text, NodeSeq }
import net.liftweb.common._
import net.liftweb.http._
import code.model._
import net.liftweb.json._
import net.liftweb.json.JsonParser._
import com.restfb._

class Auth extends LiftView with Loggable {

  override def dispatch = {
    case "success" => doSuccess _
    case "failure" => doFailure _
  }

  def doSuccess: NodeSeq = {
    val success = Omniauth.currentAuth match {
      case Full(omni) => User.processUser(omni)
      case Empty => doFailure
      case Failure(_,_,_) => doFailure
    }
    if (success == true) {
      S.notice("Authentication Success")
      S.redirectTo("/")  
    }
    doFailure
  }

  def doFailure: NodeSeq = {
    S.notice("Authentication Failed")
    S.redirectTo("/")
  }

}