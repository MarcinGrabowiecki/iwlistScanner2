package marcin

import scala.annotation.tailrec
import java.util.Date

/**
 * Created by Marcin on 2015-05-28.
 */


class Saga extends Printer {

  private val rIe = """\s*IE: (.*)""".r
  private val rUnknown = """Unknown: (.*)""".r
  private val rIeee = """(IEEE|WPA) (.*)""".r
  private val rBitrates = """\s*Bit Rates:(.*)""".r
  private val rBitratesCont = """\s{30}([0-9].*)""".r
  private val rQuality="""\s{20}Quality=(../..)  Signal level=-(..) dBm.*""".r
  private val rCellAddress="""\s{10}Cell (..) - Address: (.*)""".r
  private val r20="""\s{20}""".r
  private val rAny="""\s{20}([A-Z].*):(.*)""".r
  private val rDate="""::: [0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2} ([0-9]{10})""".r
  private val rWlan0="""wlan0     Scan completed :""".r

  @tailrec
  private def process(lr: LogReader, cell: Cell, date: Date): Unit = {

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

    retCel.addField("scanDate", "" + retDate)

    if (line.startsWith(" " * 10 + "Cell ")) {
      retCel.write
      retCel = new Cell(printer)
      new Matcher( """Cell (..) - Address: (.*)""")
        .setStartingSpaces(21)
        .forText(line)
        .forMatching(m => retCel.addField("Cell", m.group(1)).addField("Address", m.group(2))
        )
    }

      line match {
        //case Experymentator(bla,bul)  => println(bla)
        case r20=>{
          line match {
            case rIe(text) => text match {
              case rUnknown(text1) => retCel.addField("Unknown", text1)
              case rIeee(prefix, text) => retCel.addField(prefix, text); suckIndent(prefix)
              case doNothing => throw new RuntimeException(doNothing)
            }
            case rBitrates(values) => retCel.addField("BitRates", values)
            case rBitratesCont(values) => retCel.addField("BitRatesCont", values)
            case rQuality(qual,lvl) => retCel.addField("Quality", qual).addField("SignalLevel", lvl)
            case rAny(key,value) => retCel.addField(key,value)
            case rDate(value) => retDate = new Date(value.toLong * 1000)
            case rWlan0 =>
            case doNothing => throw new RuntimeException(doNothing)
          }
        }
      }
    process(lr, retCel, retDate)
  }

  def proc(lr: LogReader): Unit = {
    process(lr, new Cell(printer), null)
  }

  var printer: Printer = this

  override def print(line: String): Unit = println(line)
}
