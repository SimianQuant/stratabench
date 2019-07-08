package simianquant.strata.setup

import annotation.switch
import com.opengamma.strata.basics.value.ValueDerivatives;
import com.opengamma.strata.collect.array.DoubleArray
import com.opengamma.strata.pricer.impl.volatility.smile.{
  SabrHaganVolatilityFunctionProvider,
  SmileModelData,
  SmileModelFitter,
  VolatilityFunctionProvider
}
import com.opengamma.strata.math.impl.minimization.{
  DoubleRangeLimitTransform,
  NonLinearParameterTransforms,
  ParameterLimitsTransform,
  SingleRangeLimitTransform,
  UncoupledParameterTransforms
}
import com.opengamma.strata.math.impl.minimization.ParameterLimitsTransform.LimitType
import java.{util => jutil}

/** Parameters to be calibrated with a fixed beta
  *
  * @author Harshad Deo
  */
case class FixedBetaSabrModelData(alpha: Double, rho: Double, nu: Double) extends SmileModelData {
  override final def getNumberOfParameters(): Int = 3
  override final def getParameter(index: Int): Double = (index: @switch) match {
    case 0 => alpha
    case 1 => rho
    case 2 => nu
    case _ => 0
  }
  override final def isAllowed(index: Int, value: Double): Boolean = (index: @switch) match {
    case 0 => true
    case 1 => (value > -1 && value < 1) // perfect correlation and anti-correlation isn't allowed
    case 2 => value > 0
    case _ => false
  }
  override final def `with`(index: Int, value: Double): FixedBetaSabrModelData = (index: @switch) match {
    case 0 => copy(alpha = value)
    case 1 => copy(rho = value)
    case 2 => copy(nu = value)
    case _ => this
  }
}

/** Evaluates a SABR smile for a fixed beta
  *
  * @author Harshad Deo
  */
final class FixedBetaSabrVolatilityFunctionProvider(beta: Double)
    extends VolatilityFunctionProvider[FixedBetaSabrModelData] {

  override final def volatility(forward: Double,
                                strike: Double,
                                timeToExpiry: Double,
                                data: FixedBetaSabrModelData): Double = {
    SabrHaganVolatilityFunctionProvider.DEFAULT.volatility(forward,
                                                           strike,
                                                           timeToExpiry,
                                                           data.alpha,
                                                           beta,
                                                           data.rho,
                                                           data.nu)
  }

  override final def volatilityAdjoint(forward: Double,
                                       strike: Double,
                                       timeToExpiry: Double,
                                       data: FixedBetaSabrModelData): ValueDerivatives = {
    val calc = SabrHaganVolatilityFunctionProvider.DEFAULT.volatilityAdjoint(forward,
                                                                             strike,
                                                                             timeToExpiry,
                                                                             data.alpha,
                                                                             beta,
                                                                             data.rho,
                                                                             data.nu)
    // set to zero because they won't be used
    val sansBeta = DoubleArray.of(0, 0, calc.getDerivative(2), calc.getDerivative(4), calc.getDerivative(5))
    ValueDerivatives.of(calc.getValue, sansBeta)
  }

  // since it won't be used
  override final def volatilityAdjoint2(forward: Double,
                                        strike: Double,
                                        timeToExpiry: Double,
                                        data: FixedBetaSabrModelData,
                                        volatilityD: Array[Double],
                                        volatilityD2: Array[Array[Double]]): Double = 0

}

/** Calibrates a SABR smile for a fixed beta
  *
  * @author Harshad Deo
  */
final class FixedBetaSabrModelFitter(beta: Double,
                                     forward: Double,
                                     strikes: DoubleArray,
                                     timeToExpiry: Double,
                                     impliedVols: DoubleArray,
                                     error: DoubleArray)
    extends SmileModelFitter[FixedBetaSabrModelData](forward,
                                                     strikes,
                                                     timeToExpiry,
                                                     impliedVols,
                                                     error,
                                                     new FixedBetaSabrVolatilityFunctionProvider(beta)) {

  override protected final def getMaximumStep(): DoubleArray = null

  override protected final def getTransform(start: DoubleArray, fixed: jutil.BitSet): NonLinearParameterTransforms = {
    new UncoupledParameterTransforms(start, FixedBetaSabrModelFitter.DefaultTransforms, fixed)
  }

  override protected final def getTransform(start: DoubleArray): NonLinearParameterTransforms = {
    val fixed = new jutil.BitSet
    new UncoupledParameterTransforms(start, FixedBetaSabrModelFitter.DefaultTransforms, fixed)
  }

  override final def toSmileModelData(parameters: DoubleArray): FixedBetaSabrModelData = {
    FixedBetaSabrModelData(parameters.get(0), parameters.get(1), parameters.get(2))
  }
}

object FixedBetaSabrModelFitter {
  private val RhoLimit = 0.999;
  private val DefaultTransforms: Array[ParameterLimitsTransform] = Array(
    new SingleRangeLimitTransform(0, LimitType.GREATER_THAN),
    new DoubleRangeLimitTransform(-RhoLimit, RhoLimit), // -RhoLimit <= rho <= RhoLimit
    new DoubleRangeLimitTransform(0.01d, 2.50d)
  )

}
