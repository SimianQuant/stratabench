package simianquant.strata.setup

import com.opengamma.strata.collect.array.DoubleArray
import com.opengamma.strata.market.curve.interpolator.{BoundCurveInterpolator, CurveExtrapolators, CurveInterpolators}

/** Wrapper around Strata's weird syntax to generate interpolators
  *
  * @author Harshad Deo
  */
object InterpolatorWrapper {

  def linear(xs: DoubleArray, ys: DoubleArray): BoundCurveInterpolator = {
    val interp = CurveInterpolators.LINEAR.bind(xs, ys)
    val extrap = CurveExtrapolators.FLAT.bind(xs, ys, interp)
    interp.bind(extrap, extrap)
  }

  def cubicSpline(xs: DoubleArray, ys: DoubleArray): BoundCurveInterpolator = {
    val interp = CurveInterpolators.NATURAL_CUBIC_SPLINE.bind(xs, ys)
    val extrap = CurveExtrapolators.FLAT.bind(xs, ys, interp)
    interp.bind(extrap, extrap)
  }

}
