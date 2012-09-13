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
import code.model.{User}
import code.lib.{FacebookProviderCustom}

//import omniauth.lib.{TwitterProvider, FacebookProvider}
import omniauth.lib._
import omniauth.Omniauth;

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
      Menu.i("Home") / "index" // the simple way to declare a menu
    ) ::: Omniauth.sitemap ::: User.sitemap
    
    //println(Omniauth.sitemap)
    //println(entries)
    
	//Set sitemap
    //LiftRules.setSiteMapFunc(() => User.sitemapMutator(SiteMap(entries:_*)))

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
	Omniauth.initWithProviders(List(new FacebookProvider("279073072192775", "52b1db374376abca115dd031cc2adcab")))
	
	// What is the function to test if a user is logged in?
    LiftRules.loggedInTest = Full(() => User.loggedIn_?)

  }
}
