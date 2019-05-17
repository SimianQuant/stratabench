package simianquant.test.strata.setup

import com.opengamma.strata.market.curve.CurveId
import com.opengamma.strata.examples.marketdata.ExampleMarketData
import org.scalactic.anyvals.PosInt
import org.scalacheck.Gen
import org.scalacheck.Prop.forAll
import org.scalatest.prop.Checkers
import org.scalatest.PropSpec
import simianquant.strata.setup.Constants

/** Getting the curves wrong can cause a lot of unnecessary hair pulling when trying to validate trades. This simple
  * class makes sure that that isnt a problem
  *
  * @author Harshad Deo
  */
final class CurveParityProp extends PropSpec with Checkers {

  implicit override final val generatorDrivenConfig = PropertyCheckConfiguration(
    minSuccessful = 1000000,
    workers = PosInt.from(Runtime.getRuntime().availableProcessors()).get)

  private val marketDataBuilder = ExampleMarketData.builder();
  private val marketData = marketDataBuilder.buildSnapshot(Constants.valuationDate);
  private val yfGen = Gen.choose(0.0, 32.0)

  private val usdDiscCurveId = CurveId.of("Default", "USD-Disc")
  private val gbpDiscCurveId = CurveId.of("Default", "GBP-Disc")
  private val gbp3MCurveId = CurveId.of("Default", "GBP-3ML")
  private val usd3MCurveId = CurveId.of("Default", "USD-3ML")
  private val usd6MCurveId = CurveId.of("Default", "USD-6ML")

  private val usdDiscCurve = marketData.findValue(usdDiscCurveId).get
  private val gbpDiscCurve = marketData.findValue(gbpDiscCurveId).get
  private val gbp3MCurve = marketData.findValue(gbp3MCurveId).get
  private val usd3MCurve = marketData.findValue(usd3MCurveId).get
  private val usd6MCurve = marketData.findValue(usd6MCurveId).get

  property("USD-Disc parity") {
    check(
      forAll(yfGen) { yf =>
        val expected = usdDiscCurve.yValue(yf)
        val actual = MarketData.Zero.usdDisc(yf)
        expected == actual
      }
    )
  }

  property("GBP-Disc parity") {
    check(
      forAll(yfGen) { yf =>
        val expected = gbpDiscCurve.yValue(yf)
        val actual = MarketData.Zero.gbpDisc(yf)
        expected == actual
      }
    )
  }

  property("GBP-3ML parity") {
    check(
      forAll(yfGen) { yf =>
        val expected = gbp3MCurve.yValue(yf)
        val actual = MarketData.Zero.gbp3M(yf)
        expected == actual
      }
    )
  }

  property("USD-3ML parity") {
    check(
      forAll(yfGen) { yf =>
        val expected = usd3MCurve.yValue(yf)
        val actual = MarketData.Zero.usd3M(yf)
        expected == actual
      }
    )
  }

  property("USD-6ML parity") {
    check(
      forAll(yfGen) { yf =>
        val expected = usd6MCurve.yValue(yf)
        val actual = MarketData.Zero.usd6M(yf)
        expected == actual
      }
    )
  }

}
