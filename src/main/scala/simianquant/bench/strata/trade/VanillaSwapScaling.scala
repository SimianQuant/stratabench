package simianquant.bench.strata.trade

import java.util.concurrent.TimeUnit
import com.google.common.collect.ImmutableList
import com.opengamma.strata.calc.{Column, Results}
import com.opengamma.strata.measure.Measures
import com.opengamma.strata.product.Trade
import org.openjdk.jmh.annotations._
import simianquant.bench.strata.SampleTrades

object VanillaSwapScalingData {

  abstract class BaseSwapData(cnt: Int) {
    private val builder = ImmutableList.builder[Trade]
    private var ctr = 0
    while (ctr < cnt) {
      builder.add(SampleTrades.vanillaFixedVsLibor3mSwap)
      ctr += 1
    }
    val trades = builder.build()

    val columns = ImmutableList.of(Column.of(Measures.PRESENT_VALUE))
  }

  @State(Scope.Benchmark)
  class SwapData20 extends BaseSwapData(20)

  @State(Scope.Benchmark)
  class SwapData40 extends BaseSwapData(40)

  @State(Scope.Benchmark)
  class SwapData60 extends BaseSwapData(60)

  @State(Scope.Benchmark)
  class SwapData80 extends BaseSwapData(80)

  @State(Scope.Benchmark)
  class SwapData100 extends BaseSwapData(100)

}

/** Benchmarks the scaling properties of the VanillaSwap pricer to estimate overhead
  *
  * @author Harshad Deo
  */
@BenchmarkMode(Array(Mode.SampleTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class VanillaSwapScaling {

  import VanillaSwapScalingData._
  import BaseTradeData._

  @Benchmark
  def cnt020(marketDataContainer: MarketDataContainer,
             calcRunnerMulti: CalculationRunnerMulti,
             data: SwapData20): Results = {
    calcRunnerMulti.runner.calculate(marketDataContainer.rules,
                                     data.trades,
                                     data.columns,
                                     marketDataContainer.marketData,
                                     marketDataContainer.refData);
  }

  @Benchmark
  def cnt040(marketDataContainer: MarketDataContainer,
             calcRunnerMulti: CalculationRunnerMulti,
             data: SwapData40): Results = {
    calcRunnerMulti.runner.calculate(marketDataContainer.rules,
                                     data.trades,
                                     data.columns,
                                     marketDataContainer.marketData,
                                     marketDataContainer.refData);
  }

  @Benchmark
  def cnt060(marketDataContainer: MarketDataContainer,
             calcRunnerMulti: CalculationRunnerMulti,
             data: SwapData60): Results = {
    calcRunnerMulti.runner.calculate(marketDataContainer.rules,
                                     data.trades,
                                     data.columns,
                                     marketDataContainer.marketData,
                                     marketDataContainer.refData);
  }

  @Benchmark
  def cnt080(marketDataContainer: MarketDataContainer,
             calcRunnerMulti: CalculationRunnerMulti,
             data: SwapData80): Results = {
    calcRunnerMulti.runner.calculate(marketDataContainer.rules,
                                     data.trades,
                                     data.columns,
                                     marketDataContainer.marketData,
                                     marketDataContainer.refData);
  }

  @Benchmark
  def cnt100(marketDataContainer: MarketDataContainer,
             calcRunnerMulti: CalculationRunnerMulti,
             data: SwapData100): Results = {
    calcRunnerMulti.runner.calculate(marketDataContainer.rules,
                                     data.trades,
                                     data.columns,
                                     marketDataContainer.marketData,
                                     marketDataContainer.refData);
  }

}
