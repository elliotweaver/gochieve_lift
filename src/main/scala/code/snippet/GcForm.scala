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
import com.foursquare.rogue.Rogue._
import java.text.SimpleDateFormat

import code.model.{Achievement, Taxonomy}

object GcForm {
  
  def render = {
    
    var valid = true
    
    var hasId = false
    var id = ""
    var fieldName = ""
    var fieldDescription = ""
    var fieldTags = ""
    var fieldPhoto = ""
    var fieldSetting = ""
    var fieldMethod = ""
    var fieldDeadline = ""
    var fieldLocation = ""
    var fieldCategory = ""
    var fieldAllowComments = false
    var fieldApprovedCommentsOnly = false
    var fieldUsersVote = false
    var fieldViewRatings = false
    var fieldPrivate = false
    
    val settings = Taxonomy.getSettings
    val categories = Taxonomy.getCategories
    val methods = Taxonomy.getMethods
    
    val dateFormat = new SimpleDateFormat("MM/dd/yyyy")
    
    S.param("id") match {
      case Full(x) => {
        loadData(x)
        hasId = true
        id = x
      }
      case _ => println("no pass")
    }
    
    def loadData(c: String) = {
      val data = Achievement.where(_.id eqs new ObjectId(c)).fetch()
      data match {
        case List(x) => {
          fieldName = x.title.toString
          fieldDescription = x.description.toString
          fieldTags = x.tags.toString
          fieldPhoto = x.image.toString
          fieldSetting = x.setting.toString
          fieldMethod = x.method.toString
          fieldDeadline = x.deadline.valueBox.map(s => dateFormat.format(s.getTime)) openOr ""
          fieldLocation = x.location.toString
          fieldCategory = x.category.toString
          fieldAllowComments = x.allow_comments.value
          fieldApprovedCommentsOnly = x.only_approved_comments.value
          fieldUsersVote = x.can_vote.value
          fieldViewRatings = x.can_view_ratings.value
          fieldPrivate = x.is_private.value
        }
        case _ => println("no data")
      }
    }

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
      
      if (valid) {
        
        var valDeadline = Calendar.getInstance()
        valDeadline.setTime(dateFormat.parse(fieldDeadline))
        
        if (hasId) {
          Achievement.where(_.id eqs new ObjectId(id))
              .modify(_.title setTo fieldName)
              .and(_.description setTo fieldDescription)
              .and(_.tags setTo fieldTags)
              .and(_.image setTo fieldPhoto)
              .and(_.setting setTo fieldSetting)
              .and(_.method setTo fieldMethod)
              .and(_.location setTo fieldLocation)
              .and(_.category setTo fieldCategory)
              .and(_.deadline setTo valDeadline)
              .and(_.allow_comments setTo fieldAllowComments)
              .and(_.only_approved_comments setTo fieldApprovedCommentsOnly)
              .and(_.can_vote setTo fieldUsersVote)
              .and(_.can_view_ratings setTo fieldViewRatings)
              .and(_.is_private setTo fieldPrivate)
              .and(_.author_updated setTo "Author Updated")
              .updateOne
        }
        
        else {
          val record = Achievement.createRecord
            .title(fieldName)
            .description(fieldDescription)
            .tags(fieldTags)
            .image(fieldPhoto)
            .setting(fieldSetting)
            .method(fieldMethod)
            .location(fieldLocation)
            .category(fieldCategory)
            .deadline(valDeadline)
            .allow_comments(fieldAllowComments)
            .only_approved_comments(fieldApprovedCommentsOnly)
            .can_vote(fieldUsersVote)
            .can_view_ratings(fieldViewRatings)
            .is_private(fieldPrivate)
            .author_created("AuthorCreated")
            .author_updated("AuthorUpdated")
            .save
          id = record.id.is.toString
        }
        
        RedirectTo("/gc?id="+id)
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
    
    ".view-link" #> <a href={"/gc?id=" + id}>view</a> &
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