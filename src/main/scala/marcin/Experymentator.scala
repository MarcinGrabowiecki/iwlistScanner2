package marcin

/**
 * Created by m on 2015-08-17.
 */
object Experymentator {
//  def unapply(x:Any):Option[Any]={
//    println(s"u:${x}")
//    None
//  }

  def unapply(x:Any,y:Any):Option[Any]={
    println(s"u:${x} ${y}")
    None
  }
}
