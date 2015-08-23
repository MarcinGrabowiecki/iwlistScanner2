package marcin

import scala.collection.mutable

/**
 * Created by m on 2015-08-22.
 */
class InteligentMap {

  val dateCells = mutable.HashMap.empty[Long,mutable.HashMap[String,Cell]]

  def add(date:Any,c:Cell):Unit={
    if(date==null) return
    val ldate=date.asInstanceOf[Long]
    if(dateCells.contains(ldate)){
      dateCells.get(ldate).get+=(""+c.getField("Address")->c)
    } else {
      val listBuffer=mutable.HashMap.empty[String,Cell]
      dateCells.put(ldate,listBuffer)
      listBuffer+=(""+c.getField("Address")->c)
    }
  }
}
