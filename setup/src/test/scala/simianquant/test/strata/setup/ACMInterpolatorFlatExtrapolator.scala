package simianquant.test.strata.setup

import org.apache.commons.math3.analysis.interpolation.LinearInterpolator

final class ACMInterpolatorFlatExtrapolator(xs: Array[Double], ys: Array[Double]) {

  private val interp = new LinearInterpolator().interpolate(xs, ys)

  def apply(x: Double): Double =
    if (x < xs(0)) {
      ys(0)
    } else if (x > xs(xs.length - 1)) {
      ys(ys.length - 1)
    } else {
      interp.value(x)
    }

}
