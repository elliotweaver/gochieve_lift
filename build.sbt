name := "Lift 2.5 starter template"

version := "0.0.1"

organization := "net.liftweb"

scalaVersion := "2.9.1"

resolvers ++= Seq("snapshots"     at "http://oss.sonatype.org/content/repositories/snapshots",
                "releases"        at "http://oss.sonatype.org/content/repositories/releases",
                "omniauth"        at "https://repository-liftmodules.forge.cloudbees.com/release"
                )

seq(com.github.siasia.WebPlugin.webSettings :_*)

scalacOptions ++= Seq("-deprecation", "-unchecked")

libraryDependencies ++= {
  val liftVersion = "2.5-M1"
  Seq(
    "net.liftweb"             %% "lift-webkit"         % liftVersion                  % "compile",
    "net.liftweb"             %% "lift-mongodb-record" % liftVersion                  % "compile",
    "net.liftweb"             %% "lift-widgets"        % "2.4",
    "net.liftweb"             %% "lift-wizard"         % liftVersion,
    "net.liftmodules"         %% "omniauth"            % "2.4-0.5",
    "net.liftmodules"         %% "lift-jquery-module"  % (liftVersion + "-1.0"),
    "net.liftweb"             %% "lift-json"           % "XXX",
    "com.restfb"              %  "restfb"              % "1.6.11",
    "com.foursquare"          %% "rogue"               % "1.1.6"                      % "compile->default" intransitive(),
    "org.eclipse.jetty"       %  "jetty-webapp"        % "8.1.7.v20120910"            % "container",
    "org.eclipse.jetty.orbit" %  "javax.servlet"       % "3.0.0.v201112011016"        % "container" artifacts Artifact("javax.servlet", "jar", "jar"),
    "ch.qos.logback"          %  "logback-classic"     % "1.0.6",
    "org.specs2"              %% "specs2"              % "1.12.1"                     % "test"
  )
}
