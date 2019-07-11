package simianquant.test.strata.setup

import org.apache.commons.math3.analysis.interpolation.{LinearInterpolator, SplineInterpolator}

object ACMInterpolatorFlatExtrapolator {
  private final class Linear(xs: Array[Double], ys: Array[Double]) extends (Double => Double) {
    private val interp = new LinearInterpolator().interpolate(xs, ys)
    override final def apply(x: Double): Double =
      if (x < xs(0)) {
        ys(0)
      } else if (x > xs(xs.length - 1)) {
        ys(ys.length - 1)
      } else {
        interp.value(x)
      }
  }

  private final class CubicSpline(xs: Array[Double], ys: Array[Double]) extends (Double => Double) {
    private final val interp = new SplineInterpolator().interpolate(xs, ys)
    override final def apply(x: Double): Double =
      if (x < xs(0)) {
        ys(0)
      } else if (x > xs(xs.length - 1)) {
        ys(ys.length - 1)
      } else {
        interp.value(x)
      }
  }

  def linear(xs: Array[Double], ys: Array[Double]): Double => Double = new Linear(xs, ys)

  def cubicSpline(xs: Array[Double], ys: Array[Double]): Double => Double = new CubicSpline(xs, ys)
}
