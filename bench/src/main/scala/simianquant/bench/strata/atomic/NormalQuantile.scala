package simianquant.bench.strata.atomic

import com.opengamma.strata.math.impl.cern.Probability
import java.util.concurrent.TimeUnit
import org.openjdk.jmh.infra.Blackhole
import org.openjdk.jmh.annotations._
import simianquant.mathbridge.NormalDistribution
import simianquant.strata.jnibridge.AtomicFunction

object NormalQuantile {

  @State(Scope.Benchmark)
  class EvalLimits {
    val lb = 0.01
    val ub = 0.99
    val cnt = 1 << 16
  }

  @State(Scope.Benchmark)
  class AtomicFunctionInitialized {
    final val value = new AtomicFunction
  }
}

/** Benchmarks three variations of the implementation of Normal Quantile function
  *
  * @author Harshad Deo
  */
@BenchmarkMode(Array(Mode.SampleTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class NormalQuantile {

  import NormalQuantile._

  @Benchmark
  def strata(lim: EvalLimits, bh: Blackhole): Unit = {
    var x = lim.lb
    val xincr = (lim.ub - lim.lb) / lim.cnt
    while (x < lim.ub) {
      bh.consume(Probability.normalInverse(x))
      x += xincr
    }
  }

  @Benchmark
  def mathbridge(lim: EvalLimits, bh: Blackhole): Unit = {
    var x = lim.lb
    val xincr = (lim.ub - lim.lb) / lim.cnt
    while (x < lim.ub) {
      bh.consume(NormalDistribution.quantile(x))
      x += xincr
    }
  }

  @Benchmark
  def jni(lim: EvalLimits, afi: AtomicFunctionInitialized, bh: Blackhole): Unit = {
    var x = lim.lb
    val xincr = (lim.ub - lim.lb) / lim.cnt
    while (x < lim.ub) {
      bh.consume(afi.value.normalQuantile(x))
      x += xincr
    }
  }

}