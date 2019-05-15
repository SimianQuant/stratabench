package simianquant.test.strata.jnibridge

import com.opengamma.strata.math.impl.cern.Probability
import org.scalacheck.Gen
import org.scalacheck.Prop.forAll
import org.scalatest.prop.Checkers
import org.scalatest.PropSpec
import simianquant.strata.jnibridge.AtomicFunction

/** Simple class to ensure that the JNI implementations are correct
  *
  * @author Harshad Deo
  */
final class AtomicFunctionProp extends PropSpec with Checkers {

  private val atomicFunction = new AtomicFunction

  implicit override final val generatorDrivenConfig = PropertyCheckConfiguration(minSuccessful = 1000000)

  property("normal-cdf equivalence") {
    check(
      forAll(Gen.choose(-4.0, 4.0)) { x =>
        val jniv = atomicFunction.normalCdf(x)
        val stratav = Probability.normal(x)
        val error = jniv - stratav
        math.abs(error) < 1e-15
      }
    )
  }

  property("normal-quantile equivalence") {
    check(
      forAll(Gen.choose(0.01, 0.99)) { x =>
        if (x <= 0 || x >= 1) {
          true
        } else {
          val jniv = atomicFunction.normalQuantile(x)
          val stratav = Probability.normalInverse(x)
          val error = jniv - stratav
          math.abs(error) < 1e-14
        }
      }
    )
  }

}
