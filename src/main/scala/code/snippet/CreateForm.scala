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

object CreateForm {
  
  def render = {
    
    var valid = true
    
    var fieldName = ""
    var fieldDescription = ""
    var fieldTags = ""
    var fieldPhoto = ""
    var fieldSetting = "1"
    var fieldMethod = "1"
    var fieldDeadline = ""
    var fieldLocation = ""
    var fieldCategory = "1"
    var fieldAllowComments = false
    var fieldApprovedCommentsOnly = false
    var fieldUsersVote = false
    var fieldViewRatings = false
    var fieldPrivate = false
    
    val settings = List("1" -> "Setting 1", "2" -> "Setting 2")
    val categories = List("1" -> "Category 1", "2" -> "Category 2")
    val methods = List("1" -> "Method 1", "2" -> "Method 2")

    def process(): JsCmd = {
      
      Thread.sleep(400) 
      
      valRequired(fieldName, "Name")
      valRequired(fieldDescription, "Description")
      valRequired(fieldTags, "Tags")
      //!valRequired(fieldPhoto) ||
      valRequired(fieldSetting, "Setting")
      valRequired(fieldMethod, "Method")
      valRequired(fieldCategory, "Category")
      valMin(fieldName, 3, "Name")
      valMax(fieldName, 128, "Name")
      valMax(fieldDescription, 500, "Description")
      valMax(fieldPhoto, 500, "Photo")
      valIsIn(fieldSetting, settings, "Settings")
      valIsIn(fieldCategory, categories, "Categories")
      valIsIn(fieldMethod, methods, "Methods")
      
      if (valid) {
        val date = Calendar.getInstance()
        val record = Achievement.createRecord
            .title(fieldName)
            .description(fieldDescription)
            .tags(fieldTags)
            .image(fieldPhoto)
            .setting(fieldSetting)
            .method(fieldMethod)
            .location(fieldLocation)
            .category(fieldCategory)
            .deadline(date:java.util.Calendar)
            .allow_comments(fieldAllowComments)
            .only_approved_comments(fieldApprovedCommentsOnly)
            .can_vote(fieldUsersVote)
            .can_view_ratings(fieldViewRatings)
            .is_private(fieldPrivate)
            .created(date:java.util.Calendar)
            .updated(date:java.util.Calendar)
            .author_created("AuthorCreated")
            .author_updated("AuthorUpdated")
            .save
        RedirectTo("/gc?"+record.id)
      }
      
      else {
        var pass = valid
        valid = true
        if (pass) SetHtml(LiftRules.noticesContainerId, Text(""))
      }
    }
    
    def valRequired(v: String, m: String): Boolean = {
      if (v.length == 0) {
        S.error(m+": field is required")
        valid = false 
      }
      valid
    }
    
    def valMin(v: String, i: Int, m: String): Boolean = {
      if (v.length < i) {
        S.error(m+": field needs to be at least "+i+" characters")
        valid = false
      }
      valid
    }
    
    def valMax(v: String, i: Int, m: String): Boolean = {
      if (v.length > i) {
        S.error(m+": field can be no longer than "+i+" characters")
        valid = false
      }
      valid
    }
    
    def valIsIn(v: String, l: List[net.liftweb.util.Helpers.TheStrBindParam], m: String): Boolean = {
      var found = false
      l.foreach( c => {
        if (c._1 == v) found = true
      })
      if (!found) {
        S.error(m+": invalid value")
        valid = false
      }
      valid
    }
    
    def doName(msg: NodeSeq) = {
      fieldWrap("name", "Name", 
        SHtml.ajaxText(fieldName, v => { 
          println("name:" + v)
          fieldName = v
        }, "maxlength" -> "128", "placeholder" -> "Name"))
    }
    
    def doDescription(msg: NodeSeq) = {
      fieldWrap("description", "Description",
        SHtml.ajaxTextarea(fieldDescription, v => {
          println("description:" + v)
          fieldDescription = v
        }, "maxlength" -> "500", "placeholder" -> "Description"))
    }
    
    def doTags(msg: NodeSeq) = {
      fieldWrap("tags", "Tags", 
        SHtml.ajaxText(fieldTags, v => {
          println("tags:" + v)
          fieldTags = v
        }, "placeholder" -> "Tags"))
    }
    
    def doSetting(msg: NodeSeq) = {
      fieldWrap("setting", "Setting", 
        SHtml.ajaxSelect(settings, Full(fieldSetting), v => {
          println("setting:" + v)
          fieldSetting = v 
        }))
    }
    
    def doMethod(msg: NodeSeq) = {
      fieldWrap("method", "Method",
        SHtml.ajaxSelect(methods, Full(fieldMethod), v => {
          println("method:" + v)
          fieldMethod = v
        }))
    }
    
    def doDeadline(msg: NodeSeq) = {
      fieldWrap("deadline", "Deadline",
        SHtml.ajaxText(fieldDeadline, v => {
          println("deadline:" + v)
          fieldDeadline = v 
        }, "maxlength" -> "128", "placeholder" -> "Deadline"))
    }
    
    def doPhoto(msg: NodeSeq) = {
      fieldWrap("photo", "Photo", <input type="filepicker-dragdrop"></input>)
    }
    
    def doLocation(msg: NodeSeq) = {
      fieldWrap("location", "Location",
        SHtml.ajaxText(fieldLocation, v => {
          println("location:" + v)
          fieldLocation = v 
        }, "maxlength" -> "128", "placeholder" -> "Location"))
    }
    
    def doCategory(msg: NodeSeq) = {
      fieldWrap("category", "Category",
        SHtml.ajaxSelect(categories, Full(fieldCategory), v => {
          println("category: " + v)
          fieldCategory = v 
        }) ++ SHtml.hidden(process))
    }
    
    def doAllowComments(msg: NodeSeq) = {
      fieldWrap("allow-comments", "Allow Comments",
        SHtml.ajaxCheckbox(fieldAllowComments, v => {
          println("allow comments:" + v)
          fieldAllowComments = v
        }))
    }
    
    def doApprovedOnly(msg: NodeSeq) = {
      fieldWrap("approved-comments-only", "Approved Comments Only",
        SHtml.ajaxCheckbox(fieldApprovedCommentsOnly, v => {
          println("approved only:" + v)
          fieldApprovedCommentsOnly = v
        }))
    }
    
    def doUsersVote(msg: NodeSeq) = {
      fieldWrap("users-vote", "Users Can Vote on Comments",
        SHtml.ajaxCheckbox(fieldUsersVote, v => {
          println("users vote:" + v)
          fieldUsersVote = v
        }))
    }
    
    def doViewRatings(msg: NodeSeq) = {
      fieldWrap("view-ratings", "Users Can View Ratings",
        SHtml.ajaxCheckbox(fieldViewRatings, v => {
          println("view ratings:" + v)
          fieldViewRatings = v
        }))
    }
    
    def doPrivate(msg: NodeSeq) = {
      fieldWrap("private", "This is a Private GoCheivement",
        SHtml.ajaxCheckbox(fieldPrivate, v => {
          println("private:" + v)
          fieldPrivate = v
        }))
    }
    
    ".field-name" #> doName _ &
    ".field-description" #> doDescription _ &
    ".field-tags" #> doTags _ &
    ".field-photo" #> doPhoto _ &
    ".field-setting" #> doSetting _ &
    ".field-method" #> doMethod _ &
    ".field-deadline" #> doDeadline _ &
    ".field-location" #> doLocation _ &
    ".field-allow-comments" #> doAllowComments _ &
    ".field-approved-comments-only" #> doApprovedOnly _ &
    ".field-users-vote" #> doUsersVote _ &
    ".field-view-ratings" #> doViewRatings _ &
    ".field-private" #> doPrivate _ &
    ".field-category" #> doCategory _
    
  }
  
  def fieldWrap(name: String, label: String, in: NodeSeq): NodeSeq = {
    <div class={"field field-"+name}>
      <label>{label}: </label>
      <div class="item">{in}</div>
    </div> 
  }
  
}