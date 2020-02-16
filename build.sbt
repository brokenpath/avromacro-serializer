ThisBuild / name := "avroserializer"

ThisBuild / scalaVersion := "2.12.10"

lazy val supportedVersions = List("2.12.10", "2.13.1")

lazy val commonDependencies =
  libraryDependencies ++= List(
    "org.scalameta" %% "scalameta" % "4.2.5",
    "org.scala-lang" % "scala-reflect" % scalaVersion.value,
    "org.scalactic" %% "scalactic" % "3.0.8" % "test",
    "org.scalatest" %% "scalatest" % "3.0.8" % "test",
  )

def configuration(id: String)(project: Project): Project =
  project.settings(
    moduleName := s"avroserializer-$id",
    crossScalaVersions := supportedVersions,
    sources in (Compile, doc) := List.empty,
    commonDependencies,
    scalacOptions ++= List(
      "-language:experimental.macros",
    ),
    libraryDependencies ++= {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, 13)) => Nil
        case _ => List(compilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.patch))
      }
    },
    scalacOptions ++= {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, 13)) => List("-Ymacro-annotations")
        case _ => Nil
      }
    }
  )


/**
lazy val `scalameta-serialiser` = project.in(file("."))
  .settings(commonSettings: _*)

lazy val examples = project.in(file("examples"))
  .settings(commonSettings: _*)
  .dependsOn(`scalameta-serialiser`)
*/

def avroserModule(id: String) =
  Project(id, file(s"modules/$id"))
    .configure(configuration(id))

lazy val core = avroserModule("core")
lazy val serusage = avroserModule("serusage") dependsOn (core % "compile->compile;test->test")

lazy val modules: List[ProjectReference] = List(core, serusage)


lazy val avroserializer = project
  .in(file("."))
  .settings(
    moduleName := "avroserializer",
    skip in publish := true
  )
  .aggregate(modules: _*)