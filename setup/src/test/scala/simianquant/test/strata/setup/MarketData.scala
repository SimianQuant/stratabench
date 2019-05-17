package simianquant.test.strata.setup

import com.opengamma.strata.basics.date.DayCounts
import java.time.LocalDate
import simianquant.strata.setup.Constants

object MarketData {

  object FxRate {
    val GBPUSD: Double = 1.61
  }

  private val usdDiscYf = Array(0.0027397260273972603, 0.09041095890410959, 0.16712328767123288, 0.25205479452054796,
    0.5013698630136987, 0.7534246575342466, 1.010958904109589, 2.0080395239164606, 3.0054794520547947,
    4.005479452054795, 5.005479452054795, 6.005307283479302, 7.008219178082192, 8.005479452054795, 9.005479452054795,
    10.005307283479302, 12.01095890410959, 15.005479452054795, 20.005479452054793, 25.005479452054793,
    30.008039523916462)
  private val usdDiscZero = Array(0.001571524, 9.34099E-4, 9.48444E-4, 9.35866E-4, 0.001027672, 0.001280398,
    0.001841773, 0.005475661, 0.009655095, 0.013191876, 0.015783922, 0.017845475, 0.019539006, 0.02095814, 0.02217629,
    0.023239034, 0.02501226, 0.026896017, 0.028543824, 0.029327868, 0.029693673)
  private val usdDiscInterp = new ACMInterpolatorFlatExtrapolator(usdDiscYf, usdDiscZero)

  private val gbpDiscYf = Array(0.09041095890410959, 0.16712328767123288, 0.25205479452054796, 0.5013698630136987,
    0.7534246575342466, 1.010958904109589, 2.0080395239164606, 3.0054794520547947, 4.005479452054795, 5.005479452054795,
    6.005307283479302, 7.008219178082192, 8.005479452054795, 9.005479452054795, 10.005307283479302, 12.01095890410959,
    15.005479452054795, 20.005479452054793, 25.005479452054793, 30.008039523916462)
  private val gbpDiscZero = Array(0.003619914, 0.00388712, 0.003951528, 0.004603065, 0.005434232, 0.006434251,
    0.009851532, 0.012585948, 0.01445751, 0.015792938, 0.016943957, 0.017977007, 0.018778199, 0.019473144, 0.020153626,
    0.021307446, 0.022582553, 0.023968824, 0.024603858, 0.025077621)
  private val gbpDiscInterp = new ACMInterpolatorFlatExtrapolator(gbpDiscYf, gbpDiscZero)

  private val gbp3MYf = Array(0.2465753424657534, 0.4958904109589041, 0.7479452054794521, 1.0, 1.9998428026049853,
    3.0027397260273974, 4.0, 5.0, 5.999842802604985, 7.0, 8.005479452054795, 9.002739726027396, 9.999842802604986, 12.0,
    15.0, 20.002739726027396, 25.005479452054793, 29.999842802604984)
  private val gbp3MZero = Array(0.005636082, 0.006213342, 0.006970785, 0.007835542, 0.01139746, 0.014381743,
    0.016488241, 0.01804329, 0.019337967, 0.020453166, 0.021390734, 0.022179111, 0.022943567, 0.024280163, 0.025771703,
    0.027230902, 0.027578227, 0.027644976)
  private val gbp3MInterp = new ACMInterpolatorFlatExtrapolator(gbp3MYf, gbp3MZero)

  private val usd6MYf = Array(00.5013698630136987, 0.7534246575342466, 1.010958904109589, 2.0080395239164606,
    3.0054794520547947, 4.005479452054795, 5.005479452054795, 7.008219178082192, 10.005307283479302, 12.01095890410959,
    15.005479452054795, 20.005479452054793, 25.005479452054793, 30.008039523916462)
  private val usd6MZero = Array(0.003342049, 0.003814115, 0.004217781, 0.008029061, 0.01235849, 0.016019274,
    0.018731166, 0.022623657, 0.026432995, 0.028264092, 0.030176612, 0.031856567, 0.032613569, 0.032950241)
  private val usd6MInterp = new ACMInterpolatorFlatExtrapolator(usd6MYf, usd6MZero)

  private val usd3MYf = Array(0.25205479452054796, 0.5013698630136987, 0.7534246575342466, 1.010958904109589,
    2.0080395239164606, 3.0054794520547947, 4.005479452054795, 5.005479452054795, 7.008219178082192, 10.005307283479302,
    12.01095890410959, 15.005479452054795, 20.005479452054793, 25.005479452054793, 30.008039523916462)
  private val usd3MZero = Array(0.002413351, 0.002523692, 0.002696588, 0.003298585, 0.007112496, 0.011435289,
    0.015109223, 0.017822649, 0.021709401, 0.025521059, 0.027335798, 0.029219998, 0.030936921, 0.031693336, 0.032010599)
  private val usd3MInterp = new ACMInterpolatorFlatExtrapolator(usd3MYf, usd3MZero)

  private val dayCount = DayCounts.ACT_ACT_ISDA
  def yf(terminalDate: LocalDate): Double = dayCount.yearFraction(Constants.valuationDate, terminalDate)

  object Zero {

    def usdDisc(t: Double): Double = usdDiscInterp(t)
    def usdDisc(terminalDate: LocalDate): Double = usdDiscInterp(yf(terminalDate))

    def gbpDisc(t: Double): Double = gbpDiscInterp(t)
    def gbpDisc(terminalDate: LocalDate): Double = gbpDiscInterp(yf(terminalDate))

    def gbp3M(t: Double): Double = gbp3MInterp(t)
    def gbp3M(terminalDate: LocalDate): Double = gbp3MInterp(yf(terminalDate))

    def usd6M(t: Double): Double = usd6MInterp(t)
    def usd6M(terminalDate: LocalDate): Double = usd6MInterp(yf(terminalDate))

    def usd3M(t: Double): Double = usd3MInterp(t)
    def usd3M(terminalDate: LocalDate): Double = usd3MInterp(yf(terminalDate))

  }

}
