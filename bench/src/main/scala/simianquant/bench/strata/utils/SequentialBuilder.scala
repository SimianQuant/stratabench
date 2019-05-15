package simianquant.bench.strata.utils

object SequentialBuilder {

  def apply(lb: Double, ub: Double, cnt: Int): Array[Double] = {
    val incr = (ub - lb) / cnt
    val res = new Array[Double](cnt)
    var i = 0
    while (i < cnt) {
      res(i) = lb + incr * i
      i += 1
    }
    res
  }
}
