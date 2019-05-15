package simianquant.strata.jnibridge

import ch.jodersky.jni.nativeLoader

/** Bridge to JNI invocations of common mathematical functions
  *
  * @author Harshad Deo
  */
@nativeLoader("jnibridge0")
class JniBridge {

  /** Evaluates the standard cumulative normal distribution
    *
    * @author Harshad Deo
    */
  @native
  def normalCdf(x: Double): Double

}
