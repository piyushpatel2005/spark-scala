# Spark

Spark is a lighting fast cluster computing framework. It is based on MapReduce model. Main feature is in-memory processing which is the core reason for its speed. It was first developed at UC Berkeley.
Features of spark include:
- Fast computations
- Support for multiple languages like Scala, Python, R, Java
- It has its own machine learning library MLLib, streaming and graph processing libraries.

Spark applications can be deployed in various ways. It can be deployed in standalone cluster, Hadoop YARN cluster, Mesos cluster, Amazon EC2, etc.

Typical RDD programming model is as follows.

1. From environment variable, Spark context creates initial data reference RDD object.
2. Transform the initial RDD to create more RDD objects.
3. Send the code, algorithms or applications from the driver program to the cluster manager nodes. Then cluster manager provides a copy to each computing node.
4. Computing nodes hold the reference to RDDs in their partition.
5. After transformation, the result to be generated is a brand new RDD, since original one will be mutated.
6. Finally, RDD is materialized through an action to dump the RDD into storage.
7. The driver program can ask the computing nodes for a chunk of results for the analysis or visulation of a program.

Spark can be deployed in Standalone cluster, HDFS, Hadoop YARN cluster, Mesos cluster or IaaS services like EC2.

Spark Ecosystem consists of following

- Spark core
- Spark SQL
- Spark streaming
- MLlib
- GraphX

Apache Spark is fast in-memory data processing engine with elegant and expressive development APIs to allow data workers to efficiently execute streaming machine learning or SQL workloads.

The key components are **driver** and the **executors**. Driver program contains the applications, main program. The driver launches the executors across the cluster and also controls the task executions. Executors are processes running on the worker nodes in cluster. In this, the individual tasks or computations are run.  When driver connects to the cluster manager, the cluster manager assigns resources to run executors. The cluster manager is responsible for the scheduling and allocation of resources across the compute nodes forming the cluster. There can be three types of cluster managers: standalone, YARN and Mesos.

SparkContext is the main entry point to the Spark program. It is inside driver component and represents the connection to the cluster along with the code to ru nthe scheduler and task distribution and orchestration. SparkContext can be used to create RDDs, accumulators and broadcast variables on the cluster. You must `stop()` the active SparkContext before creating a new one.

The **Directed Acyclic Graph(DAG)** are created by Driver program  DAG is executed in stages and tasks by the task scheduler by communicating with the cluster manager for resources. A DAG represents a job and a job is split into subsets called stages and each stage is executed as task using one core per task. The number of stages and what the stages consist of is determined by the kind of operations.

## Installation

```shell
tar -xvzf spark-2.2.0-bin-hadoop2.7.tgz
cd spark-2.2.0-bin-hadoop2.7
export SPARK_HOME= <spark_dir>
export PATH = $PATH:$SPARK_HOME/bin
source ~/.bashrc
```

Spark can be run in one of the following modes.
1. Default shell on local machine as master
`spark-shell`
2. Default shell on local machine with local machine as master of n threads: `spark-shell --master local[n]`
3. Default shell on local machine connecting to the specified spark master as `spark-shell --master spark://<IP>:<PORT>`
4. Local machine connecting to a YARN cluster using client mode `spark-shell --master yarn --deploy-mode client`
5. Local machine connecting to a YARN cluster using cluster mode as `spark-shell --master yarn --deploy-mode cluster`

spark Driver also has a web UI for easy monitoring of jobs and DAGs. Open [http://localhost:4040](http://localhost:4040) after running spark-shell commands as shown above.

In YARN mode, the client communicates with YARN resource manager and gets containers to run the Spark execution. Multiple clients interacting with the cluster create their own executors on the cluster nodes.

In YARN **client mode**, the Driver runs on a node outside the cluster (typically where the client is). Driver first contacts resource manager requesting resources to run the Spark job. The resource manager allocates a container and responds to run the Spark job. The Driver then launches the Spark application master in the container zero. The spark application master creates the executors on the containers allocated by the resource manager.
In YARN **cluster mode**, the Driver runs on a node inside the cluster.

## RDDs

A Resilient Distributed Dataset (RDD) is an immutable, distributed collection of objects. Immutability makes the RDDs read-only once created. Transformations allow operations on the RDD to create a new RDD but the original one is never modified. This makes RDDs immune to race conditions and other synchronization problems.
RDD could be from HDFS, Hbase table, Cassandra table, Amazon S3. The number of partitions is independent of the number of nodes in the cluster. Partitioning is one of the main tuning factors to improve the performance of a Spark job. RDDs also store the lineage which is used to recover from failures. An RDD can be created in several ways using parallelizing a collection, reading data from an external source, transformation of an existing RDD or streaming API. When reading data from external source such as HDFS, each node is doing its own input-output operations and each node is independently reading one or more blocks from the HDFS blocks. The `textFile` function loads the input data as a text file with each newline terminated portions becomes an element in RDD.

```scala
// Launch spark-shell in console
// Parallelize a collection
val rdd_one = sc.parallelize(Seq(1,2,3))
rdd_one.take(10)

// Transformation
val rdd_one_x2 = rdd_one.map(i => i * 2) // new RDD
rdd_one_x2.take(10)

// Reading data from External source
val rdd_two = sc.textFile("sample.txt")
rdd_two.count
rdd_two.first
rdd_two.take(2).foreach(println)

val rdd_three = rdd_two.map(line => line.length)
rdd_three.take(10)

val rdd_three = rdd_two.flatMap(line => line.split(" "))
rdd_three.take(10)

val rdd_three = rdd_two.filter(line => line.contains("line3"))
rdd_three.count

rdd_two.partitions.length
val rdd_three = rdd_two.coalesce(1) // change input partitions
rdd_three.partitions.length

val rdd_three = rdd_two.repartition(5)
rdd_three.partitions.length

rdd_one.reduce((a,b) => a * b)

rdd_two.saveAsTextFile("out")

import org.apache.spark.storage.StorageLevel
rdd_one.persist(StorageLevel.MEMORY_ONLY)
rdd_one.unpersist()

rdd_one.persist(StorageLevel.DISK_ONLY)
rdd_one.unpersist()

sc.textFile(name, minPartitions=None, use_unicode=True)

// wholeTextFiles function can be used to load multiple files
sc.wholeTextFiles(path, minPartitions=None,use_unicode=True)

// load from jdbc source
sqlContext.load(path=None, source=None, schema=None, **options)

val dbContent = sqlContext.load(source="jdbc", url="jdbc:mysql://localhost:3306/test", dbtable="test", partitionColumn="id")
```

Spark shell is an interactive REPL environment with Spark context available as sc. It can be launched with various options as mentioned below. We can see different options with autocomplete if we press Tab.

```shell
spark-shell --help
// job submission
spark-submit
```
## Working with RDDs

RDDs are immutable and every operation creates a new RDD. **Transformations** change the elements in the RDD such as splitting the input element, filtering, performing calculations. Spark uses lazy evaluation. For transformations, Spark adds them to a DAG of computation and only when driver requests some data, does this DAG actually gets executed. Spark optimizes execution so that transformations are performed efficiently. Several transformations can be performed in sequence.

Transformations can be divided into four parts.
1. General transformations
  This includes map, filter, flatMap, groupByKey, sortByKey, combineByKey.
2. Math/Statistical Transformations
  This includes transofmrations such as sampleByKey, randomSplit, etc.
3. Set theory/relational transformations
  These handle tranformations like joins of datasets. cogroup, join, subtractByKey, fullOuterJoin, leftOuterJoin, etc. are included in this category.
4. Data structure based tranformations include partitionBy, repartition, zipwithIndex, coalesce, etc.

If we are grouping for aggregation, reduceByKey and aggregateByKey gives much better performance compared to groupByKey function.

**Actions** are operations which actually trigger the computations. There could be several transformations of all sorts within the execution plan, but nothing happens until you perform an action. Actions can be of two kinds.

Driver action such as collection count, count by key, etc perform some calculations on remote executors and pulls the data back into the driver. On large datasets, it can overwhelm the memory available on the driver taking down the application.
Distributed action is executed on the nodes in the cluster. This is the most common action operation due to desirable distributed nature of the operation.

### Types of RDDs

We can use `rdd.toDebugString` to check the lineage of each RDD.

**Pair RDDs** consist of key-value tuples.
