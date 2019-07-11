package simianquant.strata.setup

import org.apache.commons.math3.analysis.interpolation.{LinearInterpolator, SplineInterpolator}
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction

/** ACM Interpolators don't support extrapolation, so this is a simple wrapper class. It does not extend Function1 because
  * the apply method would have been boxed
  *
  * @author Harshad Deo
  */
final class ACMInterpolatorFlatExtrapolator private (xmin: Double,
                                                     ymin: Double,
                                                     xmax: Double,
                                                     ymax: Double,
                                                     interp: PolynomialSplineFunction) {
  final def apply(x: Double): Double =
    if (x < xmin) {
      ymin
    } else if (x > xmax) {
      ymax
    } else {
      interp.value(x)
    }
}

object ACMInterpolatorFlatExtrapolator {

  def linear(xs: Array[Double], ys: Array[Double]): ACMInterpolatorFlatExtrapolator = {
    val interp = new LinearInterpolator().interpolate(xs, ys) // this will also check all relevant invariants
    new ACMInterpolatorFlatExtrapolator(xs(0), ys(0), xs(xs.length - 1), ys(ys.length - 1), interp)
  }

  def cubicSpline(xs: Array[Double], ys: Array[Double]): ACMInterpolatorFlatExtrapolator = {
    val interp = new SplineInterpolator().interpolate(xs, ys)
    new ACMInterpolatorFlatExtrapolator(xs(0), ys(0), xs(xs.length - 1), ys(ys.length - 1), interp)
  }
}
