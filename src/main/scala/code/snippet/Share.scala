package code
package snippet

import net.liftweb._
import util._
import Helpers._
import sitemap._

import model.{Achievement}

object Share {
  //lazy val menu = Menu.param[Share]("Share", "Share", Achievement.find, _.id.toString) / "share" / *
}

case class Share(pageName: String)