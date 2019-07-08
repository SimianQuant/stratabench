package simianquant.bench.sabr

import com.opengamma.strata.collect.array.DoubleArray
import java.util.concurrent.TimeUnit
import org.openjdk.jmh.infra.Blackhole
import org.openjdk.jmh.annotations._
import simianquant.strata.setup.FixedBetaSabrModelFitter

object Calibration {

  case class Input(timeToExpiry: Double, forward: Double, strikes: DoubleArray, impliedVols: DoubleArray)

  @State(Scope.Benchmark)
  class Inputs {
    var inputs = Array(
      Input(0.2575342466,
            100.0820867713320,
            DoubleArray.of(95.38960895, 99.87034026, 104.4323566),
            DoubleArray.of(0.1408125, 0.12825, 0.1239975)),
      Input(0.498630137,
            99.6415215845161,
            DoubleArray.of(93.42993192, 99.27155006, 105.4077149),
            DoubleArray.of(0.1358425, 0.12215, 0.1177475)),
      Input(1.005479452,
            98.7308837609165,
            DoubleArray.of(90.24809408, 98.03692194, 106.7276312),
            DoubleArray.of(0.1338425, 0.11845, 0.1146975)),
      Input(2.005479452,
            96.8200686026398,
            DoubleArray.of(85.35565746, 95.49341635, 108.1049459),
            DoubleArray.of(0.1334725, 0.1173, 0.1148675)),
      Input(3.005479452,
            94.8434394036724,
            DoubleArray.of(81.28858266, 92.88940915, 108.6033696),
            DoubleArray.of(0.1338725, 0.1177, 0.1152675)),
      Input(0.24931506849,
            68.57727269314,
            DoubleArray.of(66.95406834323, 68.57727269314, 70.02027469618),
            DoubleArray.of(0.07125000000, 0.06425000000, 0.06175000000)),
      Input(0.50958904110,
            69.70548349125,
            DoubleArray.of(67.12313632014, 69.70548349125, 72.00843472866),
            DoubleArray.of(0.07862500000, 0.07000000000, 0.06737500000)),
      Input(0.76164383562,
            70.73069190549,
            DoubleArray.of(67.46386442383, 70.73069190549, 73.68617561849),
            DoubleArray.of(0.08062500000, 0.07200000000, 0.06937500000)),
    )

    var errors = DoubleArray.of(1e-8, 1e-8, 1e-8)
    var initialParams = DoubleArray.of(0.1, 0.0, 1.0)
  }

}

/** Benchmarks Strata's SABR calibration routines
  *
  * @author Harshad Deo
  */
@BenchmarkMode(Array(Mode.SampleTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class Calibration {

  import Calibration._

  @Benchmark
  def betaOne(input: Inputs, bh: Blackhole): Unit = {
    input.inputs foreach {
      case Input(timeToExpiry, forward, strikes, impliedVols) =>
        val calibrator = new FixedBetaSabrModelFitter(1, forward, strikes, timeToExpiry, impliedVols, input.errors)
        val solved = calibrator.solve(input.initialParams)
        bh.consume(solved)
    }
  }

  @Benchmark
  def beta0p5(input: Calibration.Inputs, bh: Blackhole): Unit = {
    input.inputs foreach {
      case Input(timeToExpiry, forward, strikes, impliedVols) =>
        val calibrator = new FixedBetaSabrModelFitter(0.5, forward, strikes, timeToExpiry, impliedVols, input.errors)
        val solved = calibrator.solve(input.initialParams)
        bh.consume(solved)
    }
  }

}
