def commonSettings(nameStr: String) = Seq(
  name := nameStr,
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
  fork := true
)

lazy val setup = project
  .in(file("setup"))
  .settings(commonSettings("setup"))
  .settings(
    libraryDependencies ++= Seq(
      "org.apache.logging.log4j" % "log4j-slf4j-impl" % Settings.versions.log4jslf4j,
      "com.opengamma.strata" % "strata-measure" % Settings.versions.strata,
      "com.opengamma.strata" % "strata-report" % Settings.versions.strata,
      "com.opengamma.strata" % "strata-loader" % Settings.versions.strata
    )
  )

lazy val jnibridge = project
  .in(file("jnibridge"))
  .settings(commonSettings("jnibridge"))
  .settings(
    libraryDependencies ++= Seq(
      "com.opengamma.strata" % "strata-measure" % Settings.versions.strata % Test,
      "org.scalatest" %% "scalatest" % Settings.versions.scalatest % Test,
      "org.scalacheck" %% "scalacheck" % Settings.versions.scalacheck % Test
    ),
    testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oD")
  )
  .dependsOn(jnibridgeimpl % Runtime)

lazy val jnibridgeimpl = project
  .in(file("jnibridgeimpl"))
  .settings(commonSettings("jnibridgeimpl"))
  .enablePlugins(JniNative)

lazy val bench = project
  .in(file("bench"))
  .settings(commonSettings("bench"))
  .settings(
    libraryDependencies += "com.simianquant" %% "mathbridge" % Settings.versions.mathbridge,
    resourceDirectory in Jmh := (resourceDirectory in Compile).value
  )
  .dependsOn(setup, jnibridge, jnibridgeimpl % Runtime)
  .enablePlugins(JmhPlugin)

lazy val scratch = project
  .in(file("scratch"))
  .settings(commonSettings("scratch"))
  .dependsOn(setup)

lazy val root = project
  .in(file("."))
  .settings(commonSettings("root"))
