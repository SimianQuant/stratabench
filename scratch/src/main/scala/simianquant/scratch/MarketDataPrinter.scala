package simianquant.scratch

import collection.JavaConverters._
import com.opengamma.strata.examples.marketdata.ExampleMarketData
import com.opengamma.strata.data.FxRateId
import com.opengamma.strata.market.curve.{CurveId, CurveInfoType, InterpolatedNodalCurve}
import java.time.LocalDate

/** Prints the market data used by the examples, so that the numbers can be validated with external pricers
  *
  * @author Harshad Deo
  */
object MarketDataPrinter {

  val valuationDate = LocalDate.of(2014, 1, 22)
  val marketDataBuilder = ExampleMarketData.builder()
  val marketData = marketDataBuilder.buildSnapshot(valuationDate)
  val ids = marketData.getIds().asScala

  var prev = false

  def printInterpolatedCurve(curve: InterpolatedNodalCurve, curveGroupName: String) = {
    if (prev) {
      println("----------------------------------")
    }
    val name = curve.getName
    val interpolator = curve.getInterpolator.getName
    val leftExtrapolator = curve.getExtrapolatorLeft.getName
    val rightExtrapolator = curve.getExtrapolatorRight.getName
    val dayCount = curve.getMetadata().getInfo(CurveInfoType.DAY_COUNT)
    val xValueType = curve.getMetadata().getXValueType()
    val yValueType = curve.getMetadata().getYValueType()

    println(s"name: $name, group: $curveGroupName")
    println(s"interpolator: $interpolator, leftExtrapolator: $leftExtrapolator, rightExtrapolator: $rightExtrapolator")
    println(s"dayCount: $dayCount, xValueType: $xValueType, yValueType: $yValueType")

    val xValues = curve.getXValues()
    val yValues = curve.getYValues()
    var ctr = 0
    while (ctr < xValues.size()) {
      println(s"$ctr, ${xValues.get(ctr)}, ${yValues.get(ctr)}")
      ctr += 1
    }

    prev = true
  }

  def main(args: Array[String]): Unit = {
    printHeader("FxRate")
    ids foreach {
      case x: FxRateId => println(marketData.findValue(x).get)
      case _           => ()
    }

    printHeader("Curves")
    ids foreach {
      case curveId: CurveId =>
        val curve = marketData.findValue(curveId).get
        curve match {
          case x: InterpolatedNodalCurve => printInterpolatedCurve(x, curveId.getCurveGroupName().toString())
          case _                         => println(s"Printing of ${curve.getName()} is not supported")
        }
      case _ => ()
    }

    printHeader("ho gaya")
  }

}
