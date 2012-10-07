package code
package snippet

import net.liftweb._
import http._
import common._
import util.Helpers._
import js._
import JsCmds._
import JE._
import scala.xml.{NodeSeq, Text}
import java.util.{Calendar, Date}
import code.lib._

import code.model.{Achievement}

class GcView {
  
  var id: Box[String] = Empty
  
  def startInit(in: NodeSeq) = {
    setId
    NodeSeq.Empty
  }
  
  def setId = {
    id = S.param("id")
  }
  
  def viewBtnBucketlist = {
    <div class="add-to-bucketlist">Add to Bucketlist</div>
  }
  
  def viewBtnClaimThis = {
    <div class="claim-this">Claim This or Don't</div>
  }
  
  def viewTitle(xhtml: NodeSeq) = <h1>This is a title</h1>
    
  def viewDetails(xhtml: NodeSeq) = {
    <div class="details">
    {viewDescription}
    {viewCategory}
    {viewTags}
    {viewLocation}
    </div>
  } 
  
  def viewInteract = {
    <div class="interact">
      <h2>Interaction Area</h2>
      <div>The interaction widget goes here. Things like add comments, image, link, etc.</div>
    </div>
  }
  
  def viewActivity = {
    <div class="activity">
      <h2>Activities Area</h2>
      <div>Activity List view area goes here.</div>
    </div>
  }
  
  def viewPostedBy =
    <div class="posted-by">
      Posted By: Elliot Weaver :D
    </div>
  
  def viewSettings = 
    <div class="settings">
      Public : Honor System : no time limit
    </div>
  
  def viewRelated = 
    <div class="related">
      <h2>Related GoChievements</h2>
      <div>Here are some related GoChievements!</div>
    </div>
    
  def viewShareInvite = 
    <div class="share-invite">
      <h2>Share/Invite your friends</h2>
      <div>Share/invite with your friends</div>
    </div>
  
  def viewPhoto = 
    <div class="photo">
      <img src="http://www.google.com/images/srpr/logo3w.png"></img>
    </div>
  
  def viewDescription = 
    <div class="item description">
      <h3>Description</h3> 
      <span class="about">Lorem ipsum about this description</span>
    </div>
    
  def viewCategory = 
    <div class="item category">
      <h3>Category</h3> 
      <span class="about">Lorem ipsum about this category</span>
    </div>
    
  def viewTags = 
    <div class="item tags">
      <h3>Tags</h3> 
      <span class="about">Lorem ipsum about these tags</span>
    </div>
    
  def viewLocation = 
    <div class="location">
      <h3>Location</h3> 
      <span class="about">Lorem ipsum about this location lorem ipsum</span>
    </div>
    
  def viewGochieves = 
    <div class="gochieves">
      <span class="num">293,102</span> 
      <span class="name">GoChieves</span>
    </div>
    
  def viewBucketlists = 
    <div class="bucketlists">
      <span class="num">10,652</span> 
      <span class="name">Added to Bucketlist</span>
    </div>
    
  def viewLikes = 
    <div class="likes">
      <span class="num">40,243</span> 
      <span class="name">Liked this</span>
    </div>
  
}