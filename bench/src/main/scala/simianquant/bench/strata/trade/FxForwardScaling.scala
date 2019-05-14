package simianquant.bench.strata.trade

import java.util.concurrent.TimeUnit
import com.google.common.collect.ImmutableList
import com.opengamma.strata.calc.{Column, Results}
import com.opengamma.strata.measure.Measures
import com.opengamma.strata.product.Trade
import org.openjdk.jmh.annotations._
import simianquant.strata.setup.SampleTrades

object FxForwardScalingData {

  abstract class BaseFxData(cnt: Int) {
    private val builder = ImmutableList.builder[Trade]
    private var ctr = 0
    while (ctr < cnt) {
      builder.add(SampleTrades.fxForward)
      ctr += 1
    }
    val trades = builder.build()

    val columns = ImmutableList.of(Column.of(Measures.PRESENT_VALUE))
  }

  @State(Scope.Benchmark)
  class FxData20 extends BaseFxData(20)

  @State(Scope.Benchmark)
  class FxData40 extends BaseFxData(40)

  @State(Scope.Benchmark)
  class FxData60 extends BaseFxData(60)

  @State(Scope.Benchmark)
  class FxData80 extends BaseFxData(80)

  @State(Scope.Benchmark)
  class FxData100 extends BaseFxData(100)

}

/** Benchmarks the scaling properties of the FXForward pricer to estimate overhead
  *
  * @author Harshad Deo
  */
@BenchmarkMode(Array(Mode.SampleTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class FxForwardScaling {

  import FxForwardScalingData._
  import BaseTradeData._

  @Benchmark
  def cnt020(marketDataContainer: MarketDataContainer,
             calcRunnerMulti: CalculationRunnerMulti,
             data: FxData20): Results = {
    calcRunnerMulti.runner.calculate(marketDataContainer.rules,
                                     data.trades,
                                     data.columns,
                                     marketDataContainer.marketData,
                                     marketDataContainer.refData);
  }

  @Benchmark
  def cnt040(marketDataContainer: MarketDataContainer,
             calcRunnerMulti: CalculationRunnerMulti,
             data: FxData40): Results = {
    calcRunnerMulti.runner.calculate(marketDataContainer.rules,
                                     data.trades,
                                     data.columns,
                                     marketDataContainer.marketData,
                                     marketDataContainer.refData);
  }

  @Benchmark
  def cnt060(marketDataContainer: MarketDataContainer,
             calcRunnerMulti: CalculationRunnerMulti,
             data: FxData60): Results = {
    calcRunnerMulti.runner.calculate(marketDataContainer.rules,
                                     data.trades,
                                     data.columns,
                                     marketDataContainer.marketData,
                                     marketDataContainer.refData);
  }

  @Benchmark
  def cnt080(marketDataContainer: MarketDataContainer,
             calcRunnerMulti: CalculationRunnerMulti,
             data: FxData80): Results = {
    calcRunnerMulti.runner.calculate(marketDataContainer.rules,
                                     data.trades,
                                     data.columns,
                                     marketDataContainer.marketData,
                                     marketDataContainer.refData);
  }

  @Benchmark
  def cnt100(marketDataContainer: MarketDataContainer,
             calcRunnerMulti: CalculationRunnerMulti,
             data: FxData100): Results = {
    calcRunnerMulti.runner.calculate(marketDataContainer.rules,
                                     data.trades,
                                     data.columns,
                                     marketDataContainer.marketData,
                                     marketDataContainer.refData);
  }

}
