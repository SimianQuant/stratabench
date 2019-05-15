package simianquant.bench.strata.atomic

import com.opengamma.strata.math.impl.cern.Probability
import java.util.concurrent.TimeUnit
import org.openjdk.jmh.infra.Blackhole
import org.openjdk.jmh.annotations._
import simianquant.mathbridge.NormalDistribution

/** Benchmarks three variations of the implementation of Normal Cumulative Distribution Function.
  *
  * @author Harshad Deo
  */
@BenchmarkMode(Array(Mode.SampleTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class NormalCdf {

  private val xlb = -7.0
  private val xub = 7.0
  private val cnt = 1 << 10
  private val xincr = (xub - xlb) / cnt

  @Benchmark
  def strata(bh: Blackhole): Unit = {
    var x = xlb
    while (x < xub) {
      val eval = Probability.normal(1, 0, x)
      bh.consume(eval)
      x += xincr
    }
  }

  @Benchmark
  def mathbridge(bh: Blackhole): Unit = {
    var x = xlb
    while (x < xub) {
      val eval = NormalDistribution.cdf(x)
      bh.consume(eval)
      x += xincr
    }
  }

}
