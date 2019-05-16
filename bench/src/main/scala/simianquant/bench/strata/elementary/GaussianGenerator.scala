package simianquant.bench.strata.elementary

import com.opengamma.strata.math.impl.cern.{MersenneTwister64, Normal}
import org.apache.commons.math3.random.{MersenneTwister => ACM64}
import java.util.concurrent.TimeUnit
import java.util.Date
import org.openjdk.jmh.infra.Blackhole
import org.openjdk.jmh.annotations._

object GaussianGenerator {

  abstract class BaseData {
    val cnt = 1 << 10
  }

  @State(Scope.Thread)
  class StrataData extends BaseData {
    val impl = new Normal(0, 1, new MersenneTwister64(new Date()))
  }

  @State(Scope.Thread)
  class ACMData extends BaseData {
    val impl = new ACM64(System.currentTimeMillis)
  }

}

/** Benchmarks Strata's implementations of a Gaussian Generator against Apache Commons Math, both using a Mersenne
  * Twister. Since the algorithm is stateful, generating multiple random numbers using JNI is tricky at best. Therefore, that
  * approach is not considered here
  *
  * @author Harshad Deo
  */
@BenchmarkMode(Array(Mode.SampleTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class GaussianGenerator {

  import GaussianGenerator._

  @Benchmark
  def strata(data: StrataData, bh: Blackhole): Unit = {
    var ctr = 0
    while (ctr < data.cnt) {
      bh.consume(data.impl.nextDouble())
      ctr += 1
    }
  }

  @Benchmark
  def acm(data: ACMData, bh: Blackhole): Unit = {
    var ctr = 0
    while (ctr < data.cnt) {
      bh.consume(data.impl.nextGaussian())
      ctr += 1
    }
  }

}
