package marcin

import java.io.FileWriter
import scala.xml._

object SVGPrinter  extends  App{
  val fw=new FileWriter("o.svg")
  val x=
    <x>x</x>
  val o=
    <svg width="100%" height="100%" xmlns="http://www.w3.org/2000/svg">
    <g>
      <title>Layer 1</title>
      {for (i <- 1 to 100) yield <rect id={"svg_"+i} height="199" width="74" y={""+i*10} x={""+i*10} stroke-width="0.5" stroke="#000000" fill="#dd0000">{i}</rect>}
      {x}
    </g>
  </svg>


  fw.write(""+o)
  println(o)
  fw.close()
}
