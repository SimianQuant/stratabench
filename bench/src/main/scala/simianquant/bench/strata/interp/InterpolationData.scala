package simianquant.bench.strata.interp

import com.opengamma.strata.collect.array.DoubleArray

object InterpolationData1D {

  val xlb = -3.2
  val xub = 3.2

  val xs: DoubleArray = DoubleArray.ofUnsafe(
    Array(-3.0, -2.8, -2.6, -2.4, -2.2, -2.0, -1.8, -1.6, -1.4, -1.2, -1.0, -0.8, -0.6, -0.4, -0.2, 0.0, 0.2, 0.4, 0.6,
      0.8, 1.0, 1.2, 1.4, 1.6, 1.8, 2.0, 2.2, 2.4, 2.6, 2.8))
  val ys: DoubleArray = DoubleArray.ofUnsafe(
    Array(-0.05, -0.11, -0.17, -0.21, -0.22, -0.19, -0.12, -0.02, 0.12, 0.28, 0.45, 0.62, 0.78, 0.90, 0.97, 1.00, 0.97,
      0.90, 0.78, 0.62, 0.45, 0.28, 0.12, -0.02, -0.12, -0.19, -0.22, -0.21, -0.17, -0.11))

}
