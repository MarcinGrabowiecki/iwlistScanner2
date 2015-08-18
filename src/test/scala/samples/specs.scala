package samples

import marcin.{ESSth, Saga, LogReader}
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable._
//import org.specs2.runner._
  

/**
 * Sample specification.
 * 
 * This specification can be executed with: scala -cp <your classpath=""> ${package}.SpecsTest
 * Or using maven: mvn test
 *
 * For more information on how to write or run specifications, please visit: 
 *   http://etorreborre.github.com/specs2/guide/org.specs2.guide.Runners.html
 *
 */
@RunWith(classOf[JUnitRunner])
class MySpecTest extends Specification {
  "The 'Hello world' string" should {
    "contain 11 characters" in {
      "Hello world" must have size(11)
    }
    "start with 'Hello'" in {
      "Hello world" must startWith("Hello")
    }
    "end with 'world'" in {
      "Hello world" must endWith("world")
    }
  }



  "LogReader" should {
    val lr = new LogReader
    "read single line" in {
      lr.getLine must have size(34)
    }

    "be able to unread" in {
      val line1=lr.getLine
      lr.unread()
      line1 must beEqualTo(lr.getLine)
    }

  }

  "Saga" should {
    "read log" in {
      new Saga().proc(new LogReader());
      //new ESSth().parse;
      1 must beEqualTo(1)
    }
  }


}
