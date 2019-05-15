#include <cmath>
#include <jni.h>
#include <boost/math/special_functions/erf.hpp>

#ifndef _Included_simianquant_strata_jnibridge_AtomicFunction
#define _Included_simianquant_strata_jnibridge_AtomicFunction
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jdouble JNICALL Java_simianquant_strata_jnibridge_AtomicFunction_normalCdf
  (JNIEnv * env, jobject obj, jdouble arg){
  	return (0.5 * erfc(-arg * M_SQRT1_2));
  }

JNIEXPORT jdouble JNICALL Java_simianquant_strata_jnibridge_AtomicFunction_normalQuantile
  (JNIEnv * env, jobject obj, jdouble arg){
  	return -M_SQRT2 * boost::math::erfc_inv(2 * arg);
  }

#ifdef __cplusplus
}
#endif
#endif