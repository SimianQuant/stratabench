package simianquant.bench.strata.black

import java.util.concurrent.TimeUnit
import org.openjdk.jmh.infra.Blackhole
import org.openjdk.jmh.annotations._
import simianquant.bench.strata.utils.SequentialBuilder
import com.opengamma.strata.pricer.impl.option.BlackScholesFormulaRepository

object Spot {

  @State(Scope.Benchmark)
  class ComputationalParameters {
    private val cnt = 10
    var spot = 64.5
    var rn = 0.1 // numeraire zero rate
    var rc = 0.09 // carry zero rate
    var tau = 0.5
    var sigma = 0.09
    var strikes = SequentialBuilder(64.0, 66.0, 1 << cnt)
  }
}

case class SpotPremiumAndGreeks(premium: Double,
                                delta: Double,
                                dualDelta: Double,
                                rho: Double,
                                phi: Double,
                                theta: Double,
                                vega: Double)

/** Benchmarks Strata's implementation of discounted black prices
  *
  * @author Harshad Deo
  */
@BenchmarkMode(Array(Mode.SampleTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class Spot {

  import Spot._

  @Benchmark
  def premium(cp: ComputationalParameters, bh: Blackhole): Unit = {
    var ctr = 0
    while (ctr < cp.strikes.length) {
      val strike = cp.strikes(ctr)
      val res = BlackScholesFormulaRepository.price(cp.spot, strike, cp.tau, cp.sigma, cp.rn, cp.rc, true)
      bh.consume(res)
      ctr += 1
    }
  }

  @Benchmark
  def greeks(cp: ComputationalParameters, bh: Blackhole): Unit = {
    var ctr = 0
    while (ctr < cp.strikes.length) {
      val strike = cp.strikes(ctr)
      val price = BlackScholesFormulaRepository.price(cp.spot, strike, cp.tau, cp.sigma, cp.rn, cp.rc, true)
      val delta = BlackScholesFormulaRepository.delta(cp.spot, strike, cp.tau, cp.sigma, cp.rn, cp.rc, true)
      val dualDelta = BlackScholesFormulaRepository.dualDelta(cp.spot, strike, cp.tau, cp.sigma, cp.rn, cp.rc, true)
      val rho = BlackScholesFormulaRepository.rho(cp.spot, strike, cp.tau, cp.sigma, cp.rn, cp.rc, true)
      val phi = BlackScholesFormulaRepository.carryRho(cp.spot, strike, cp.tau, cp.sigma, cp.rn, cp.rc, true)
      val theta = BlackScholesFormulaRepository.theta(cp.spot, strike, cp.tau, cp.sigma, cp.rn, cp.rc, true)
      val vega = BlackScholesFormulaRepository.vega(cp.spot, strike, cp.tau, cp.sigma, cp.rn, cp.rc)
      val res = SpotPremiumAndGreeks(price, delta, dualDelta, rho, phi, theta, vega)
      bh.consume(res)
      ctr += 1
    }
  }

}
