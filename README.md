# A Clojure/Compojure/Ring skeleton

This is a very simple small skeleton for a [JVM 1.6+](http://www.oracle.com/technetwork/java/javase/downloads/java-archive-downloads-javase6-419409.html), [Clojure 1.7](https://github.com/clojure/clojure/tree/clojure-1.7.0), [Ring 1.4.0](https://github.com/ring-clojure/ring/tree/1.4.0) and [Compojure 1.4.0](https://github.com/weavejester/compojure/tree/1.4.0) stack.

### How to use this skeleton

Fork this project, change its name to something relevant, clone the fork and run `./rename-to.sh <your-project-name>`. Then open README.md, add the relevant information about your project.
In project.clj change the following keys:
- description
- url
- license

Push your changes. You're ready to start!

### Development stack

To build the application into a war file that can be used in an application container such as [jetty](http://www.eclipse.org/jetty/) or [tomcat](http://tomcat.apache.org/), you can either use [Leiningen 2+](https://github.com/technomancy/leiningen) or [gradle 2.4+](http://gradle.org/gradle-download/).
.

As a consequence, dependencies have to be added both to the build.graddle and the project.clj files.
Or pick one and erase the other.

###### Using gradle to build

If you have JVM 1.6+ and gradle installed, simply:
```
gradle build
```

The resulting war file will be in `build/libs/`.

###### Using Leiningen to build

If you have JVM 1.6+ installed, simply run [`build.sh`](build.sh).

The resulting war file will be in `target/`.

###### Day to day development

To develop, the project uses [gaffer](https://github.com/jingweno/gaffer) to start a few processes.

Once you've installed a JVM 1.6+, simply download this code (either via git or [download the zip and extract it](archive/master.zip)) and run:
```
./dev.sh
```

This will install curl, leiningen, mongodb and gaffer if they're not available in your system. Then it will start [gaffer](https://github.com/jingweno/gaffer) for development, notably:
- A webserver on port 5000 or the port assigned by the PORT environment variable
- A mongodb instance looking at `tmp/db/data` on the default mongo db port (27017)
- A process to watch the test folders and rerun tests upon change

Note this project uses lein-dotenv which will read environment variables or variables specified in .env so you can assign your PORT in there.
