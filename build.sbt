lazy val stratabench = project
  .in(file("."))
  .settings(
    name := "stratebench",
    organization := "com.simianquant",
    version := Settings.versions.project,
    scalaVersion := Settings.versions.scala,
    scalacOptions ++= List(
      ("-Xlint:adapted-args,nullary-unit,inaccessible,nullary-override,infer-any,doc-detached,private-shadow," +
        "type-parameter-shadow,poly-implicit-overload,option-implicit,delayedinit-select,by-name-right-associative," +
        "package-object-classes,unsound-match,stars-align,constant"),
      "-Ywarn-unused:imports,patvars,privates,locals",
      "-opt:l:method",
      "-Ywarn-unused-import",
      "-deprecation",
      "-unchecked",
      "-explaintypes",
      "-encoding",
      "UTF-8",
      "-feature",
      "-Xlog-reflective-calls",
      "-Ywarn-inaccessible",
      "-Ywarn-infer-any",
      "-Ywarn-nullary-override",
      "-Ywarn-nullary-unit",
      "-Xfuture"
    ),
    libraryDependencies ++= Seq(
      "com.opengamma.strata" % "strata-measure" % Settings.versions.strata,
      "com.opengamma.strata" % "strata-report" % Settings.versions.strata,
      "com.opengamma.strata" % "strata-loader" % Settings.versions.strata
    ),
    resourceDirectory in Jmh := (resourceDirectory in Compile).value
  )
  .enablePlugins(JmhPlugin)
