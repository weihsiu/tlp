lazy val root = project
  .in(file("."))
  .settings(
    name := "tlp",
    version := "0.1.0",
    scalaVersion := "3.0.0-M1",
    scalacOptions ++= Seq(
      "-language:implicitConversions",
      "-Yindent-colons"
    ),
    libraryDependencies ++= Seq(
      "org.scala-lang" %% "scala3-staging" % scalaVersion.value,
      "org.typelevel" %% "cats-effect" % "3.0.0-M3",
      "com.github.rssh" %% "dotty-cps-async" % "0.3.1-M1"
    )
  )
