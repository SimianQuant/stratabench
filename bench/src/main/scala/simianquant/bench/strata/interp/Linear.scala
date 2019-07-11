package simianquant.bench.strata.interp

import java.util.concurrent.TimeUnit
import org.openjdk.jmh.infra.Blackhole
import org.openjdk.jmh.annotations._
import simianquant.strata.setup.{ACMInterpolatorFlatExtrapolator, InterpolatorWrapper}

object Linear {

  @State(Scope.Benchmark)
  class StrataInterp {
    val instance = InterpolatorWrapper.linear(InterpolationData1D.xs, InterpolationData1D.ys)
  }

  @State(Scope.Benchmark)
  class ACMInterp {
    val instance = ACMInterpolatorFlatExtrapolator.linear(InterpolationData1D.xsr, InterpolationData1D.ysr)
  }

}

/** Benchmarks Strata's implementation of linear interpolation
  *
  * @author Harshad Deo
  */
@BenchmarkMode(Array(Mode.SampleTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class Linear {

  import Linear._

  @Benchmark
  def value(interp: StrataInterp, data: InterpolationData1D.Data, bh: Blackhole): Unit = {
    var ctr = 0
    while (ctr < data.xs.length) {
      val x = data.xs(ctr)
      bh.consume(interp.instance.interpolate(x))
      ctr += 1
    }
  }

  @Benchmark
  def valueACM(interp: ACMInterp, data: InterpolationData1D.Data, bh: Blackhole): Unit = {
    var ctr = 0
    while (ctr < data.xs.length) {
      val x = data.xs(ctr)
      bh.consume(interp.instance(x))
      ctr += 1
    }
  }

  @Benchmark
  def sensitivity(interp: StrataInterp, data: InterpolationData1D.Data, bh: Blackhole): Unit = {
    var ctr = 0
    while (ctr < data.xs.length) {
      val x = data.xs(ctr)
      bh.consume(interp.instance.parameterSensitivity(x))
      ctr += 1
    }
  }

}
