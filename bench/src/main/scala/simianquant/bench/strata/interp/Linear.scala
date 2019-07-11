package simianquant.bench.strata.interp

import com.opengamma.strata.market.curve.interpolator.{CurveExtrapolators, CurveInterpolators}
import java.util.concurrent.TimeUnit
import org.openjdk.jmh.infra.Blackhole
import org.openjdk.jmh.annotations._

object Linear {

  @State(Scope.Benchmark)
  class Instance {
    // val instance = CurveInterpolators.LINEAR.bind(InterpolationData1D.xs, InterpolationData1D.ys).bind(CurveExtrapolators.FLAT, CurveExtrapolators.FLAT)
  }

}

/** Benchmarks Strata's implementation of linear interpolation
  *
  * @author Harshad Deo
  */
@BenchmarkMode(Array(Mode.SampleTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class Linear {

  @Benchmark
  def value(bh: Blackhole): Unit = {}

  @Benchmark
  def sensitivity(bh: Blackhole): Unit = {}

}
