package simianquant.strata.setup

import com.opengamma.strata.collect.array.DoubleArray
import com.opengamma.strata.market.curve.interpolator._
import com.opengamma.strata.market.surface.interpolator.{BoundSurfaceInterpolator, GridSurfaceInterpolator}

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

  def biLinear(data: Array[(Double, Double, Double)]): BoundSurfaceInterpolator = {
    val (xs, ys, zs) = data.unzip3
    GridSurfaceInterpolator
      .of(CurveInterpolators.LINEAR, CurveInterpolators.LINEAR)
      .bind(DoubleArray.ofUnsafe(xs), DoubleArray.ofUnsafe(ys), DoubleArray.ofUnsafe(zs))
  }

  def linearCubic(data: Array[(Double, Double, Double)]): BoundSurfaceInterpolator = {
    val (xs, ys, zs) = data.unzip3
    GridSurfaceInterpolator
      .of(CurveInterpolators.LINEAR, CurveInterpolators.NATURAL_CUBIC_SPLINE)
      .bind(DoubleArray.ofUnsafe(xs), DoubleArray.ofUnsafe(ys), DoubleArray.ofUnsafe(zs))
  }

}
