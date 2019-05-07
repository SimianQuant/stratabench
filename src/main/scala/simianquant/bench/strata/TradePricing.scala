package simianquant.bench.strata

import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.{BenchmarkMode, Mode, OutputTimeUnit}

/** Benchmarks the evaluation of the NPV of a given trade
  *
  * @author Harshad Deo
  */
@BenchmarkMode(Array(Mode.SampleTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class TradeNPV {}
