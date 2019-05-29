package simianquant.bench.strata.black

import java.util.concurrent.TimeUnit
import org.openjdk.jmh.infra.Blackhole
import org.openjdk.jmh.annotations._
import simianquant.bench.strata.utils.SequentialBuilder
import com.opengamma.strata.pricer.impl.option.BlackFormulaRepository

object Forward {

  @State(Scope.Benchmark)
  class ComputationalParameters {
    private val cnt = 10
    var forward = 65
    var tau = 0.5
    var sigma = 0.09
    var strikes = SequentialBuilder(64.0, 66.0, 1 << cnt)
  }
}

case class ForwardPremiumAndGreeks(premium: Double, delta: Double, dualDelta: Double, theta: Double, vega: Double)

/** Benchmarks Strata's implementation of undiscounted black prices
  *
  * @author Harshad Deo
  */
@BenchmarkMode(Array(Mode.SampleTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class Forward {

  import Forward._

  @Benchmark
  def premium(cp: ComputationalParameters, bh: Blackhole): Unit = {
    var ctr = 0
    while (ctr < cp.strikes.length) {
      val strike = cp.strikes(ctr)
      val res = BlackFormulaRepository.price(cp.forward, strike, cp.tau, cp.sigma, true)
      bh.consume(res)
      ctr += 1
    }
  }

  @Benchmark
  def greeksAdjoint(cp: ComputationalParameters, bh: Blackhole): Unit = {
    var ctr = 0
    while (ctr < cp.strikes.length) {
      val strike = cp.strikes(ctr)
      val res = BlackFormulaRepository.priceAdjoint(cp.forward, strike, cp.tau, cp.sigma, true)
      bh.consume(res)
      ctr += 1
    }
  }

  @Benchmark
  def greeksSep(cp: ComputationalParameters, bh: Blackhole): Unit = {
    var ctr = 0
    while (ctr < cp.strikes.length) {
      val strike = cp.strikes(ctr)
      val price = BlackFormulaRepository.price(cp.forward, strike, cp.tau, cp.sigma, true)
      val delta = BlackFormulaRepository.delta(cp.forward, strike, cp.tau, cp.sigma, true)
      val dualDelta = BlackFormulaRepository.dualDelta(cp.forward, strike, cp.tau, cp.sigma, true)
      val theta = BlackFormulaRepository.driftlessTheta(cp.forward, strike, cp.tau, cp.sigma)
      val vega = BlackFormulaRepository.vega(cp.forward, strike, cp.tau, cp.sigma)
      val res = ForwardPremiumAndGreeks(price, delta, dualDelta, theta, vega)
      bh.consume(res)
      ctr += 1
    }
  }

}
