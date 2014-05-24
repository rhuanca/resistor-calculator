import scala.swing.BoxPanel
import scala.swing.Button
import scala.swing.Dimension
import scala.swing.Label
import scala.swing.MainFrame
import scala.swing.Orientation
import scala.swing.SimpleSwingApplication
import scala.swing.TextField
import scala.swing.event.ButtonClicked

object SecondSwingApp extends SimpleSwingApplication {
  def top = new MainFrame {
    title = "Resistor Calculator"
    object battVolt extends TextField("6.47") 
    object ledVolt extends TextField("2.07")
    object transistorOnVolt extends TextField("1.0")
    object ledResistence extends TextField
    object offResistence extends TextField("1900")
    object onResistence extends TextField("7300")
    object calculate extends Button { text = "Calculate" }
    object result1 extends Label("Result 1: ")
    object result2 extends Label("Result 2:")
    minimumSize = new Dimension(200, 400)
    peer.setLocationRelativeTo(null)

    contents = new BoxPanel(Orientation.Vertical) {
      contents += new Label("Battery Voltage")
      contents += battVolt
      contents += new Label("Led Voltage")
      contents += ledVolt
      contents += new Label("Transistor ON Voltage")
      contents += transistorOnVolt
      contents += new Label("LED Resistence")
      contents += ledResistence
      contents += new Label("OFF Resistence")
      contents += offResistence
      contents += new Label("ON Resistence")
      contents += onResistence
      contents += calculate
      contents += result1
      contents += result2
    }

    def calcUpperResistence(vin: Double, vout: Double, lowResistence: Double): Double = {
      (lowResistence * (vin - vout)) / vout
    }

    def calcVolt(up: Double, low: Double, vin: Double): Double = {
      (low * vin) / (up + low)
    }

    def isGoodOn(v: Double, threshold: Double): Boolean = {
      v >= threshold
    }

    def isGoodOff(v: Double, threshold: Double): Boolean = {
      v <= threshold
    }

    listenTo(calculate)
    reactions += {
      case ButtonClicked(calculate) =>
        val workVolt = battVolt.text.toDouble - ledVolt.text.toDouble
        val transVolt = transistorOnVolt.text.toDouble
        val off = offResistence.text.toDouble
        val on = onResistence.text.toDouble
        val opt1 = calcUpperResistence(workVolt, transVolt/2, off)
        val opt2 = calcUpperResistence(workVolt, transVolt, on)

        println ("Work voltage = " + workVolt)
        println ("Off Opt1 = "+  opt1)
        println ("On Opt2 = "+  opt2)
        println ("transVolt = "+  transVolt)
        val optOn1 = calcVolt(opt1, on, workVolt)
        val optOff1 = calcVolt(opt1, off, workVolt)
        println ("optOn1 = "+  optOn1)
        println ("optOff1 = "+  optOff1)
        
        val isGood1 = isGoodOn(optOn1, transVolt) && isGoodOff(optOn1, transVolt/2 )
        val isGood2 = isGoodOn(calcVolt(opt2, on, workVolt), transVolt) && isGoodOff(calcVolt(opt2, off, workVolt), transVolt/2 )
        result1.text = "Result 1 = " + isGood1.toString + " with " + opt1.toString
        result2.text = "Result 2 = " + isGood2.toString + " with " + opt2.toString
    }
  }
}