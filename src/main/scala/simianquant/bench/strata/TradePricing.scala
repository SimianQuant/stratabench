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

  @State(Scope.Benchmark)
  class FxForwardData {
    val trades = ImmutableList.of(SampleTrades.fxForward)
    val columns = ImmutableList.of(Column.of(Measures.PRESENT_VALUE))
  }

  @State(Scope.Thread)
  class CalculationRunnerMulti {
    val runner = CalculationRunner.ofMultiThreaded()
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
  def fxForwardBenchmark(marketDataContainer: MarketDataContainer,
                         calcRunnerMulti: CalculationRunnerMulti,
                         data: FxForwardData): Results = {
    calcRunnerMulti.runner.calculate(marketDataContainer.rules,
                                     data.trades,
                                     data.columns,
                                     marketDataContainer.marketData,
                                     marketDataContainer.refData);
  }

}
