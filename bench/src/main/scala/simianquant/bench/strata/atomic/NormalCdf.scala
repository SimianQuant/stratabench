package simianquant.bench.strata.atomic

import com.opengamma.strata.math.impl.cern.Probability
import java.util.concurrent.TimeUnit
import org.openjdk.jmh.infra.Blackhole
import org.openjdk.jmh.annotations._
import simianquant.mathbridge.NormalDistribution
import simianquant.strata.jnibridge.AtomicFunction

object NormalCdf {

  @State(Scope.Benchmark)
  class EvalLimits {
    val lb = -4.0
    val ub = 4.0
    val cnt = 1 << 16
  }

  @State(Scope.Benchmark)
  class AtomicFunctionInitialized {
    final val value = new AtomicFunction
  }
}

/** Benchmarks three variations of the implementation of Normal Cumulative Distribution Function by measuring the time
  * taken to evaluate 65536 values.
  *
  * @author Harshad Deo
  */
@BenchmarkMode(Array(Mode.SampleTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class NormalCdf {

  import NormalCdf._

  @Benchmark
  def strata(lim: EvalLimits, bh: Blackhole): Unit = {
    var x = lim.lb
    val xincr = (lim.ub - lim.lb) / lim.cnt
    while (x < lim.ub) {
      bh.consume(Probability.normal(0, 1, x))
      x += xincr
    }
  }

  @Benchmark
  def mathbridge(lim: EvalLimits, bh: Blackhole): Unit = {
    var x = lim.lb
    val xincr = (lim.ub - lim.lb) / lim.cnt
    while (x < lim.ub) {
      bh.consume(NormalDistribution.cdf(x))
      x += xincr
    }
  }

  @Benchmark
  def jni(lim: EvalLimits, afi: AtomicFunctionInitialized, bh: Blackhole): Unit = {
    var x = lim.lb
    val xincr = (lim.ub - lim.lb) / lim.cnt
    while (x < lim.ub) {
      bh.consume(afi.value.normalCdf(x))
      x += xincr
    }
  }

}
