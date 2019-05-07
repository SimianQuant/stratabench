package simianquant.bench.strata

import java.time.LocalDate
import java.util.concurrent.TimeUnit
import com.google.common.collect.ImmutableList
import com.opengamma.strata.basics.ReferenceData
import com.opengamma.strata.calc.{CalculationRules, CalculationRunner, Column, Results}
import com.opengamma.strata.examples.marketdata.ExampleMarketData
import com.opengamma.strata.measure.{Measures, StandardComponents}
import org.openjdk.jmh.annotations._

object TradeNPVData {

  @State(Scope.Benchmark)
  class MarketDataContainer {
    private val valuationDate = LocalDate.of(2014, 1, 22);
    private val marketDataBuilder = ExampleMarketData.builder();
    val marketData = marketDataBuilder.buildSnapshot(valuationDate);

    // // the complete set of rules for calculating measures
    val functions = StandardComponents.calculationFunctions();
    val rules = CalculationRules.of(functions, marketDataBuilder.ratesLookup(valuationDate));

    // // the reference data, such as holidays and securities
    val refData = ReferenceData.standard();
  }

  @State(Scope.Thread)
  class CalculationRunnerMulti {
    val runner = CalculationRunner.ofMultiThreaded()
  }

  @State(Scope.Benchmark)
  class FxForwardData {
    val trades = ImmutableList.of(SampleTrades.fxForward)
    val columns = ImmutableList.of(Column.of(Measures.PRESENT_VALUE))
  }

  @State(Scope.Benchmark)
  class FxSwapData {
    val trades = ImmutableList.of(SampleTrades.fxSwap)
    val columns = ImmutableList.of(Column.of(Measures.PRESENT_VALUE))
  }

  @State(Scope.Benchmark)
  class FraData {
    val trades = ImmutableList.of(SampleTrades.fra)
    val columns = ImmutableList.of(Column.of(Measures.PRESENT_VALUE))
  }

  @State(Scope.Benchmark)
  class BulletData {
    val trades = ImmutableList.of(SampleTrades.bullet)
    val columns = ImmutableList.of(Column.of(Measures.PRESENT_VALUE))
  }

  @State(Scope.Benchmark)
  class VanillaFixedVsLibor3mSwapData {
    val trades = ImmutableList.of(SampleTrades.vanillaFixedVsLibor3mSwap)
    val columns = ImmutableList.of(Column.of(Measures.PRESENT_VALUE))
  }

  @State(Scope.Benchmark)
  class BasisLibor3mVsLibor6mWithSpreadSwap {
    val trades = ImmutableList.of(SampleTrades.basisLibor3mVsLibor6mWithSpreadSwap)
    val columns = ImmutableList.of(Column.of(Measures.PRESENT_VALUE))
  }

  @State(Scope.Benchmark)
  class OvernightAveragedWithSpreadVsLibor3mSwap {
    val trades = ImmutableList.of(SampleTrades.overnightAveragedWithSpreadVsLibor3mSwap)
    val columns = ImmutableList.of(Column.of(Measures.PRESENT_VALUE))
  }

  @State(Scope.Benchmark)
  class FixedVsLibor3mWithFixingSwap {
    val trades = ImmutableList.of(SampleTrades.fixedVsLibor3mWithFixingSwap)
    val columns = ImmutableList.of(Column.of(Measures.PRESENT_VALUE))
  }

  @State(Scope.Benchmark)
  class FixedVsOvernightWithFixingSwap {
    val trades = ImmutableList.of(SampleTrades.fixedVsOvernightWithFixingSwap)
    val columns = ImmutableList.of(Column.of(Measures.PRESENT_VALUE))
  }

  @State(Scope.Benchmark)
  class Stub3mFixedVsLibor3mSwap {
    val trades = ImmutableList.of(SampleTrades.stub3mFixedVsLibor3mSwap)
    val columns = ImmutableList.of(Column.of(Measures.PRESENT_VALUE))
  }

  @State(Scope.Benchmark)
  class Stub1mFixedVsLibor3mSwap {
    val trades = ImmutableList.of(SampleTrades.stub1mFixedVsLibor3mSwap)
    val columns = ImmutableList.of(Column.of(Measures.PRESENT_VALUE))
  }

  @State(Scope.Benchmark)
  class InterpolatedStub3mFixedVsLibor6mSwap {
    val trades = ImmutableList.of(SampleTrades.interpolatedStub3mFixedVsLibor6mSwap)
    val columns = ImmutableList.of(Column.of(Measures.PRESENT_VALUE))
  }

  @State(Scope.Benchmark)
  class InterpolatedStub4mFixedVsLibor6mSwap {
    val trades = ImmutableList.of(SampleTrades.interpolatedStub4mFixedVsLibor6mSwap)
    val columns = ImmutableList.of(Column.of(Measures.PRESENT_VALUE))
  }

  @State(Scope.Benchmark)
  class ZeroCouponFixedVsLibor3mSwap {
    val trades = ImmutableList.of(SampleTrades.zeroCouponFixedVsLibor3mSwap)
    val columns = ImmutableList.of(Column.of(Measures.PRESENT_VALUE))
  }

  @State(Scope.Benchmark)
  class CompoundingFixedVsFedFundsSwap {
    val trades = ImmutableList.of(SampleTrades.compoundingFixedVsFedFundsSwap)
    val columns = ImmutableList.of(Column.of(Measures.PRESENT_VALUE))
  }

  @State(Scope.Benchmark)
  class CompoundingFedFundsVsLibor3mSwap {
    val trades = ImmutableList.of(SampleTrades.compoundingFedFundsVsLibor3mSwap)
    val columns = ImmutableList.of(Column.of(Measures.PRESENT_VALUE))
  }

  @State(Scope.Benchmark)
  class CompoundingLibor6mVsLibor3mSwap {
    val trades = ImmutableList.of(SampleTrades.compoundingLibor6mVsLibor3mSwap)
    val columns = ImmutableList.of(Column.of(Measures.PRESENT_VALUE))
  }

  @State(Scope.Benchmark)
  class XCcyGbpLibor3mVsUsdLibor3mSwap {
    val trades = ImmutableList.of(SampleTrades.xCcyGbpLibor3mVsUsdLibor3mSwap)
    val columns = ImmutableList.of(Column.of(Measures.PRESENT_VALUE))
  }

  @State(Scope.Benchmark)
  class XCcyUsdFixedVsGbpLibor3mSwap {
    val trades = ImmutableList.of(SampleTrades.xCcyUsdFixedVsGbpLibor3mSwap)
    val columns = ImmutableList.of(Column.of(Measures.PRESENT_VALUE))
  }

  @State(Scope.Benchmark)
  class NotionalExchangeSwap {
    val trades = ImmutableList.of(SampleTrades.notionalExchangeSwap)
    val columns = ImmutableList.of(Column.of(Measures.PRESENT_VALUE))
  }

}

/** Benchmarks the evaluation of the NPV of a given trade
  *
  * @author Harshad Deo
  */
@BenchmarkMode(Array(Mode.SampleTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class TradeNPV {

  import TradeNPVData._

  @Benchmark
  def fxForward(marketDataContainer: MarketDataContainer,
                calcRunnerMulti: CalculationRunnerMulti,
                data: FxForwardData): Results = {
    calcRunnerMulti.runner.calculate(marketDataContainer.rules,
                                     data.trades,
                                     data.columns,
                                     marketDataContainer.marketData,
                                     marketDataContainer.refData);
  }

  @Benchmark
  def fxSwap(marketDataContainer: MarketDataContainer,
             calcRunnerMulti: CalculationRunnerMulti,
             data: FxSwapData): Results = {
    calcRunnerMulti.runner.calculate(marketDataContainer.rules,
                                     data.trades,
                                     data.columns,
                                     marketDataContainer.marketData,
                                     marketDataContainer.refData);
  }

  @Benchmark
  def fra(marketDataContainer: MarketDataContainer, calcRunnerMulti: CalculationRunnerMulti, data: FraData): Results = {
    calcRunnerMulti.runner.calculate(marketDataContainer.rules,
                                     data.trades,
                                     data.columns,
                                     marketDataContainer.marketData,
                                     marketDataContainer.refData);
  }

  @Benchmark
  def bullet(marketDataContainer: MarketDataContainer,
             calcRunnerMulti: CalculationRunnerMulti,
             data: BulletData): Results = {
    calcRunnerMulti.runner.calculate(marketDataContainer.rules,
                                     data.trades,
                                     data.columns,
                                     marketDataContainer.marketData,
                                     marketDataContainer.refData);
  }

  @Benchmark
  def vanillaFixedVsLibor3mSwap(marketDataContainer: MarketDataContainer,
                                calcRunnerMulti: CalculationRunnerMulti,
                                data: VanillaFixedVsLibor3mSwapData): Results = {
    calcRunnerMulti.runner.calculate(marketDataContainer.rules,
                                     data.trades,
                                     data.columns,
                                     marketDataContainer.marketData,
                                     marketDataContainer.refData);
  }

  @Benchmark
  def basisLibor3mVsLibor6mWithSpreadSwap(marketDataContainer: MarketDataContainer,
                                          calcRunnerMulti: CalculationRunnerMulti,
                                          data: BasisLibor3mVsLibor6mWithSpreadSwap): Results = {
    calcRunnerMulti.runner.calculate(marketDataContainer.rules,
                                     data.trades,
                                     data.columns,
                                     marketDataContainer.marketData,
                                     marketDataContainer.refData);
  }

  @Benchmark
  def overnightAveragedWithSpreadVsLibor3mSwap(marketDataContainer: MarketDataContainer,
                                               calcRunnerMulti: CalculationRunnerMulti,
                                               data: OvernightAveragedWithSpreadVsLibor3mSwap): Results = {
    calcRunnerMulti.runner.calculate(marketDataContainer.rules,
                                     data.trades,
                                     data.columns,
                                     marketDataContainer.marketData,
                                     marketDataContainer.refData);
  }

  @Benchmark
  def fixedVsLibor3mWithFixingSwap(marketDataContainer: MarketDataContainer,
                                   calcRunnerMulti: CalculationRunnerMulti,
                                   data: FixedVsLibor3mWithFixingSwap): Results = {
    calcRunnerMulti.runner.calculate(marketDataContainer.rules,
                                     data.trades,
                                     data.columns,
                                     marketDataContainer.marketData,
                                     marketDataContainer.refData);
  }

  @Benchmark
  def fixedVsOvernightWithFixingSwap(marketDataContainer: MarketDataContainer,
                                     calcRunnerMulti: CalculationRunnerMulti,
                                     data: FixedVsOvernightWithFixingSwap): Results = {
    calcRunnerMulti.runner.calculate(marketDataContainer.rules,
                                     data.trades,
                                     data.columns,
                                     marketDataContainer.marketData,
                                     marketDataContainer.refData);
  }

  @Benchmark
  def stub3mFixedVsLibor3mSwap(marketDataContainer: MarketDataContainer,
                               calcRunnerMulti: CalculationRunnerMulti,
                               data: Stub3mFixedVsLibor3mSwap): Results = {
    calcRunnerMulti.runner.calculate(marketDataContainer.rules,
                                     data.trades,
                                     data.columns,
                                     marketDataContainer.marketData,
                                     marketDataContainer.refData);
  }

  @Benchmark
  def stub1mFixedVsLibor3mSwap(marketDataContainer: MarketDataContainer,
                               calcRunnerMulti: CalculationRunnerMulti,
                               data: Stub1mFixedVsLibor3mSwap): Results = {
    calcRunnerMulti.runner.calculate(marketDataContainer.rules,
                                     data.trades,
                                     data.columns,
                                     marketDataContainer.marketData,
                                     marketDataContainer.refData);
  }

  @Benchmark
  def interpolatedStub3mFixedVsLibor6mSwap(marketDataContainer: MarketDataContainer,
                                           calcRunnerMulti: CalculationRunnerMulti,
                                           data: InterpolatedStub3mFixedVsLibor6mSwap): Results = {
    calcRunnerMulti.runner.calculate(marketDataContainer.rules,
                                     data.trades,
                                     data.columns,
                                     marketDataContainer.marketData,
                                     marketDataContainer.refData);
  }

  @Benchmark
  def interpolatedStub4mFixedVsLibor6mSwap(marketDataContainer: MarketDataContainer,
                                           calcRunnerMulti: CalculationRunnerMulti,
                                           data: InterpolatedStub4mFixedVsLibor6mSwap): Results = {
    calcRunnerMulti.runner.calculate(marketDataContainer.rules,
                                     data.trades,
                                     data.columns,
                                     marketDataContainer.marketData,
                                     marketDataContainer.refData);
  }

  @Benchmark
  def zeroCouponFixedVsLibor3mSwap(marketDataContainer: MarketDataContainer,
                                   calcRunnerMulti: CalculationRunnerMulti,
                                   data: ZeroCouponFixedVsLibor3mSwap): Results = {
    calcRunnerMulti.runner.calculate(marketDataContainer.rules,
                                     data.trades,
                                     data.columns,
                                     marketDataContainer.marketData,
                                     marketDataContainer.refData);
  }

  @Benchmark
  def compoundingFixedVsFedFundsSwap(marketDataContainer: MarketDataContainer,
                                     calcRunnerMulti: CalculationRunnerMulti,
                                     data: CompoundingFixedVsFedFundsSwap): Results = {
    calcRunnerMulti.runner.calculate(marketDataContainer.rules,
                                     data.trades,
                                     data.columns,
                                     marketDataContainer.marketData,
                                     marketDataContainer.refData);
  }

  @Benchmark
  def compoundingFedFundsVsLibor3mSwap(marketDataContainer: MarketDataContainer,
                                       calcRunnerMulti: CalculationRunnerMulti,
                                       data: CompoundingFedFundsVsLibor3mSwap): Results = {
    calcRunnerMulti.runner.calculate(marketDataContainer.rules,
                                     data.trades,
                                     data.columns,
                                     marketDataContainer.marketData,
                                     marketDataContainer.refData);
  }

  @Benchmark
  def compoundingLibor6mVsLibor3mSwap(marketDataContainer: MarketDataContainer,
                                      calcRunnerMulti: CalculationRunnerMulti,
                                      data: CompoundingLibor6mVsLibor3mSwap): Results = {
    calcRunnerMulti.runner.calculate(marketDataContainer.rules,
                                     data.trades,
                                     data.columns,
                                     marketDataContainer.marketData,
                                     marketDataContainer.refData);
  }
  @Benchmark
  def xCcyGbpLibor3mVsUsdLibor3mSwap(marketDataContainer: MarketDataContainer,
                                     calcRunnerMulti: CalculationRunnerMulti,
                                     data: XCcyGbpLibor3mVsUsdLibor3mSwap): Results = {
    calcRunnerMulti.runner.calculate(marketDataContainer.rules,
                                     data.trades,
                                     data.columns,
                                     marketDataContainer.marketData,
                                     marketDataContainer.refData);
  }

  @Benchmark
  def xCcyUsdFixedVsGbpLibor3mSwap(marketDataContainer: MarketDataContainer,
                                   calcRunnerMulti: CalculationRunnerMulti,
                                   data: XCcyUsdFixedVsGbpLibor3mSwap): Results = {
    calcRunnerMulti.runner.calculate(marketDataContainer.rules,
                                     data.trades,
                                     data.columns,
                                     marketDataContainer.marketData,
                                     marketDataContainer.refData);
  }

  @Benchmark
  def notionalExchangeSwap(marketDataContainer: MarketDataContainer,
                           calcRunnerMulti: CalculationRunnerMulti,
                           data: NotionalExchangeSwap): Results = {
    calcRunnerMulti.runner.calculate(marketDataContainer.rules,
                                     data.trades,
                                     data.columns,
                                     marketDataContainer.marketData,
                                     marketDataContainer.refData);
  }

}
