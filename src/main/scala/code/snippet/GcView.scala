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
import org.bson.types.ObjectId

import code.model.{Achievement, Action, User}

class GcView {
  
  val uid = User.currentUserId match {
    case Full(x) => x
    case _ => "0"
  }
  var id: Box[String] = Empty
  var data: Achievement = Achievement
  
  def startInit(in: NodeSeq) = {
    setId
    id match {
      case Full(x) => loadData(x)
      case _ => println("No ID Found")
    }
    NodeSeq.Empty
  }
  
  def setId = {
    id = S.param("id")
  }
  
  def loadData(s: String) = {
    val q = Achievement.find("_id", new ObjectId(s))
    q match {
      case Full(x) => data = x
      case _ => println("No Record Found") 
    }
  }
  
  def btnUnbucket: NodeSeq = <div id="btn-bucket">
    <div class="unbucket">{SHtml.ajaxButton("Remove from Bucketlist", () => {
        println("Unbucketing it")
        SetHtml("btn-bucket", btnBucket)
      })}</div>
    </div>
  
  def btnBucket: NodeSeq = <div id="btn-bucket">
    <div class="bucket">{SHtml.ajaxButton("Add to Bucketlist", {() =>
        println("Bucket it")
        SetHtml("btn-bucket", btnUnbucket)
      })}</div>
    </div>
  
  def viewBtnBucketlist = {
    Action.getAction("bucket", uid, data.id.is.toString) match {
      case Full(x) => btnUnbucket
      case _ => btnBucket
    }
  }
  
  def btnUnclaim: NodeSeq = <div id="btn-claim">
    <div class="unclaim">{SHtml.ajaxButton("Unclaim This", () => {
        println("Unclaiming it")
        SetHtml("btn-claim", btnClaim)
      })}</div>
    </div>
  
  def btnClaim: NodeSeq = <div id="btn-claim">
    <div class="claim">{SHtml.ajaxButton("Claim This", {() =>
        println("Claiming it")
        SetHtml("btn-claim", btnUnclaim)
      })}</div>
    </div>
  
  def viewBtnClaimThis = {
    Action.getAction("claim", uid, data.id.is.toString) match {
      case Full(x) => btnUnclaim
      case _ => btnClaim
    }
  }
  
  def btnUnlike: NodeSeq = <div id="btn-like">
    <div class="unlike">{SHtml.ajaxButton("Unlike This", () => {
        println("Unliking it")
        SetHtml("btn-like", btnLike)
      })}</div>
    </div>
  
  def btnLike: NodeSeq = <div id="btn-like">
    <div class="like">{SHtml.ajaxButton("Like This", {() =>
        println("Liking it")
        SetHtml("btn-like", btnUnlike)
      })}</div>
    </div>
  
  def viewBtnLikeThis = {
    Action.getAction("like", uid, data.id.is.toString) match {
      case Full(x) => btnUnlike
      case _ => btnLike
    }
  }
  
  def viewTitle(xhtml: NodeSeq) = <h1>{data.title}</h1>
    
  def viewDetails(xhtml: NodeSeq) = {
    <div class="details">
    {viewDescription}
    {viewCategory}
    {viewTags}
    {viewLocation}
    </div>
  } 
  
  def viewPostedBy =
    <div class="posted-by">
      Posted By: {data.author_created}
    </div>
  
  def viewSettings = 
    <div class="settings">
      {data.getSetting} : {data.getMethod} : no time limit
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
      <span class="about">{data.description}</span>
    </div>
    
  def viewCategory = 
    <div class="item category">
      <h3>Category</h3> 
      <span class="about">{data.getCategory}</span>
    </div>
    
  def viewTags = 
    <div class="item tags">
      <h3>Tags</h3> 
      <span class="about">{data.tags}</span>
    </div>
    
  def viewLocation = 
    <div class="location">
      <h3>Location</h3> 
      <span class="about">{data.location}</span>
    </div>
    
  def viewGochieves = 
    <div class="gochieves">
      <span class="num">{data.gochieves}</span> 
      <span class="name">GoChieves</span>
    </div>
    
  def viewBucketlists = 
    <div class="bucketlists">
      <span class="num">{data.bucketlists}</span> 
      <span class="name">Added to Bucketlist</span>
    </div>
    
  def viewLikes = 
    <div class="likes">
      <span class="num">{data.likes}</span> 
      <span class="name">Liked this</span>
    </div>
    
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
  
}