#!/usr/bin/env bash

git clone https://github.com/apache/commons-lang.git
cd commons-lang
mvn clean install -DskipTests

cd ..
mvn exec:java -Dexec.mainClass="Main" -Dexec.args="-i commons-lang/ -o target/commong-lang -v OVERALL"