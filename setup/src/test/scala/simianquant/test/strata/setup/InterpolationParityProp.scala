package simianquant.test.strata.setup

import com.opengamma.strata.collect.array.DoubleArray
import org.scalactic.anyvals.PosInt
import org.scalacheck.Gen
import org.scalacheck.Prop.forAll
import org.scalatest.prop.Checkers
import org.scalatest.PropSpec
import simianquant.strata.setup.{ACMInterpolatorFlatExtrapolator, InterpolatorWrapper}

/** Validates that the wrappers are building the expected interpolators
  *
  * @author Harshad Deo
  */
final class InterpolationParityProp extends PropSpec with Checkers {

  implicit override final val generatorDrivenConfig = PropertyCheckConfiguration(
    minSuccessful = 1000000,
    workers = PosInt.from(Runtime.getRuntime().availableProcessors()).get)

  private val xlb = -3.2
  private val xub = 3.2
  private val _eps = 1e-14

  private val xs = Array(-3.0, -2.8, -2.6, -2.4, -2.2, -2.0, -1.8, -1.6, -1.4, -1.2, -1.0, -0.8, -0.6, -0.4, -0.2, 0.0,
    0.2, 0.4, 0.6, 0.8, 1.0, 1.2, 1.4, 1.6, 1.8, 2.0, 2.2, 2.4, 2.6, 2.8)
  private val ys = Array(-0.05, -0.11, -0.17, -0.21, -0.22, -0.19, -0.12, -0.02, 0.12, 0.28, 0.45, 0.62, 0.78, 0.90,
    0.97, 1.00, 0.97, 0.90, 0.78, 0.62, 0.45, 0.28, 0.12, -0.02, -0.12, -0.19, -0.22, -0.21, -0.17, -0.11)

  private val acmLinear = ACMInterpolatorFlatExtrapolator.linear(xs, ys)
  private val acmCubicSpline = ACMInterpolatorFlatExtrapolator.cubicSpline(xs, ys)

  private val strataLinear = InterpolatorWrapper.linear(DoubleArray.ofUnsafe(xs), DoubleArray.ofUnsafe(ys))
  private val strataCubicSpline = InterpolatorWrapper.cubicSpline(DoubleArray.ofUnsafe(xs), DoubleArray.ofUnsafe(ys))

  private val genX = Gen.choose(xlb, xub)

  property("Linear Interpolation Parity") {
    check(forAll(genX) { x =>
      val acmY = acmLinear(x)
      val strataY = strataLinear.interpolate(x)
      math.abs(acmY - strataY) < _eps
    })
  }

  property("Cubic Spline Interpolation Parity") {
    check(forAll(genX) { x =>
      val acmY = acmCubicSpline(x)
      val strataY = strataCubicSpline.interpolate(x)
      math.abs(acmY - strataY) < _eps
    })
  }

}
