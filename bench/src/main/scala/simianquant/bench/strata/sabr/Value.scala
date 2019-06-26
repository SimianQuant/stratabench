package simianquant.bench.strata.sabr

import com.opengamma.strata.pricer.impl.volatility.smile.SabrHaganVolatilityFunctionProvider
import java.util.concurrent.TimeUnit
import org.openjdk.jmh.infra.Blackhole
import org.openjdk.jmh.annotations._

/** Benchmarks Strata's implementation of SABR evaluation
  *
  * @author Harshad Deo
  */
@BenchmarkMode(Array(Mode.SampleTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class Value {

  import SabrComputationParameters._

  @Benchmark
  def beta1(cp: Beta1, bh: Blackhole): Unit = {
    var ctr = 0
    while (ctr < cp.strikes.length) {
      val strike = cp.strikes(ctr)
      val eval =
        SabrHaganVolatilityFunctionProvider.DEFAULT.volatility(cp.forward, strike, cp.tau, cp.alpha, 1, cp.rho, cp.nu)
      bh.consume(eval)
      ctr += 1
    }
  }

  @Benchmark
  def beta0p5(cp: Beta0p5, bh: Blackhole): Unit = {
    var ctr = 0
    while (ctr < cp.strikes.length) {
      val strike = cp.strikes(ctr)
      val eval =
        SabrHaganVolatilityFunctionProvider.DEFAULT.volatility(cp.forward, strike, cp.tau, cp.alpha, 0.5, cp.rho, cp.nu)
      bh.consume(eval)
      ctr += 1
    }
  }

}
