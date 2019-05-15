package simianquant.bench.strata.utils

object StaggeredBuilder {

  def apply(lb: Double, ub: Double, cnt: Int): Array[Double] = {
    val incr = (ub - lb) / cnt
    val pincr = (ub - lb) / 5

    var p0 = lb
    var p1 = lb + pincr
    var p2 = lb + pincr * 3 - incr
    var p3 = lb + pincr * 3
    var p4 = ub - incr

    val res = new Array[Double](cnt)
    var i = 0
    while (i < cnt) {
      (i % 5) match {
        case 0 =>
          res(i) = p0
          p0 += incr
        case 1 =>
          res(i) = p4
          p4 -= incr
        case 2 =>
          res(i) = p1
          p1 += incr
        case 3 =>
          res(i) = p3
          p3 += incr
        case 4 =>
          res(i) = p2
          p2 -= incr
      }
      i += 1
    }
    res
  }
}
