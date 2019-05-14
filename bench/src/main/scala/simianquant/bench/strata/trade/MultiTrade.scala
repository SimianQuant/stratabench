package simianquant.bench.strata.trade

import java.util.concurrent.TimeUnit
import com.google.common.collect.ImmutableList
import com.opengamma.strata.calc.{Column, Results}
import com.opengamma.strata.measure.Measures
import org.openjdk.jmh.annotations._
import simianquant.strata.setup.SampleTrades

object MultiTradeData {

  @State(Scope.Benchmark)
  class FxData {
    val trades = ImmutableList.of(SampleTrades.fxForward, SampleTrades.fxSwap, SampleTrades.fra)
    val columns = ImmutableList.of(Column.of(Measures.PRESENT_VALUE), Column.of(Measures.PV01_CALIBRATED_BUCKETED))
  }

  @State(Scope.Benchmark)
  class RatesData {
    val trades = ImmutableList.of(
      SampleTrades.vanillaFixedVsLibor3mSwap,
      SampleTrades.basisLibor3mVsLibor6mWithSpreadSwap,
      SampleTrades.overnightAveragedWithSpreadVsLibor3mSwap,
      SampleTrades.fixedVsLibor3mWithFixingSwap,
      SampleTrades.fixedVsOvernightWithFixingSwap,
      SampleTrades.stub3mFixedVsLibor3mSwap,
      SampleTrades.stub1mFixedVsLibor3mSwap,
      SampleTrades.interpolatedStub3mFixedVsLibor6mSwap,
      SampleTrades.interpolatedStub4mFixedVsLibor6mSwap,
      SampleTrades.zeroCouponFixedVsLibor3mSwap,
      SampleTrades.compoundingFixedVsFedFundsSwap,
      SampleTrades.compoundingFedFundsVsLibor3mSwap,
      SampleTrades.compoundingLibor6mVsLibor3mSwap,
      SampleTrades.xCcyGbpLibor3mVsUsdLibor3mSwap,
      SampleTrades.xCcyUsdFixedVsGbpLibor3mSwap,
      SampleTrades.notionalExchangeSwap
    )
    val columns = ImmutableList.of(Column.of(Measures.PRESENT_VALUE), Column.of(Measures.PV01_CALIBRATED_BUCKETED))
  }

}

/** Benchmarks the evaluation of the PV01 of multiple trades
  *
  * @author Harshad Deo
  */
@BenchmarkMode(Array(Mode.SampleTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class MultiTrade {

  import MultiTradeData._
  import BaseTradeData._

  @Benchmark
  def fx(marketDataContainer: MarketDataContainer, calcRunnerMulti: CalculationRunnerMulti, data: FxData): Results = {
    calcRunnerMulti.runner.calculate(marketDataContainer.rules,
                                     data.trades,
                                     data.columns,
                                     marketDataContainer.marketData,
                                     marketDataContainer.refData)
  }

  @Benchmark
  def rates(marketDataContainer: MarketDataContainer,
            calcRunnerMulti: CalculationRunnerMulti,
            data: RatesData): Results = {
    calcRunnerMulti.runner.calculate(marketDataContainer.rules,
                                     data.trades,
                                     data.columns,
                                     marketDataContainer.marketData,
                                     marketDataContainer.refData)
  }

}
