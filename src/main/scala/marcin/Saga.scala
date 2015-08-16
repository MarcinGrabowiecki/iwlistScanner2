package marcin

import scala.annotation.tailrec
import java.util.Date

/**
 * Created by Marcin on 2015-05-28.
 */


class Saga extends Printer {

  @tailrec
  private def process(lr: LogReader, cell: Cell, date: Date): Unit = {
    var line = lr.getLine
    var retCel = cell
    var retDate = date
    if (line == null) return


    val ie = """\s*IE:(.*)""".r
    val unknown = """ Unknown: (.*)""".r
    line match {
      case ie(text)=>text match {
        case unknown(text1)=> cell.addField("Unknown",text1)
        case _ =>
      }
      case _ =>
    }



    if (line.startsWith(" "*20+"IE: ")) {
      line = line.replace("                    IE: ", "                    IE-")
    }

    new Matcher( """::: \d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2} (\d{10})""")
      .forText(line)
      .forMatching(m =>
      retDate = new Date(m.group(1).toLong * 1000)
    )

    retCel.addField("scanDate", "" + retDate)

    if (line.startsWith(" "*10+"Cell ")) {
      retCel.write
      retCel = new Cell(printer)
      new Matcher( """Cell (..) - Address: (.*)""")
        .setStartingSpaces(21)
        .forText(line)
        .forMatching(m => retCel.addField("Cell", m.group(1)).addField("Address", m.group(2))
      )
    }





    if (line.startsWith(" " * 20)) {


      new Matcher( """([A-Z].*):(.*)""")
        .setStartingSpaces(20).forText(line)
        .forMatching(m =>
        if (m.group(1) == "Bit Rates") {
          val next = lr.getLine
          if (next.startsWith(" "*29)) {
            retCel.addField(m.group(1) + "-1", m.group(1) + next.trim)
          } else {
            lr.unread()
            retCel.addField(m.group(1) + "-2", m.group(1))
          }
        } else {
          retCel.addField(m.group(1), m.group(2))
        }
      )

      new Matcher( """Quality=(../..)  Signal level=-(..) dBm""")
        .setStartingSpaces(20).forText(line)
        .forMatching(m => retCel.addField("Quality", m.group(1)).addField("SignalLevel", m.group(2))
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
