package marcin

import scala.annotation.tailrec
import java.util.Date

import scala.collection.mutable.ListBuffer

/**
 * Created by Marcin on 2015-05-28.
 */

class Saga extends Printer {

  private val rIe = """\s*IE: (.*)""".r
  private val rUnknown = """(Unknown): (.*)""".r
  private val rIeee = """(IEEE|WPA) (.*)""".r
  private val rBitrates = """\s*Bit Rates:(.*)""".r
  private val rBitratesCont = """\s{30}([0-9].*)""".r
  private val rQuality="""\s{20}(Quality)=(..)/70  Signal level=-(..) dBm.*""".r
  private val rCellAddress="""\s{10}(Cell) (..) - (Address): (.*)""".r
  private val r20="""\s{20}""".r
  private val rAny="""\s{20}([A-Z].*):(.*)""".r
  private val rDate="""::: [0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2} ([0-9]{10})""".r
  private val rWlan0="""wlan0\s{5}Scan completed :""".r

  val cells=ListBuffer[Cell]()

  @tailrec
  final def process(lr: LogReader, cell: Cell=new Cell(printer), date: Date=null): Unit = {

    var line = lr.getLine
    var retCel = cell
    var retDate = date
    if (line == null) return

    def suckIndent(pref: String): Unit = {
      line = lr.getLine
      if (line.startsWith(" " * 23)) {
        val tmps = pref + "-" + line.trim
        val v = tmps.split(": ");
        cell addField(tmps.split(" ")(0), if (v.size>1) v(1) else "+")
        suckIndent(pref)
      } else {
        lr.unread
      }
    }

    retCel.addField("scanDate", ""+retDate).addField("scanDateLong", if(retDate!=null) retDate.getTime else 0)

      line match {
        case rCellAddress(cellName,cell,adressName,address)=>{
          retCel.write
          //cells += retCel
          //if(cells.size%1000==0){
            //println("----------------------------------")
            //println(cells.groupBy(c=>c.getField("ESSID")).map(k=>k._1))
          //}
          //if(System.currentTimeMillis()%100==0) println(cells.size)
          retCel = new Cell(printer)
          retCel.addField(cellName, cell).addField(adressName, address)
        }
        case r20=>{
          line match {
            case rIe(text) => text match {
              case rUnknown(label,text1) => retCel.addField(label, text1)
              case rIeee(prefix, text) => retCel.addField(prefix, text); suckIndent(prefix)
              case doNothing => throw new RuntimeException(doNothing)
            }
            case rBitrates(values) => retCel.addField("BitRates", values)
            case rBitratesCont(values) => retCel.addField("BitRatesCont", values)
            case rQuality(qualityName,qual,lvl) => retCel.addField(qualityName, qual.toInt).addField("SignalLevel", lvl.toInt)
            case rAny(key,value) => retCel.addField(key,value)
            case rDate(value) => retDate = new Date(value.toLong * 1000)
            case rWlan0 =>
            case doNothing => throw new RuntimeException(doNothing)
          }
        }
      }
    process(lr, retCel, retDate)
  }


  var printer: Printer = this

  override def print(line: String): Unit = println(line)
}
