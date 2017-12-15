[![Build Status](https://travis-ci.org/rbraeunlich/gatling-cluster.svg?branch=master)](https://travis-ci.org/rbraeunlich/gatling-cluster)

# Gatling Cluster

## Manual Testing

1. Run `sbt assembly`
2. Start the different instances the following way:

    * java -DPORT=2551 -Dconfig.resource=/seed.conf -jar target/scala-2.12/gatling-cluster-assembly-1.0.jar

    * java -DPORT=2554 -Dconfig.resource=/master.conf -jar target/scala-2.12/gatling-cluster-assembly-1.0.jar

    * java -DPORT=2556 -Dconfig.resource=/worker.conf -jar target/scala-2.12/gatling-cluster-assembly-1.0.jar
    
3. The Master node should start the simulation on the single worker node and collection the results.

Find the results under `results/collect`