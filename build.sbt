
lazy val V = _root_.scalafix.sbt.BuildInfo

inThisBuild(
  List(
    publish / skip := true,
    scalaVersion := V.scala213,
    crossScalaVersions := List(V.scala213, V.scala212, V.scala211),
    organization := "ba.sake",
    homepage := Some(url("https://github.com/sake92/kalem")),
    licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    developers := List(
      Developer(
        "sake92",
        "Sakib Hadžiavdić",
        "sakib@sake.ba",
        url("https://sake.ba")
      )
    ),
    addCompilerPlugin(scalafixSemanticdb),
    scalacOptions ++= List(
      "-Yrangepos",
      "-P:semanticdb:synthetics:on"
    )
  )
)

lazy val kalemCore = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)
  .in(file("kalem-core"))
  .settings(
    publish / skip := false,
    moduleName := "kalem-core"
  )

lazy val rules = project.settings(
  publish / skip := false,
  moduleName := "kalem-rules",
  libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % V.scalafixVersion
).dependsOn(kalemCore.jvm)

lazy val input = project.dependsOn(kalemCore.jvm)

lazy val output = project.dependsOn(kalemCore.jvm)

lazy val tests = project
  .settings(
    libraryDependencies += "ch.epfl.scala" % "scalafix-testkit" % V.scalafixVersion % Test cross CrossVersion.full,
    Compile / compile :=
      (Compile / compile).dependsOn(input / Compile / compile).value,
    scalafixTestkitOutputSourceDirectories :=
      (output / Compile / unmanagedSourceDirectories).value,
    scalafixTestkitInputSourceDirectories :=
      (input / Compile / unmanagedSourceDirectories).value,
    scalafixTestkitInputClasspath :=
      (input / Compile / fullClasspath).value,
  )
  .dependsOn(rules)
  .enablePlugins(ScalafixTestkitPlugin)
