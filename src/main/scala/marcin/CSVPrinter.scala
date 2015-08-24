package marcin

import java.io.FileWriter

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
  val fw=new FileWriter("oo.svg")
  CSV.ads.foreach(address=>print(s";${address}"))
  fw.write(
    """
      <svg width="10000" height="2000" xmlns="http://www.w3.org/2000/svg">
          <g>
            <title>Layer 1</title>
    """)


  def printt(cell:Cell)={
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
            cells.find(
              foundCell=>foundCell.getField("Address")==address).map(
                cell=> {
                  val xml=
                    <rect
                      id={""+System.nanoTime()}
                      height={""+cell.getField("Quality")}
                      width="2"
                      y={""+CSV.ads.indexOf(address)*70}
                      x={""+(dateLong-1432347182000L)/1000/60}
                      stroke-width="0.5" stroke="#000000" fill="#dd0000"/>
                  fw.write(""+xml)
                  fw.write("\n")
                  cell.getField("Quality")
                }).getOrElse("")+";"
          )
      )
      println()
      dateLong=dl
      cells=new ListBuffer[Cell]()
    }
  }

  def close()={
    fw.write(
      """
          </g>
          </svg>
      """.stripMargin)
    fw.close()
  }
}
