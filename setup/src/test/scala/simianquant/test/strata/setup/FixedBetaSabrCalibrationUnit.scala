package simianquant.test.strata.setup

import com.opengamma.strata.collect.array.DoubleArray
import org.scalatest.FlatSpec
import com.opengamma.strata.pricer.impl.volatility.smile.SabrHaganVolatilityFunctionProvider
import simianquant.strata.setup.FixedBetaSabrModelFitter

final class FixedBetaSabrCalibrationUnit extends FlatSpec {

  case class Input(timeToExpiry: Double, forward: Double, strikes: DoubleArray, impliedVols: DoubleArray)

  val inputs = Array(
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

  private val errors = DoubleArray.of(1e-8, 1e-8, 1e-8)
  private val initialParams = DoubleArray.of(0.1, 0.0, 1.0)

  private def check(forward: Double,
                    strikes: DoubleArray,
                    timeToExpiry: Double,
                    alpha: Double,
                    beta: Double,
                    rho: Double,
                    nu: Double,
                    impliedVols: DoubleArray): Boolean = {
    var res = true
    var ctr = 0
    while (res && (ctr < 3)) {
      val calc = SabrHaganVolatilityFunctionProvider.DEFAULT.volatility(forward,
                                                                        strikes.get(ctr),
                                                                        timeToExpiry,
                                                                        alpha,
                                                                        beta,
                                                                        rho,
                                                                        nu)
      res = math.abs(calc - impliedVols.get(ctr)) < 1e-10
      ctr += 1
    }
    res
  }

  it should "pass beta = 1 calibration - strata" in {
    inputs foreach {
      case Input(timeToExpiry, forward, strikes, impliedVols) =>
        val calibrator = new FixedBetaSabrModelFitter(1, forward, strikes, timeToExpiry, impliedVols, errors)
        val solved = calibrator.solve(initialParams).getModelParameters
        val alpha = solved.get(0)
        val rho = solved.get(1)
        val nu = solved.get(2)

        val res = check(forward, strikes, timeToExpiry, alpha, 1, rho, nu, impliedVols)
        assert(res)
    }
  }

  it should "pass beat = 0.5 calibration - strata" in {
    inputs foreach {
      case Input(timeToExpiry, forward, strikes, impliedVols) =>
        val calibrator = new FixedBetaSabrModelFitter(0.5, forward, strikes, timeToExpiry, impliedVols, errors)
        val solved = calibrator.solve(initialParams).getModelParameters
        val alpha = solved.get(0)
        val rho = solved.get(1)
        val nu = solved.get(2)

        val res = check(forward, strikes, timeToExpiry, alpha, 0.5, rho, nu, impliedVols)
        assert(res)
    }
  }

}
