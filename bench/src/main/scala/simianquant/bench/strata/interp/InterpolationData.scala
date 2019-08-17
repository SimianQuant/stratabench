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
    val xs = StaggeredBuilder(xlb, xub, cnt)
  }

}

object InterpolationData2D {

  val xlb = -3.2
  val xub = 3.2

  val ylb = -3.2
  val yub = 3.2

  val data = Array(
    (-3.0, -3.0, -0.047),
    (-3.0, -1.5, -0.217),
    (-3.0, 0.0, 0.047),
    (-3.0, 1.5, 0.665),
    (-3.0, 3.0, 1.000),
    (-2.4, -3.0, -0.143),
    (-2.4, -1.5, -0.176),
    (-2.4, 0.0, 0.281),
    (-2.4, 1.5, 0.870),
    (-2.4, 3.0, 0.941),
    (-1.8, -3.0, -0.208),
    (-1.8, -1.5, -0.048),
    (-1.8, 0.0, 0.541),
    (-1.8, 1.5, 0.985),
    (-1.8, 3.0, 0.777),
    (-1.2, -3.0, -0.208),
    (-1.2, -1.5, 0.158),
    (-1.2, 0.0, 0.777),
    (-1.2, 1.5, 0.985),
    (-1.2, 3.0, 0.541),
    (-0.6, -3.0, -0.123),
    (-0.6, -1.5, 0.411),
    (-0.6, 0.0, 0.941),
    (-0.6, 1.5, 0.870),
    (-0.6, 3.0, 0.281),
    (0.0, -3.0, 0.047),
    (0.0, -1.5, 0.665),
    (0.0, 0.0, 1.000),
    (0.0, 1.5, 0.665),
    (0.0, 3.0, 0.047),
    (0.6, -3.0, 0.281),
    (0.6, -1.5, 0.870),
    (0.6, 0.0, 0.941),
    (0.6, 1.5, 0.411),
    (0.6, 3.0, -0.123),
    (1.2, -3.0, 0.541),
    (1.2, -1.5, 0.985),
    (1.2, 0.0, 0.777),
    (1.2, 1.5, 0.158),
    (1.2, 3.0, -0.208),
    (1.8, -3.0, 0.777),
    (1.8, -1.5, 0.985),
    (1.8, 0.0, 0.541),
    (1.8, 1.5, -0.048),
    (1.8, 3.0, -0.208),
    (2.4, -3.0, 0.941),
    (2.4, -1.5, 0.870),
    (2.4, 0.0, 0.281),
    (2.4, 1.5, -0.176),
    (2.4, 3.0, -0.143),
    (3.0, -3.0, 1.000),
    (3.0, -1.5, 0.665),
    (3.0, 0.0, 0.047),
    (3.0, 1.5, -0.217),
    (3.0, 3.0, -0.047)
  )

  @State(Scope.Benchmark)
  class Data {
    private val cnt = 1 << 14
    val xs = StaggeredBuilder(xlb, xub, cnt)
    val ys = StaggeredBuilder(ylb, yub, cnt)
  }

}
