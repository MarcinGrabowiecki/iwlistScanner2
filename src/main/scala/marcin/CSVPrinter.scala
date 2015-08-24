package marcin

import scala.collection.mutable.ListBuffer

/**
 * Created by m on 2015-08-23.
 */
class CSVPrinter {
  var dateLong=0L;
  var lb=new ListBuffer[Cell]()
  def printt(cell:Cell)={
    val dl=cell.getOptionalField("scanDateLong").getOrElse(0L).asInstanceOf[Long]
    lb+=cell
    if(dateLong!=dl){
      print(dl)
      print("\t")
      lb.map(c=>CSV.ads.map(h=>print(lb.find(c=>c.getField("Address")==h).map(c=>c.getField("Quality")).getOrElse("-")+"\t")))
      println()
      dateLong=dl
      lb=new ListBuffer[Cell]()
    }
  }
}
