[![Build Status](https://travis-ci.org/rbraeunlich/gatling-cluster.svg?branch=master)](https://travis-ci.org/rbraeunlich/gatling-cluster)

# Gatling Cluster

## Goal

Executing Gatling tests in a distributed fashion requires users to either do everything manually [via shell scripts](https://gatling.io/docs/current/cookbook/scaling_out/) or
to buy the Gatling enterprise version. There might be good reasons to do so but for hobby projects both options are uncomfortable.

Therefore, this project aims to replace the shell script option by basically doing everything the shell scripts should do, just in Scala.

In contrast to other projects that use a work pulling pattern this project wants to achieve a predictable load on every node in the cluster.
I.e. every Gatling simulation you implement will be executed exactly as it is on one of the worker nodes. This had the advantage to know exactly
how many operations a single node executes and how many requests might happen in parallel.

## Usage

### Configuration

TODO

### Cluster Setup

Before you can start executing your performance tests in a distributed way you have to set up the cluster for the tests.
Since we are in a test environment, one seed and one master node should be enough. Start up as many worker nodes as you want to.

***Important***

Due to compilation and everything related to it the worker nodes you distribute have to contain you simulation class inside their
JAR or at least within the classpath. The main class only sends the classname as string across the network, not the compiled file.

Therefore, the recommendation is to create fat jars.

#### Startup via main method

To ease the developer burden simply create a Main class that extends `MetaSimulation`. That main class has to provide a number
for the intended worker nodes. Within the main method (or the object body if you extend `App`) call `startSimulation` with the class of the simulation you want 
to execute. This will spin up a master node locally, which joins the cluster and starts the simulation.

#### Startup via Docker

Possible?

### Test start

In order to start test load test you only have to send the simulation class name and the number of worker instances you wanna use to the master node.
Please keep in mind the the simulation class is executed as is. All of the users you configured there will create request ***times*** the number of nodes you want to use.

TODO

### Simulation Results

## Manual Testing

1. Run `sbt assembly`
2. Start a seed node and at least one worker node this way:

    * java -DPORT=2551 -Dconfig.resource=/seed.conf -jar target/scala-2.12/gatling-cluster-assembly-1.0.jar

    * java -DPORT=2556 -Dconfig.resource=/worker.conf -jar target/scala-2.12/gatling-cluster-assembly-1.0.jar

3. Starte the Main class under src/test
    
4. The main class starts a master node on the localhost running on port 2554. This should execute the simulation class.

Find the results under `results/collect`

## Interna

### Config files

`src/main/resources` contains the config files for the three different types of cluster node that exist.