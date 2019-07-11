package simianquant.bench.strata.interp

import com.opengamma.strata.collect.array.DoubleArray
import org.openjdk.jmh.annotations.{Scope, State}
import simianquant.bench.strata.utils.StaggeredBuilder

object InterpolationData1D {

  val xlb = -3.2
  val xub = 3.2

  val xsr = Array(-3.00, -2.67, -2.34, -2.01, -1.68, -1.35, -1.02, -0.69, -0.36, -0.03, 0.30, 0.63, 0.96, 1.29, 1.62,
    1.95, 2.28, 2.61, 2.94, 3.27)
  val xs: DoubleArray = DoubleArray.ofUnsafe(xsr)

  val ysr = Array(-0.047, -0.152, -0.214, -0.191, -0.064, 0.158, 0.437, 0.711, 0.916, 0.999, 0.941, 0.756, 0.489, 0.206,
    -0.030, -0.176, -0.217, -0.167, -0.067, 0.039)
  val ys: DoubleArray = DoubleArray.ofUnsafe(ysr)

  @State(Scope.Benchmark)
  class Data {
    private val cnt = 1 << 14
    // StaggeredBuilder is used to fool the branch predictor
    val xs = StaggeredBuilder(InterpolationData1D.xlb, InterpolationData1D.xub, cnt)
  }

}
