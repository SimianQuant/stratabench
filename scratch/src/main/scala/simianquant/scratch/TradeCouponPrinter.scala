package simianquant.scratch

import com.opengamma.strata.basics.ReferenceData
import simianquant.strata.setup._
import com.opengamma.strata.product.rate._
import com.opengamma.strata.product.swap._

/** Prints the trade coupons for external validation
  *
  * @author Harshad Deo
  */
object TradeCouponPrinter {

  val refData = ReferenceData.standard();

  def toCoupon(arg: SwapPaymentPeriod): Coupon = {
    val paymentDate = arg.getPaymentDate()
    arg match {
      case rp: RatePaymentPeriod =>
        val notional = rp.getNotional
        val accrualPeriods = rp.getAccrualPeriods()
        if (accrualPeriods.size == 1) {
          val accrualPeriod = accrualPeriods.get(0)
          val paymentAccrual = accrualPeriod.getYearFraction()
          accrualPeriod.getRateComputation() match {
            case x: FixedRateComputation => FixedPaymentCoupon(notional, x.getRate(), paymentAccrual, paymentDate)
            case x: IborRateComputation =>
              FloatPaymentCoupon(notional,
                                 x.getEffectiveDate(),
                                 x.getMaturityDate(),
                                 x.getYearFraction(),
                                 paymentAccrual,
                                 accrualPeriod.getSpread(),
                                 paymentDate)
            case _ => throw new IllegalArgumentException("Unsupported rate computation")
          }
        } else {
          throw new IllegalArgumentException("Unsupported accrual period length")
        }
      case _ => throw new IllegalArgumentException("Unsupported Payment Period")
    }
  }

  def printVanillaFixedVsLibor3mSwap() = {
    val swap = SampleTrades.vanillaFixedVsLibor3mSwap.getProduct()

    printHeader("Pay Leg")
    val payLeg = swap.getPayLeg().get.resolve(refData)
    val payIterator = payLeg.getPaymentPeriods().iterator
    while (payIterator.hasNext()) {
      val coupon = toCoupon(payIterator.next())
      println(coupon)
    }

    printHeader("Receive Leg")
    val receiveLeg = swap.getReceiveLeg().get.resolve(refData)
    val receiveIterator = receiveLeg.getPaymentPeriods().iterator
    while (receiveIterator.hasNext()) {
      val coupon = toCoupon(receiveIterator.next())
      println(coupon)
    }
  }

  def printBasisLibor3mVsLibor6mWithSpreadSwap() = {
    val swap = SampleTrades.basisLibor3mVsLibor6mWithSpreadSwap.getProduct()

    printHeader("Pay Leg")
    val payLeg = swap.getPayLeg().get.resolve(refData)
    val payIterator = payLeg.getPaymentPeriods().iterator
    while (payIterator.hasNext) {
      val coupon = toCoupon(payIterator.next())
      println(coupon)
    }

    printHeader("Receive Leg")
    val receiveLeg = swap.getReceiveLeg().get.resolve(refData)
    val receiveIterator = receiveLeg.getPaymentPeriods().iterator
    while (receiveIterator.hasNext) {
      val coupon = toCoupon(receiveIterator.next())
      println(coupon)
    }
  }

  def main(args: Array[String]): Unit = {
    // printVanillaFixedVsLibor3mSwap()
    // printBasisLibor3mVsLibor6mWithSpreadSwap()
    printOvernightAveragedWithSpreadVsLibor3mSwap()
    println("ho gaya")
  }

}
