package marcin

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
 * Created by m on 2015-08-22.
 */
@deprecated
object CellProcessor {
  def process(cls:ListBuffer[Cell])={

    val cells = mutable.HashMap.empty[String,String]
    val dates = mutable.HashMap.empty[Long,String]
    val dateCells = mutable.HashMap.empty[Long,ListBuffer[Cell]]

    val im=new InteligentMap

    cls.foreach(c=>{
      cells+=(""+c.getField("Address")->"+")
      im.add(c.getField("scanDateLong"),c)
    })


    println("-----------")
    println(cells)
    println(dates)
    println(im.dateCells)
    println("-----------")

    print("\t")

    cells.foreach(c=>print(s"${c._1}\t"))
    println()

    im.dateCells.foreach(k=>{
      print(s"${k._1}\t")
      cells.keySet.foreach(c=>{
        print(k._2.get(c).map(c=>c.getField("Quality")).getOrElse("-")+"\t")
      })
      println
    })


  }
}
