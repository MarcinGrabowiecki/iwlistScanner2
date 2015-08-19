package marcin

import java.util

import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility
import org.codehaus.jackson.annotate.JsonMethod
import org.codehaus.jackson.map.ObjectMapper

import scala.collection.mutable.ListBuffer

/**
 * Created by m on 2015-05-29.
 */
class Cell(printer:Printer) {
  var counter=0;
  private val fields=new util.HashMap[String,Object]();
  val mapper=new ObjectMapper()
  mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
  def write={
    compress
    printer.print(mapper.writeValueAsString(fields))
  }
  def compress={
    compressBitRates("BitRates");
    compressBitRates("BitRates1");
  }

  def compressBitRates(br:String)={
    if(fields.get(br)!=null) {
      fields.put(br,
        fields.get(br)
          .toString
          .replaceAll(" Mb.s", "")
          .replaceAll(" ","")
      )
    }
  }

  def getField(key:String):Object={
    return fields.get(key)
  }

  def addField(key:String,value:String): Cell ={
    counter+=1
    //println(s"${key} : ${value}")
    key match {
      case "Frequency" => fields.put(key,value.toString.substring(0,5))
      case "ESSID" => fields.put(key,value.substring(1,value.length-1))
      case "Extra: Last beacon" => fields.put("LastBeacon",value.substring(1,value.length-6))
      case "Extra" => fields.put("tsf",value.substring(4))
      case "Unknown" =>
        //fields.put(key+"-"+counter,value)
      case "BitRates" => {
        if (fields.containsKey(key)) fields.put(key + 1, value) else fields.put(key, value)
      }
      case "BitRatesCont" => fields.put("BitRates", fields.get("BitRates")+"; "+value)
      case _ => fields.put(key,value)
    }
    this
  }
}
