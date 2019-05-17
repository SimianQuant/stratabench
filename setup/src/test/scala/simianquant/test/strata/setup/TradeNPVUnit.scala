package simianquant.test.strata.setup

import com.google.common.collect.ImmutableList
import com.opengamma.strata.calc.Column
import com.opengamma.strata.basics.ReferenceData
import com.opengamma.strata.calc.{CalculationRules, CalculationRunner}
import com.opengamma.strata.examples.marketdata.ExampleMarketData
import com.opengamma.strata.measure.{Measures, StandardComponents}
import java.time.LocalDate
import org.scalatest.FlatSpec
import simianquant.strata.setup.{Constants, SampleTrades}

final class TradeNPVUnit extends FlatSpec {

  private val marketDataBuilder = ExampleMarketData.builder();
  private val marketData = marketDataBuilder.buildSnapshot(Constants.valuationDate);

  private val functions = StandardComponents.calculationFunctions();
  private val rules = CalculationRules.of(functions, marketDataBuilder.ratesLookup(Constants.valuationDate));

  private val refData = ReferenceData.standard();

  private val runner = CalculationRunner.ofMultiThreaded()

  private val trades = ImmutableList.of(SampleTrades.bullet)

  private val columns = ImmutableList.of(Column.of(Measures.PRESENT_VALUE))

  private val results = runner.calculate(rules, trades, columns, marketData, refData)

  it should "pass fx-forward test" in {}

  it should "pass fx-swap test" in {}

  it should "pass fra test" in {}

  it should "pass bullet unit" in {}

}
