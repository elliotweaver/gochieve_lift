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
import net.liftweb.mongodb.BsonDSL._

import com.foursquare.rogue.Rogue._

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
  
  def editLink = {
    id match {
      case Full(x) => <a href={"/edit?id=" + x}>edit</a>
      case _ => NodeSeq.Empty
    }
  }
  
  /* Bucket Button */
  
  def viewBtnBucketlist = {
    Action.getAction("bucket", uid, data.id.is.toString) match {
      case List(x) => btnUnbucket
      case _ => btnBucket
    }
  }
  
  def btnUnbucket: NodeSeq = <div id="btn-bucket">
    <div class="unbucket">{SHtml.ajaxButton("Remove from Bucketlist", () => {
        Action.getAction("bucket", uid, data.id.is.toString) match {
          case List(x) => removeBucket
          case _ => println("error: no bucket to remove")
        }
        SetHtml("btn-bucket", btnBucket)
      })}</div>
    </div>
  
  def btnBucket: NodeSeq = <div id="btn-bucket">
    <div class="bucket">{SHtml.ajaxButton("Add to Bucketlist", {() =>
        Action.getAction("bucket", uid, data.id.is.toString) match {
          case List(x) => println("error: already added to bucket")
          case _ => addBucket
        }
        SetHtml("btn-bucket", btnUnbucket)
      })}</div>
    </div>
  
  def addBucket = {
    Action.createAction("bucket", uid, data.id.is.toString)
    Achievement.update(("_id" -> new ObjectId(data.id.is.toString)), ("$inc" -> ("bucketlists" -> 1)))
  }
  
  def removeBucket = {
    Action.removeAction("bucket", uid, data.id.is.toString)
    Achievement.update(("_id" -> new ObjectId(data.id.is.toString)), ("$inc" -> ("bucketlists" -> -1)))
  }
  
  /* Claim Button */
  
  def viewBtnClaimThis = {
    Action.getAction("claim", uid, data.id.is.toString) match {
      case List(x) => btnUnclaim
      case _ => btnClaim
    }
  }
  
  def btnUnclaim: NodeSeq = <div id="btn-claim">
    <div class="unclaim">{SHtml.ajaxButton("Unclaim This", () => {
        Action.getAction("claim", uid, data.id.is.toString) match {
          case List(x) => removeClaim
          case _ => println("error: no claim to remove")
        }
        SetHtml("btn-claim", btnClaim)
      })}</div>
    </div>
  
  def btnClaim: NodeSeq = <div id="btn-claim">
    <div class="claim">{SHtml.ajaxButton("Claim This", {() =>
        Action.getAction("claim", uid, data.id.is.toString) match {
          case List(x) => println("error: already added claimed")
          case _ => addClaim
        }
        SetHtml("btn-claim", btnUnclaim)
      })}</div>
    </div>
  
  def addClaim = {
    Action.createAction("claim", uid, data.id.is.toString)
    Achievement.update(("_id" -> new ObjectId(data.id.is.toString)), ("$inc" -> ("gochieves" -> 1)))
  }
  
  def removeClaim = {
    Action.removeAction("claim", uid, data.id.is.toString)
    Achievement.update(("_id" -> new ObjectId(data.id.is.toString)), ("$inc" -> ("gochieves" -> -1)))
  }
  
  /* Like Button */
  
  def viewBtnLikeThis = {
    Action.getAction("like", uid, data.id.is.toString) match {
      case List(x) => btnUnlike
      case _ => btnLike
    }
  }
  
  def btnUnlike: NodeSeq = <div id="btn-like">
    <div class="unlike">{SHtml.ajaxButton("Unlike This", () => {
        Action.getAction("like", uid, data.id.is.toString) match {
          case List(x) => removeLike
          case _ => println("error: no like to remove")
        }
        SetHtml("btn-like", btnLike)
      })}</div>
    </div>
  
  def btnLike: NodeSeq = <div id="btn-like">
    <div class="like">{SHtml.ajaxButton("Like This", {() =>
        Action.getAction("like", uid, data.id.is.toString) match {
          case List(x) => println("error: already likes")
          case _ => addLike
        }
        SetHtml("btn-like", btnUnlike)
      })}</div>
    </div>
  
  def addLike = {
    Action.createAction("like", uid, data.id.is.toString)
    Achievement.update(("_id" -> new ObjectId(data.id.is.toString)), ("$inc" -> ("likes" -> 1)))
  }
  
  def removeLike = {
    Action.removeAction("like", uid, data.id.is.toString)
    Achievement.update(("_id" -> new ObjectId(data.id.is.toString)), ("$inc" -> ("likes" -> -1)))
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
      {data.getSetting} : {data.getMethod} : {data.getDeadline}
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
      <img src={data.image.toString}></img>
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
      <span class="about">{data.getTags}</span>
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
    
  }
  
  def viewActivity = {
    <div class="activity">
      <h2>Activities Area</h2>
      <div>Activity List view area goes here.</div>
    </div>
  }
  
}