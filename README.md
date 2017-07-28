[![Build Status](https://travis-ci.org/rbraeunlich/gatling-cluster.svg?branch=master)](https://travis-ci.org/rbraeunlich/gatling-cluster)

java -DPORT=2551 -Dconfig.resource=/seed.conf -jar target/scala-2.11/gatling-cluster-assembly-1.0.jar

java -DPORT=2554 -Dconfig.resource=/master.conf -jar target/scala-2.11/gatling-cluster-assembly-1.0.jar

java -DPORT=2555 -Dconfig.resource=/worker.conf -jar target/scala-2.11/gatling-cluster-assembly-1.0.jar
