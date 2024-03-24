# Snowpark Java Testing Examples

This repository is meant to provide examples of some strategies that can be used for testing snowpark java code.

# Repository Structure

```
- src/main/java/org/example
  - procedure
    - ExcelWriter.java
    - Proc1.java
  - udf
    - LanguageDetectorUDF.java
  - udtf
    - WordCounter.java
  - util
    - Register.java
```

# Procedures in this repo

## PROC1

Just a demo procedure that inserts some data on a table

## ExcelWriter

A procedure that takes a query and a target location and creates an Excel file with the results of the query.

# Function in this Repo

## LanguageDetectorUDF

A function that takes a string and returns the language detected.

# Table Functions in this Repo

## WordCounter

    A table function that takes a string and returns a table with the number of words in the string.

# Handling Jar Dependencies

When you register a function you need to make sure you have the jar files in your classpath.
In order to do that we first need to make sure you have the right jars.

Using maven I find this command useful:

First I run:

```
 mvn dependency:tree
```

which will provide an output like:

```
[INFO] --- dependency:3.6.0:tree (default-cli) @ snowpark-java-testing-examples ---
[INFO] com.snowflake:snowpark-java-template:jar:0.0.1
[INFO] +- com.github.pemistahl:lingua:jar:1.2.2:compile
[INFO] |  +- org.jetbrains.kotlin:kotlin-stdlib:jar:1.6.21:runtime
[INFO] |  |  +- org.jetbrains.kotlin:kotlin-stdlib-common:jar:1.6.21:runtime
[INFO] |  |  \- org.jetbrains:annotations:jar:13.0:runtime
[INFO] |  +- com.squareup.moshi:moshi:jar:1.13.0:runtime
[INFO] |  |  +- com.squareup.okio:okio:jar:2.10.0:runtime
[INFO] |  |  \- org.jetbrains.kotlin:kotlin-stdlib-jdk8:jar:1.6.0:runtime
[INFO] |  |     \- org.jetbrains.kotlin:kotlin-stdlib-jdk7:jar:1.6.0:runtime
[INFO] |  +- com.squareup.moshi:moshi-kotlin:jar:1.13.0:runtime
[INFO] |  |  \- org.jetbrains.kotlin:kotlin-reflect:jar:1.6.0:runtime
[INFO] |  \- it.unimi.dsi:fastutil:jar:8.5.8:runtime
[INFO] +- org.apache.poi:poi:jar:5.0.0:compile
[INFO] |  +- org.slf4j:slf4j-api:jar:1.7.30:compile
[INFO] |  +- org.slf4j:jcl-over-slf4j:jar:1.7.30:compile
[INFO] |  +- commons-codec:commons-codec:jar:1.15:compile
[INFO] |  +- org.apache.commons:commons-collections4:jar:4.4:compile
[INFO] |  +- org.apache.commons:commons-math3:jar:3.6.1:compile
[INFO] |  \- com.zaxxer:SparseBitSet:jar:1.2:compile
[INFO] +- org.apache.poi:poi-ooxml:jar:5.2.0:compile
[INFO] |  +- org.apache.poi:poi-ooxml-lite:jar:5.2.0:compile
[INFO] |  +- org.apache.xmlbeans:xmlbeans:jar:5.0.3:compile
[INFO] |  +- org.apache.commons:commons-compress:jar:1.21:compile
[INFO] |  +- commons-io:commons-io:jar:2.11.0:compile
[INFO] |  +- com.github.virtuald:curvesapi:jar:1.06:compile
[INFO] |  \- org.apache.logging.log4j:log4j-api:jar:2.17.1:compile
[INFO] +- com.snowflake:snowpark:jar:1.11.0:compile
[INFO] |  +- org.scala-lang:scala-library:jar:2.12.18:compile
[INFO] |  +- org.scala-lang:scala-compiler:jar:2.12.18:compile
[INFO] |  |  +- org.scala-lang:scala-reflect:jar:2.12.18:compile
[INFO] |  |  \- org.scala-lang.modules:scala-xml_2.12:jar:2.1.0:compile
[INFO] |  +- javax.xml.bind:jaxb-api:jar:2.2.2:compile
[INFO] |  |  +- javax.xml.stream:stax-api:jar:1.0-2:compile
[INFO] |  |  \- javax.activation:activation:jar:1.1:compile
[INFO] |  +- org.slf4j:slf4j-simple:jar:1.7.32:compile
[INFO] |  +- net.snowflake:snowflake-jdbc:jar:3.14.4:compile
[INFO] |  +- com.github.vertical-blank:sql-formatter:jar:1.0.2:compile
[INFO] |  +- com.fasterxml.jackson.core:jackson-databind:jar:2.13.4.2:compile
[INFO] |  +- com.fasterxml.jackson.core:jackson-core:jar:2.13.2:compile
[INFO] |  \- com.fasterxml.jackson.core:jackson-annotations:jar:2.13.2:compile
[INFO] +- junit:junit:jar:4.11:test
[INFO] |  \- org.hamcrest:hamcrest-core:jar:1.3:test
[INFO] +- com.zavtech:morpheus-core:jar:0.9.23:test
[INFO] |  +- com.google.code.gson:gson:jar:2.8.2:test
[INFO] |  +- net.sf.trove4j:trove4j:jar:3.0.3:test
[INFO] |  \- com.univocity:univocity-parsers:jar:2.5.9:test
[INFO] +- org.mockito:mockito-junit-jupiter:jar:5.2.0:test
[INFO] |  +- org.mockito:mockito-core:jar:5.2.0:test
[INFO] |  |  +- net.bytebuddy:byte-buddy:jar:1.14.1:test
[INFO] |  |  +- net.bytebuddy:byte-buddy-agent:jar:1.14.1:test
[INFO] |  |  \- org.objenesis:objenesis:jar:3.3:test
[INFO] |  \- org.junit.jupiter:junit-jupiter-api:jar:5.9.2:test
[INFO] |     +- org.opentest4j:opentest4j:jar:1.2.0:test
[INFO] |     +- org.junit.platform:junit-platform-commons:jar:1.9.2:test
[INFO] |     \- org.apiguardian:apiguardian-api:jar:1.1.2:test
[INFO] \- org.mockito:mockito-inline:jar:5.2.0:test
```

```
mvn org.apache.maven.plugins:maven-dependency-plugin:2.8:copy-dependencies -DoutputDirectory=./libs -DincludeScope=compile -DincludeScope=runtime -Dmdep.stripVersion=true
```

This will copy all the dependencies in the `./libs` directory. That way you can use
[session.addDependency](https://docs.snowflake.com/developer-guide/snowpark/reference/java/com/snowflake/snowpark_java/Session.html#addDependency(java.lang.String)) before registering the function.

# Setting up your Snowflake environment

For ease of use what I recommend is to make sure your tools are properly configured.
First install [SnowSQL](https://docs.snowflake.com/en/user-guide/snowsql)

This tool expects your configuration in a directory like `~/.snowsql/config`

As a minimum you need to have a connection named `default` with the following properties:

```
[connections]    
accountname = aaaa
username = bbbb
password = cccc
rolename = dddd
dbname = eeee
schemaname = ffff
warehousename = gggg
```
