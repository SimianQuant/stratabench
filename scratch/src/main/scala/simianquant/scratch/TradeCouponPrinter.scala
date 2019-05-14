package simianquant.scratch

import com.opengamma.strata.basics.ReferenceData
import simianquant.strata.setup.SampleTrades

/** Prints the trade coupons for external validation
  *
  * @author Harshad Deo
  */
object TradeCouponPrinter {

  val refData = ReferenceData.standard();

  def vanillaFixedVsLibor3mSwap() = {
    val product = SampleTrades.vanillaFixedVsLibor3mSwap.getProduct()
    val resolvedPayLeg = product.getPayLeg().get().resolve(refData)
    val resolvedReceiveLeg = product.getReceiveLeg().get().resolve(refData)

    val payLegPeriods = resolvedPayLeg.getPaymentPeriods()
    val receiveLegPeriods = resolvedReceiveLeg.getPaymentPeriods()

    // println(s"Payment")

    // println(payLegPeriods)
    // println(receiveLegPeriods)
  }

  def main(args: Array[String]): Unit = {
    vanillaFixedVsLibor3mSwap()
    println("ho gaya")
  }

}
