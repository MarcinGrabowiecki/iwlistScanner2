package marcin

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
 * Created by m on 2015-08-23.
 */
class CSVPrinter {
  var addresses=new mutable.HashMap[String,Int]()
  var printOnce=true
  var dateLong=0L
  var cells=new ListBuffer[Cell]()
  def printt(cell:Cell)={
    if(printOnce){
      CSV.ads.foreach(address=>print(s";${address}"))
      printOnce=false
    }
    if(addresses.contains(""+cell.getField("Address"))){
      addresses.put(""+cell.getField("Address"),addresses.get(""+cell.getField("Address")).get+1)
    } else {
      addresses.put(""+cell.getField("Address"),1)
    }

    val dl=cell.getOptionalField("scanDateLong").getOrElse(-1L).asInstanceOf[Long]
    cells+=cell
    if(dateLong!=dl){
      print(s"${dl};")
      CSV.ads.foreach(address=>
          print(
            cells.find(c=>c.getField("Address")==address).map(c=>c.getField("Quality")).getOrElse("")+";"
          )
      )
      println()
      dateLong=dl
      cells=new ListBuffer[Cell]()
    }
  }
}
