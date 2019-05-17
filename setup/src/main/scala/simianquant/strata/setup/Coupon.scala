package simianquant.strata.setup

import java.time.LocalDate

/** Simplified representation of a coupon, to assist in validation
  *
  * @author Harshad Deo
  */
sealed trait Coupon

case class FixedPaymentCoupon(notional: Double, rate: Double, accrual: Double, paymentDate: LocalDate) extends Coupon

case class FloatPaymentCoupon(notional: Double,
                              forecastStartDate: LocalDate,
                              forecastEndDate: LocalDate,
                              forecastAccrual: Double,
                              paymentAccrual: Double,
                              paymentDate: LocalDate)
    extends Coupon
