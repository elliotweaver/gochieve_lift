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

object GcInteract {
  
  def render = {
    
    var valid = true
    
    var hasId = false
    var id = ""
    var fieldSubject = ""
    var fieldMessage = ""
    var fieldLink = ""
    var fieldVideo = ""
    var fieldImage = ""
    
    val dateFormat = new SimpleDateFormat("MM/dd/yyyy")
    
    S.param("id") match {
      case Full(x) => {
        hasId = true
        id = x
      }
      case _ => println("no pass")
    }

    def processMessage(): JsCmd = {
      Thread.sleep(400)
      println("process message")
    }
    
    def processLink(): JsCmd = {
      Thread.sleep(400)
      println("process link")
    }
    
    def processVideo(): JsCmd = {
      Thread.sleep(400)
      println("process video")
    }
    
    def processImage(): JsCmd = {
      Thread.sleep(400)
      println("process image")
    }
    
    def doAddMessage(msg: NodeSeq) = {
      <div class="field field-add-message">
        <label>Add Message: </label>
        <div class="item">{SHtml.ajaxTextarea(fieldMessage, v => { 
          println("message:" + v)
          fieldMessage = v
        }, "maxlength" -> "128", "placeholder" -> "Message")}{SHtml.hidden(processMessage)}</div>
      </div>
    }
    
    def doAddLink(msg: NodeSeq) = {
      <div class="field field-add-link">
        <label>Add Link: </label>
        <div class="item">{SHtml.ajaxText(fieldLink, v => { 
          println("link:" + v)
          fieldLink = v
        }, "maxlength" -> "128", "placeholder" -> "Link")}{SHtml.hidden(processLink)}</div>
      </div>
    }
    
    def doAddVideo(msg: NodeSeq) = {
      <div class="field field-add-video">
        <label>Add Video: </label>
        <div class="item">{SHtml.ajaxText(fieldVideo, v => { 
          println("video:" + v)
          fieldVideo = v
        }, "maxlength" -> "128", "placeholder" -> "Video")}{SHtml.hidden(processVideo)}</div>
      </div>
    }
    
    def doAddImage(msg: NodeSeq) = {
      <div class="field field-add-image">
        <label>Add Image: </label>
        <div class="item">{SHtml.ajaxText(fieldImage, v => { 
          println("image:" + v)
          fieldImage = v
        }, "maxlength" -> "128", "placeholder" -> "Image")}{SHtml.hidden(processImage)}</div>
      </div>
    }
    
  
    ".add-message" #> doAddMessage _ &
    ".add-link" #> doAddLink _ &
    ".add-video" #> doAddVideo _ &
    ".add-image" #> doAddImage _
    
  }
  
}