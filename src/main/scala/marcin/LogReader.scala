package marcin

import java.io.{FileReader, BufferedReader}

/**
 * Created by Marcin on 2015-05-28.
 */
class LogReader {
  private val br=new BufferedReader(new FileReader("../iflist"))
  private var undoBuffer:Option[String]=None
  private var lastRead:Option[String]=None

  def getLine: String ={
    if(undoBuffer.isEmpty) {
      lastRead = Some(br.readLine())
      return lastRead.get
    }

    val ret=undoBuffer.get
    undoBuffer=None
    return ret
  }

  def unread(): Unit ={
    undoBuffer = lastRead
  }


}
