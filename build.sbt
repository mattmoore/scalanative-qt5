organization in ThisBuild := "de.surfice"

version in ThisBuild := "0.0.1-SNAPSHOT"

scalaVersion in ThisBuild := "2.11.12"

val Version = new {
  val obj_interop = "0.1.0-SNAPSHOT"
  val smacrotools = "0.0.8"
  val utest       = "0.6.3"
}


lazy val commonSettings = Seq(
  scalacOptions ++= Seq("-deprecation","-unchecked","-feature","-language:implicitConversions","-Xlint"),
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
  libraryDependencies ++= Seq(
    "de.surfice" %%% "scalanative-interop-cxx" % Version.obj_interop
    )
)

lazy val qt5 = project.in(file("."))
  .enablePlugins(ScalaNativePlugin)
  .aggregate(core,gui,widgets)
  .settings(commonSettings ++ dontPublish:_*)
  .settings(
    name := "scalanative-qt5",
    description := "ScalaNative bindings for Qt5"
  )

lazy val macros = project
  .enablePlugins(ScalaNativePlugin)
  .settings(commonSettings ++ publishingSettings: _*)
  .settings(
    name := "scalanative-qt5-macros"
  )

lazy val core = project
  .enablePlugins(ScalaNativePlugin)
  .dependsOn(macros)
  .settings(commonSettings ++ publishingSettings: _*)
  .settings(
    name := "scalanative-qt5-core"
  )

lazy val gui = project
  .enablePlugins(ScalaNativePlugin)
  .dependsOn(core)
  .settings(commonSettings ++ publishingSettings: _*)
  .settings(
    name := "scalanative-qt5-gui"
  )

lazy val widgets = project
  .enablePlugins(ScalaNativePlugin)
  .dependsOn(gui)
  .settings(commonSettings ++ publishingSettings: _*)
  .settings(
    name := "scalanative-qt5-widgets"
  )

val qt5Prefix = "/usr/local/Cellar/qt/5.12.3/"
lazy val demo = project
  .enablePlugins(ScalaNativePlugin,NBHAutoPlugin,NBHCxxPlugin,NBHPkgConfigPlugin)
  .dependsOn(widgets)
  .settings(commonSettings ++ dontPublish: _*)
  .settings(
    nativeLinkStubs := true,
    nbhCxxCXXFlags := "-std=c++11 -DQT_WIDGETS_LIB -F/usr/local/Cellar/qt/5.12.3/lib -DQT_GUI_LIB -F/usr/local/Cellar/qt/5.12.3/lib -DQT_CORE_LIB -F/usr/local/Cellar/qt/5.12.3/lib -I/usr/local/Cellar/qt/5.12.3/lib/QtWidgets.framework/Headers -I/usr/local/Cellar/qt/5.12.3/lib/QtGui.framework/Headers -I/usr/local/Cellar/qt/5.12.3/lib/QtCore.framework/Headers".split(" "),
    nbhCxxLDFlags := "-F/usr/local/Cellar/qt/5.12.3/lib -framework QtWidgets -F/usr/local/Cellar/qt/5.12.3/lib -framework QtGui -F/usr/local/Cellar/qt/5.12.3/lib -framework QtCore".split(" ")
  )

lazy val dontPublish = Seq(
  publish := {},
  publishLocal := {},
  com.typesafe.sbt.pgp.PgpKeys.publishSigned := {},
  com.typesafe.sbt.pgp.PgpKeys.publishLocalSigned := {},
  publishArtifact := false,
  publishTo := Some(Resolver.file("Unused transient repository",file("target/unusedrepo")))
)

lazy val publishingSettings = Seq(
  publishMavenStyle := true,
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases"  at nexus + "service/local/staging/deploy/maven2")
  },
  pomExtra := (
    <url>https://github.com/jokade/scalanative-qt5</url>
    <licenses>
      <license>
        <name>MIT License</name>
        <url>http://www.opensource.org/licenses/mit-license.php</url>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:jokade/scalanative-unqlite</url>
      <connection>scm:git:git@github.com:jokade/scalanative-qt5.git</connection>
    </scm>
    <developers>
      <developer>
        <id>jokade</id>
        <name>Johannes Kastner</name>
        <email>jokade@karchedon.de</email>
      </developer>
    </developers>
  )
)
 