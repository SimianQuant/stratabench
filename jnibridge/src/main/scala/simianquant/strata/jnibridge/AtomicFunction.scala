package simianquant.strata.jnibridge

import ch.jodersky.jni.nativeLoader

/** Bridge to JNI invocations of common mathematical functions
  *
  * @author Harshad Deo
  */
@nativeLoader("jnibridge0")
class AtomicFunction {

  /** Evaluates the standard cumulative normal distribution
    *
    * @author Harshad Deo
    */
  @native
  def normalCdf(x: Double): Double

  /** Evaluates the standard normal quantile function (inverse CDF)
    *
    * @author Harshad Deo
    */
  @native
  def normalQuantile(x: Double): Double

}
