package simianquant.test.strata.setup

import com.google.common.collect.ImmutableList
import com.opengamma.strata.calc.Column
import com.opengamma.strata.basics.currency.CurrencyAmount
import com.opengamma.strata.basics.ReferenceData
import com.opengamma.strata.calc.{CalculationRules, CalculationRunner}
import com.opengamma.strata.examples.marketdata.ExampleMarketData
import com.opengamma.strata.measure.{Measures, StandardComponents}
import org.scalatest.FlatSpec
import simianquant.strata.setup.{Constants, SampleTrades}

final class TradeNPVUnit extends FlatSpec {

  private val _eps = 1e-10

  private val marketDataBuilder = ExampleMarketData.builder();
  private val marketData = marketDataBuilder.buildSnapshot(Constants.valuationDate);

  private val functions = StandardComponents.calculationFunctions();
  private val rules = CalculationRules.of(functions, marketDataBuilder.ratesLookup(Constants.valuationDate));

  private val refData = ReferenceData.standard();

  private val runner = CalculationRunner.ofMultiThreaded()

  private val trades =
    ImmutableList.of(SampleTrades.fxForward, SampleTrades.fxSwap, SampleTrades.fra, SampleTrades.bullet)

  private val columns = ImmutableList.of(Column.of(Measures.PRESENT_VALUE))

  private val results = runner.calculate(rules, trades, columns, marketData, refData)

  it should "pass fx-forward test" in {
    val fxRate = 1.62
    val notional = 10000
    val paymentDate = SampleTrades.fxForward.getProduct.getPaymentDate

    val gbpDf = MarketData.Df.gbpDisc(paymentDate)
    val usdDf = MarketData.Df.usdDisc(paymentDate)
    val fwdRate = MarketData.FxSpot.GBPUSD * gbpDf / usdDf

    val calcNpv = ((fwdRate - fxRate) * notional / fwdRate) * gbpDf
    val expectedNpv = results.get(0, 0, classOf[CurrencyAmount]).getValue().getAmount()

    assert(math.abs(calcNpv - expectedNpv) < _eps)
  }

  it should "pass fx-swap test" in {
    val notional = 10000
    val nearRate = 1.62
    val points = 0.03
    val fxSwap = SampleTrades.fxSwap.getProduct()

    val nearLeg = fxSwap.getNearLeg()
    val nearPaymentDate = nearLeg.getPaymentDate
    val gbpDfNear = MarketData.Df.gbpDisc(nearPaymentDate)
    val usdDfNear = MarketData.Df.usdDisc(nearPaymentDate)
    val fwdRateNear = MarketData.FxSpot.GBPUSD * gbpDfNear / usdDfNear
    val calcNpvNear = ((fwdRateNear - nearRate) * notional / fwdRateNear) * gbpDfNear

    val farLeg = fxSwap.getFarLeg()
    val farPaymentDate = farLeg.getPaymentDate
    val gbpDfFar = MarketData.Df.gbpDisc(farPaymentDate)
    val usdDfFar = MarketData.Df.usdDisc(farPaymentDate)
    val fwdRateFar = MarketData.FxSpot.GBPUSD * gbpDfFar / usdDfFar
    val calcNpvFar = ((fwdRateFar - nearRate - points) * notional / fwdRateFar) * gbpDfFar

    val calcNpvNet = calcNpvNear - calcNpvFar
    val expectedNpv = results.get(1, 0, classOf[CurrencyAmount]).getValue().getAmount()

    assert(math.abs(calcNpvNet - expectedNpv) < _eps)
  }

  it should "pass fra test" in {
    val fra = SampleTrades.fra.getProduct()
    val startDate = fra.getStartDate()
    val endDate = fra.getEndDate()

    val forecastStartDf = MarketData.Df.usd3M(startDate)
    val forecastEndDf = MarketData.Df.usd3M(endDate)
    val accrual = fra.getDayCount().yearFraction(startDate, endDate)
    val calcRate = (forecastStartDf / forecastEndDf - 1) / accrual

    val paymentDate = fra.getPaymentDate().getUnadjusted()
    val paymentDf = MarketData.Df.usdDisc(paymentDate)

    val calcNpv = (fra.getFixedRate() - calcRate) * accrual * paymentDf * fra.getNotional() / (1 + calcRate * accrual)
    val expectedNpv = results.get(2, 0, classOf[CurrencyAmount]).getValue().getAmount()

    assert(math.abs(calcNpv - expectedNpv) < _eps)
  }

  it should "pass bullet unit" in {
    val bullet = SampleTrades.bullet.getProduct()
    val paymentDf = MarketData.Df.gbpDisc(bullet.getDate().getUnadjusted())
    val calcNpv = -paymentDf * bullet.getValue().getAmount()
    val expectedNpv = results.get(3, 0, classOf[CurrencyAmount]).getValue().getAmount()

    assert(math.abs(calcNpv - expectedNpv) < _eps)
  }

}
