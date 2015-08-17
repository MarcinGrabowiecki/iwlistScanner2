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
        var vv = "+"
        if (v.size > 1) vv = v(1)
        cell addField(tmps.split(" ")(0), vv)
        suckIndent(pref)
      } else {
        lr.unread
      }
    }

    new Matcher( """::: \d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2} (\d{10})""")
      .forText(line)
      .forMatching(m => retDate = new Date(m.group(1).toLong * 1000)
      )

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

    if (line.startsWith(" " * 20)) {

      line match {
        case r20=>{
          line match {
            case rIe(text) => text match {
              case rUnknown(text1) => retCel.addField("Unknown", text1)
              case rIeee(prefix, text) => retCel.addField(prefix, text); suckIndent(prefix)
              case _ =>
            }
            case rBitrates(values) => retCel.addField("BitRates", values)
            case rBitratesCont(values) => retCel.addField("BitRatesCont", values)
            case rQuality(qual,lvl) => retCel.addField("Quality", qual).addField("SignalLevel", lvl)
            case _ =>
          }
        }
      }

      new Matcher( """([A-Z].*):(.*)""")
        .setStartingSpaces(20).forText(line)
        .forMatching(m => retCel.addField(m.group(1), m.group(2))
        )

    }
    process(lr, retCel, retDate)
  }

  def proc(lr: LogReader): Unit = {
    process(lr, new Cell(printer), null)
  }

  var printer: Printer = this

  override def print(line: String): Unit = println(line)
}
