package simianquant.bench.strata.interp

import java.util.concurrent.TimeUnit
import org.openjdk.jmh.infra.Blackhole
import org.openjdk.jmh.annotations._
import com.opengamma.strata.basics.value.ValueDerivatives
import simianquant.strata.setup.InterpolatorWrapper

object BiLinear {

  @State(Scope.Benchmark)
  class StrataInterp {
    val instance = InterpolatorWrapper.biLinear(InterpolationData2D.data)
  }

}

/** Benchmarks Strata's implementation of linear interpolation
  *
  * @author Harshad Deo
  */
@BenchmarkMode(Array(Mode.SampleTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class BiLinear {

  import BiLinear._

  @Benchmark
  def value(interp: StrataInterp, data: InterpolationData2D.Data, bh: Blackhole): Unit = {
    var ctr = 0
    while (ctr < data.xs.length) {
      val x = data.xs(ctr)
      val y = data.ys(ctr)
      bh.consume(interp.instance.interpolate(x, y))
      ctr += 1
    }
  }

  @Benchmark
  def sensitivity(interp: StrataInterp, data: InterpolationData2D.Data, bh: Blackhole): Unit = {
    var ctr = 0
    while (ctr < data.xs.length) {
      val x = data.xs(ctr)
      val y = data.xs(ctr)
      val value = interp.instance.interpolate(x, y)
      val sens = interp.instance.parameterSensitivity(x, y)
      bh.consume(ValueDerivatives.of(value, sens))
      ctr += 1
    }
  }

}
