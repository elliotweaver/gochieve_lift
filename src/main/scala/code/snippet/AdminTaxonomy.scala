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

import code.model.{Taxonomy}

object AdminTaxonomy {
  
  def render = {
    
    val all = Taxonomy.fetch()
    
    ".results *" #> all.map( x => {
        ".type *" #> x.c_type &
        ".name *" #> x.name &
        ".machine *" #> x.machine &
        ".options *" #> <span>
             <a href={"taxonomy/view?id=" + x.id.is.toString}>view</a> | 
             <a href={"taxonomy/form?id=" + x.id.is.toString}>edit</a> | 
             <a href={"taxonomy/delete?id=" + x.id.is.toString}>delete</a> 
           </span>
      })
    
  }
  
}

object AdminTaxonomyDelete {
  
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
      Taxonomy.where(_.id eqs new ObjectId(id)).findAndDeleteOne()
      RedirectTo("/admin/taxonomy")
    }
    
    ".process" #> SHtml.hidden(process) 
    
  }
  
}

object AdminTaxonomyView {
  
  def render = {
    
    var hasId = false
    var id = ""
    var fieldCtype = ""
    var fieldName = ""
    var fieldMachine = ""
    var fieldWeight = ""
    
    S.param("id") match {
      case Full(x) => {
        loadData(x)
        hasId = true
        id = x
      }
      case _ => println("no pass")
    }
    
    def loadData(c: String) = {
      val data = Taxonomy.where(_.id eqs new ObjectId(c)).fetch()
      data match {
        case List(x) => {
          fieldCtype = x.c_type.toString
          fieldName = x.name.toString
          fieldMachine = x.machine.toString
          fieldWeight = x.weight.toString
        }
        case _ => println("no data")
      }
    }
    
    ".t-id" #> id &
    ".t-ctype" #> fieldCtype &
    ".t-name" #> fieldName &
    ".t-machine" #> fieldMachine &
    ".t-weight" #> fieldWeight
    
  }
  
}

object AdminTaxonomyForm {
  
  def render = {
    
    var valid = true
    
    var hasId = false
    var id = ""
    var fieldCtype = ""
    var fieldName = ""
    var fieldMachine = ""
    var fieldWeight = ""
    var fieldCreated = ""
    var fieldUpdated = ""
    var fieldAuthorCreated = ""
    var fieldAuthorUpdated = ""
      
    S.param("id") match {
      case Full(x) => {
        loadData(x)
        hasId = true
        id = x
      }
      case _ => println("no pass")
    }
    
    def loadData(c: String) = {
      val data = Taxonomy.where(_.id eqs new ObjectId(c)).fetch()
      data match {
        case List(x) => {
          fieldCtype = x.c_type.toString
          fieldName = x.name.toString
          fieldMachine = x.machine.toString
          fieldWeight = x.weight.toString
        }
        case _ => println("no data")
      }
    }
      
    def process(): JsCmd = {
      Thread.sleep(400) 
      
      valRequired(fieldCtype, "Type")
      valRequired(fieldName, "Name")
      valRequired(fieldMachine, "Machine")
      valMin(fieldName, 3, "Name")
      valMax(fieldCtype, 128, "Type")
      valMax(fieldName, 128, "Name")
      valMax(fieldMachine, 128, "Machine")
      
      if (valid) {
        val date = Calendar.getInstance()
        println("valid")
        hasId match {
          case true => {
            Taxonomy.where(_.id eqs new ObjectId(id))
                .modify(_.c_type setTo fieldCtype)
                .and(_.name setTo fieldName)
                .and(_.machine setTo fieldMachine)
                .and(_.weight setTo fieldWeight.toInt)
                .updateOne()
          }
          case false => {
            val q = Taxonomy.createRecord
                .name(fieldName)
                .c_type(fieldCtype)
                .machine(fieldMachine)
                .weight(fieldWeight.toInt)
                .save
            id = q.id.is.toString
          }
        }
        RedirectTo("/admin/taxonomy")
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
    
    def doName(msg: NodeSeq) = {
      fieldWrap("name", "Name",
        SHtml.ajaxText(fieldName, v => {
          println("name:" + v)
          fieldName = v 
        }, "maxlength" -> "128", "placeholder" -> "Name"))
    }
    
    def doMachine(msg: NodeSeq) = {
      fieldWrap("machine", "Machine",
        SHtml.ajaxText(fieldMachine, v => {
          println("machine:" + v)
          fieldMachine = v 
        }, "maxlength" -> "128", "placeholder" -> "Machine") ++ SHtml.hidden(process) )
    }
    
    def doWeight(msg: NodeSeq) = {
      fieldWrap("weight", "Weight",
        SHtml.ajaxText(fieldWeight, v => {
          println("weight:" + v)
          fieldWeight = v 
        }, "maxlength" -> "128", "placeholder" -> "Weight"))
    }
    
    def doId(msg: NodeSeq) = {
      if (hasId) <p>Id: {id}</p>
      else NodeSeq.Empty
    }
    
    ".field-id" #> doId _ &
    ".field-c-type" #> doCtype _ &
    ".field-name" #> doName _ &
    ".field-weight" #> doWeight _ &
    ".field-machine" #> doMachine _
    
  }
  
  def fieldWrap(name: String, label: String, in: NodeSeq): NodeSeq = {
    <div class={"field field-"+name}>
      <label>{label}: </label>
      <div class="item">{in}</div>
    </div> 
  }
  
}