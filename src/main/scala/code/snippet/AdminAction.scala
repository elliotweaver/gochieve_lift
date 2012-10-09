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
import com.foursquare.rogue.Rogue._
import org.bson.types.ObjectId
import java.text.SimpleDateFormat

import code.model.{Action}

object AdminAction {
  
  def render = {
    val all = Action.fetch()
    ".results *" #> all.map( x => {
        ".type *" #> x.c_type &
        ".uid *" #> x.uid &
        ".gcid *" #> x.gcid &
        ".options *" #> <span>
             <a href={"action/view?id=" + x.id.is.toString}>view</a> | 
             <a href={"action/form?id=" + x.id.is.toString}>edit</a> | 
             <a href={"action/delete?id=" + x.id.is.toString}>delete</a> 
           </span>
      })
  }
}



object AdminActionDelete {
  
  def render = {
    
    var hasId = false
    var id = ""
    
    S.param("id") match {
      case Full(x) => {
        hasId = true
        id = x
      }
      case _ => println("no pass")
    }
    
    def process(): JsCmd = {
      Action.where(_.id eqs new ObjectId(id)).findAndDeleteOne()
      RedirectTo("/admin/action")
    }
    
    ".process" #> SHtml.hidden(process) 
    
  }
  
}

object AdminActionView {
  
  def render = {
    
    var hasId = false
    var id = ""
    var fieldCtype = ""
    var fieldUid = ""
    var fieldGcid = ""
    var fieldCreated = ""
    var fieldUpdated = ""
      
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
      val data = Action.where(_.id eqs new ObjectId(c)).fetch()
      data match {
        case List(x) => {
          fieldCtype = x.c_type.toString
          fieldUid = x.uid.toString
          fieldGcid = x.gcid.toString
          fieldCreated = x.created.valueBox.map(s => s.getTime.toString) openOr ""
          fieldUpdated = x.updated.valueBox.map(s => s.getTime.toString) openOr ""
        }
        case _ => println("no data")
      }
    }
    
    ".t-id" #> id &
    ".t-ctype" #> fieldCtype &
    ".t-uid" #> fieldUid &
    ".t-gcid" #> fieldGcid &
    ".t-created" #> fieldCreated &
    ".t-updated" #> fieldUpdated
    
  }
  
}

object AdminActionForm {
  
  def render = {
    
    var valid = true
    
    var hasId = false
    var id = ""
    var fieldCtype = ""
    var fieldUid = ""
    var fieldGcid = ""
      
    S.param("id") match {
      case Full(x) => {
        loadData(x)
        hasId = true
        id = x
      }
      case _ => println("no pass")
    }
    
    def loadData(c: String) = {
      val data = Action.where(_.id eqs new ObjectId(c)).fetch()
      data match {
        case List(x) => {
          fieldCtype = x.c_type.toString
          fieldUid = x.uid.toString
          fieldGcid = x.gcid.toString
        }
        case _ => println("no data")
      }
    }
      
    def process(): JsCmd = {
      Thread.sleep(400) 
      
      valRequired(fieldCtype, "Type")
      valRequired(fieldUid, "Uid")
      valRequired(fieldGcid, "Gcid")
      valMax(fieldCtype, 128, "Type")
      valMax(fieldUid, 128, "Uid")
      valMax(fieldGcid, 128, "Gcid")
      
      if (valid) {
        val date = Calendar.getInstance()
        println("valid")
        hasId match {
          case true => {
            Action.where(_.id eqs new ObjectId(id))
                .modify(_.c_type setTo fieldCtype)
                .and(_.uid setTo fieldUid)
                .and(_.gcid setTo fieldGcid)
                .and(_.updated setTo date)
                .updateOne()
          }
          case false => {
            val q = Action.createRecord
                .uid(fieldUid)
                .c_type(fieldCtype)
                .gcid(fieldGcid)
                .save
            id = q.id.is.toString
          }
        }
        RedirectTo("/admin/action")
      }
      
      else {
        var pass = valid
        valid = true
        println("invalid")
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
    
    def doCtype(msg: NodeSeq) = {
      fieldWrap("ctype", "Type",
        SHtml.ajaxText(fieldCtype, v => {
          println("ctype:" + v)
          fieldCtype = v 
        }, "maxlength" -> "128", "placeholder" -> "Type"))
    }
    
    def doUid(msg: NodeSeq) = {
      fieldWrap("uid", "Uid",
        SHtml.ajaxText(fieldUid, v => {
          println("uid:" + v)
          fieldUid = v 
        }, "maxlength" -> "128", "placeholder" -> "Uid"))
    }
    
    def doGcid(msg: NodeSeq) = {
      fieldWrap("gcid", "Gcid",
        SHtml.ajaxText(fieldGcid, v => {
          println("gcid:" + v)
          fieldGcid = v 
        }, "maxlength" -> "128", "placeholder" -> "Gcid") ++ SHtml.hidden(process) )
    }
    
    def doId(msg: NodeSeq) = {
      if (hasId) <p>Id: {id}</p>
      else NodeSeq.Empty
    }
    
    ".field-id" #> doId _ &
    ".field-c-type" #> doCtype _ &
    ".field-uid" #> doUid _ &
    ".field-gcid" #> doGcid _
    
  }
  
  def fieldWrap(name: String, label: String, in: NodeSeq): NodeSeq = {
    <div class={"field field-"+name}>
      <label>{label}: </label>
      <div class="item">{in}</div>
    </div> 
  }
  
}