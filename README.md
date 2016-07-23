# A Clojure/Compojure/Ring skeleton

This is a very simple small skeleton for a [JVM 1.8+](http://www.oracle.com/technetwork/java/javase/downloads/java-archive-downloads-javase6-419409.html), [Clojure 1.8](https://github.com/clojure/clojure/tree/clojure-1.8.0), [Ring](https://github.com/ring-clojure/ring/) and [Compojure](https://github.com/weavejester/compojure/) stack.

### How to use this skeleton

Read this README (it will disappear after your rename your project).

Then fork this project, change its name to something relevant, clone the fork and run `./rename-to.sh <your-project-name>`. Then open README.md, add the relevant information about your project.
In project.clj change the following keys:
- description
- url
- license

Push your changes. You're ready to start!

#### What this skeleton provides

A couple "site" routes, i.e. HTML serving routes:

1. /: which simply renders a "Hello world" paragraph on an HTML page with a javascript file and a css.
2. /status: which reports on the status of your application

An API route on /api/v1/ that simply servers a JSON response with type "success" and response "hello world".

An asset pipeline using [optimus](https://github.com/magnars/optimus). In development mode, simply serves assets as is. Otherwise, it bundles assets together, minifies them and fingerprints them so that you don't have issues with caches. It also sets smart expiration headers to help browsers cache your contents.

All the routes served (including assets) will be logged according to a log4j configuration. Extra logging can be used (examples in the code).

A DB setup for both [MongoDB](https://www.mongodb.com/) or [PostgreSQL](https://www.postgresql.org). In the case of MongoDB, all you need is to have a MONGO_URL environment variable defined with the mongo connection url.

If you're using Postgres, simply use clojure.jdbc to perform your queries with the following snippet:

```
(require '[clojure.java.jdbc :as j])
(j/query skeleton.db.config/dbspec "SELECT * FROM records;")
```

Or if you want to use [honeysql](https://github.com/jkk/honeysql):

```
(require '[clojure.java.jdbc :as j]
         '[honeysql.core :as sql]
         '[honeysql.helpers :refer :all])
(j/query skeleton.db.config/dbspec (-> (select :*) (from :records) sql/format))
```

With PostgreSQL, you can also have migrations using [ragtime](https://github.com/weavejester/ragtime). Simply drop a file under `resources/migrations` that is named like `0001-your-migration.up.sql` and another `0001-your-migration.down.sql` and run `lein migrate`. For convenience, the standalone jar generated also allows you to run the migrations with `java -jar <standalone_jar_path.jar> clojure.main -m skeleton.db.migration migrate`.

Postgres will use a JDBC pool to reuse connections.

### Development stack

If you're using [Leiningen 2+](https://github.com/technomancy/leiningen), you can build a standalone jar file or a war file that can be delpoyed to an application container such as [jetty](http://www.eclipse.org/jetty/) or [tomcat](http://tomcat.apache.org/). The default is to generate the standalone jar.

If you prefer [gradle 2.4 to 2.11](http://gradle.org/gradle-download/), the current config assumes you want a war file. It is possible to generate a standalone jar but it requires changing the build.gradle file. Gradle 2.12+ is incompatible with clojureresque 1.7.0 so please avoid.

As a consequence, dependencies have to be added both to the build.graddle and the project.clj files.
Or pick one and erase the other.

###### Using gradle to build

If you have JVM 1.8+ and gradle installed, simply:
```
gradle build
```

The resulting war file will be in `build/libs/`.

###### Using Leiningen to build

If you have JVM 1.8+ installed, simply run [`build.sh`](build.sh).

The resulting (standalone) jar file will be in `target/`. You may also change build.sh to run `lein ring uberwar` instead if you're planning to host your application is a webapp container (like tomcat and jetty). That war will also be in `target/`.

###### Day to day development

To develop, the project uses [gaffer](https://github.com/jingweno/gaffer) to start a few processes.

Once you've installed a JVM 1.8+, simply download this code (either via git or [download the zip and extract it](archive/master.zip)) and run:
```
./dev.sh
```

This will install curl, leiningen, mongodb and gaffer if they're not available in your system. Then it will start [cooper](https://github.com/kouphax/lein-cooper) for development, notably:
- A webserver on the port specified in .env or 5000
- A mongodb instance looking at `tmp/db/data` on the default mongo db port (27017)
- A process to watch the test folders and rerun tests upon change

Note this project uses lein-dotenv which will read environment variables or variables specified in .env so you can assign your environment variables in there. That file should NOT be committed to source control.

