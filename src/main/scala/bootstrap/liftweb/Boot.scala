package bootstrap.liftweb

import net.liftweb._
import util._
import Helpers._
import common._
import http._
import sitemap._
import Loc._
import net.liftmodules.JQueryModule
import net.liftweb.http.js.jquery._
import code.db.{MongoConfig}
import code.model._
import omniauth.lib._
import omniauth.Omniauth
import net.liftweb.widgets.autocomplete.AutoComplete

import code.snippet.{Share}

object MenuGroups {
  val SettingsGroup = LocGroup("settings")
  val AdminGroup = LocGroup("admin")
  val TopBarGroup = LocGroup("topbar")
}

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot extends Loggable {
  def boot {
    
    // where to search snippet
    LiftRules.addToPackages("code")

    // Build SiteMap
    val entries = List(
      Menu.i("Home") / "index",
      Menu.i("Facebook Success") / "auth" / "success" >> Hidden,
      Menu.i("Facebook Failure") / "auth" / "failure" >> Hidden,
      Menu.i("Create") / "create",
      Menu.i("View") / "gc",
      Menu.i("Share") / "share",
      Menu.i("Profile") / "profile",
      Menu.i("Account") / "account"
      //Menu.i("Share GoChieve") / "share"
    ) ::: Omniauth.sitemap ::: User.sitemap ::: Achievement.menus ::: File.menus ::: FieldOption.menus

    // set the sitemap.  Note if you don't want access control for
    // each page, just comment this line out.
    LiftRules.setSiteMap(SiteMap(entries:_*))

    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)
    
    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    // Use HTML5 for rendering
    LiftRules.htmlProperties.default.set((r: Req) => new Html5Properties(r.userAgent))

    //Init the jQuery module, see http://liftweb.net/jquery for more information.
    LiftRules.jsArtifacts = JQueryArtifacts
    JQueryModule.InitParam.JQuery = JQueryModule.JQuery172
    JQueryModule.init()
    
    //Mongo config init
    MongoConfig.init
      
    //Omni Facebook init
	  Omniauth.init
	  
	  AutoComplete.init
	
	  // What is the function to test if a user is logged in?
    LiftRules.loggedInTest = Full(() => User.loggedIn_?)

  }
}
