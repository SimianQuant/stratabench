Strata Bench
===

[Strata](https://github.com/OpenGamma/Strata) is an award winning open source analytics and market risk library published 
by OpenGamma. The company is [opaque](http://strata.opengamma.io/performance/) about the performance of the library and 
its subcomponents, with vague references to closed source prototypes and benchmarks run a decade ago. This project aims
to provide rigourous benchmarks for the library and its subcomponents using 
[Java Microbenchmark Harness](https://openjdk.java.net/projects/code-tools/jmh/). These benchmarks are not intended to 
be comprehensive, and are written from the perspective of someone using Strata to perform calculations, not to model the domain. 

For convenience, the benchmarks are written in Scala using [sbt-jmh](https://github.com/ktoso/sbt-jmh).

## Results

A summary of the results is presented on the SimianQuant blog.

1. [Elementary Functions](https://simianquant.com/blog/stratabenchelementary/)
1. [Black Formula](https://simianquant.com/blog/stratabenchblack/)