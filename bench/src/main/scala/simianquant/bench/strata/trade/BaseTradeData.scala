package simianquant.bench.strata.trade

import com.opengamma.strata.basics.ReferenceData
import com.opengamma.strata.examples.marketdata.ExampleMarketData
import com.opengamma.strata.calc.{CalculationRules, CalculationRunner}
import com.opengamma.strata.measure.StandardComponents
import org.openjdk.jmh.annotations.{Scope, State}
import simianquant.strata.setup.Constants

/** State definition common to all benchmarks
  *
  * @author Harshad Deo
  */
object BaseTradeData {

  @State(Scope.Benchmark)
  class MarketDataContainer {
    private val marketDataBuilder = ExampleMarketData.builder()
    val marketData = marketDataBuilder.buildSnapshot(Constants.valuationDate)

    val functions = StandardComponents.calculationFunctions()
    val rules = CalculationRules.of(functions, marketDataBuilder.ratesLookup(Constants.valuationDate))

    val refData = ReferenceData.standard()
  }

  @State(Scope.Thread)
  class CalculationRunnerMulti {
    val runner = CalculationRunner.ofMultiThreaded()
  }

}
