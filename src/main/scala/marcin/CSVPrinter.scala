package marcin

import java.io.FileWriter

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.xml._

/**
 * Created by m on 2015-08-23.
 */
class CSVPrinter {
  var id=0L;
  var addresses=new mutable.HashMap[String,Int]()
  var names=new mutable.HashMap[String,String]()
  var printOnce=true
  var dateLong=0L
  var cells=new ListBuffer[Cell]()
  val fw=new FileWriter("o.svg")
  CSV.ads.foreach(address=>print(s";${address}"))
  fw.write(
    """
      <svg width="20000" height="5500" xmlns="http://www.w3.org/2000/svg">
          <g>
            <title>Cells</title>
    """)

  CSV.freqAddresses.foreach(ee=> {
    val x =
      <text
        xml:space="preserve"
        text-anchor="middle"
        font-family="serif"
        font-size="24"
        id={"svg_"+ee}
        y={""+CSV.freqAddresses.indexOf(ee)*70}
        x="10"
        stroke-width="0"
        stroke="#000000"
        fill="#000000">{CSV.names.get(ee)}
      </text>
      fw.write(""+x)
    }
  )




  def printt(cell:Cell):Unit={
    if(id>300000) return

    incrementCellCounter(cell)
    names.put(cell.getAddress,cell.getName)

    val dl=cell.getOptionalField("scanDateLong").getOrElse(-1L).asInstanceOf[Long]
    cells+=cell
    if(dateLong!=dl){
      print(s"${dl};")
      CSV.freqAddresses.foreach(address=>
          print(
            cells.find(
              foundCell=>foundCell.getAddress==address).map(
                cell=> {
                  id=id+1
                  val quality=
                    <rect
                      id={"rq"+id}
                      height={(cell.getQuality/2).toString}
                      width="2"
                      fill-opacity="0.5"
                      y={""+CSV.ads.indexOf(address)*35}
                      x={""+(dateLong-1432347182000L)/1000/60}
                      fill="#dd0000"/>

                  val level=
                      <rect
                      id={"rl"+id}
                      height={(cell.getSignalLevel/2).toString}
                      width="2"
                      fill-opacity="0.5"
                      y={""+CSV.ads.indexOf(address)*35}
                      x={""+(dateLong-1432347182000L)/1000/60}
                      fill="#00ee00"/>
                  fw.write(""+level)
                  fw.write(""+quality)
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

  def incrementCellCounter(cell: Cell): Option[Int] = {
    if (addresses.contains(cell.getAddress)) {
      addresses.put(cell.getAddress, addresses.get(cell.getAddress).get + 1)
    } else {
      addresses.put(cell.getAddress, 1)
    }
  }

  def close()={
    fw.write(
      """
          </g>
          </svg>
      """.stripMargin)
    fw.close()
    println(names)
  }
}
