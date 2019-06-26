package simianquant.bench.strata.sabr

import org.openjdk.jmh.annotations.{Scope, State}
import simianquant.bench.strata.utils.SequentialBuilder

object SabrComputationParameters {

  @State(Scope.Benchmark)
  class Beta1 {
    private val cnt = 10
    var forward = 100.4456433578360
    var tau = 0.01917808219
    var strikeLb = 99.1
    var strikeUb = 101.7
    var strikes = SequentialBuilder(strikeLb, strikeUb, 1 << cnt)
    var alpha = 0.11535269852484416
    var nu = 4.249484906629612
    var rho = -0.08280305920343885
  }

  @State(Scope.Benchmark)
  class Beta0p5 {
    private val cnt = 10
    var forward = 100.4456433578360
    var tau = 0.01917808219
    var strikeLb = 99.1
    var strikeUb = 101.7
    var strikes = SequentialBuilder(strikeLb, strikeUb, 1 << cnt)
    var alpha = 1.155974625105589
    var nu = 4.242018042515018
    var rho = -0.06877254517875576
  }

}
