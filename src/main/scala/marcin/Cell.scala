package marcin

import java.util

import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility
import org.codehaus.jackson.annotate.JsonMethod
import org.codehaus.jackson.map.ObjectMapper

/**
 * Created by m on 2015-05-29.
 */
class Cell(printer:Printer) {

  var counter=0;

  private val fields=new util.HashMap[String,Object]();
  val o=new ObjectMapper()
  o.setVisibility(JsonMethod.FIELD, Visibility.ANY);
  def write={
    printer.print(o.writeValueAsString(fields))
  }
  def addField(key:String,value:String): Unit ={
    counter+=1
    if(key.equals("Frequency")){
      fields.put(key,value.toString.substring(0,5))
      return
    }
    if(key.equals("ESSID")){
      fields.put(key,value.substring(1,value.length-1))
      return
    }
    if(key.equals("Extra: Last beacon")){
      fields.put("LastBeacon",value.substring(1,value.length-6))
      return
    }
    fields.put(key,value)
  }

}

