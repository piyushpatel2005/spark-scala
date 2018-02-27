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

**PairRDDs** consist of key-value tuples.
**DoubleRDD** is a collection of double values.
**SequenceFileRDD** is created from a sequence file which is a format of files in HDFS.
**CoGroupedRDD** is an RDD that cogroups its parent.
**ShuffledRDD** shuffles the RDD elements by key so as to accumulate values for the same key on the same executor to allow an aggregation or combiner logic.
**UnionRDD** is a result of a union of two RDDs.

```scala
// PairRDD
val statesPopulationRDD = sc.textFile("statesPopulation.csv")
statesPopulationRDD.first
statesPopulationRDD.take(5)
// make pair of state name and population
val pairRDD = statesPopulationRDD.map(record => (record.split(",")(0), record.split(",")(2)))
pairRDD.take(10)

// DoubleRDD
val rdd_one = sc.parallelize(Seq(1.0, 2.0, 3.0))
rdd_one.mean
rdd_one.min
rdd_one.stdev

// SequenceFileRDD
pairRDD.saveAsSequenceFile("seqfile")
val seqRDD = sc.sequenceFile[String, String]("seqfile")
seqRDD.take(10)

// CoGroupedRDD
val pairRDD2 = statesPopulationRDD.map(rec => (rec.split(",")(0), rec.split(",")(1)))
val cogroupRDD = pairRDD.cogroup(pairRDD2)
cogroupRDD.take(10)

// ShuffledRDD
val pairRDD = statesPopulationRDD.map(rec => (rec.split(",")(0), 1))
pairRDD.take(5)
val shuffledRDD = pairRDD.reduceByKey(_+_)
shuffledRDD.take(5)

// UnionRDD
val rdd_one = sc.parallelize(Seq(1,2,3))
val rdd_two = sc.parallelize(Seq(4,5,6))
val unionRDD = rdd_one.union(rdd_two)
unionRDD.take(10)

// NewHadoopRDD
val rdd_whole = sc.wholeTextFiles("sample.txt")
rdd_whole.toDebugString
```

Aggregation functions aggregate the data.
`groupByKey` is an expensive operation due to all the data shuffling needed. groupByKey must be able to hold all the key-value pairs for any key in memory. If the key has too many values, it can result in OutOfMemoryError. `reduceByKey` tends to improve the performance by not sending all elements of pairRDD using shuffles, but it uses combiner logic locally. This reduces network overhead. `aggregateByKey` is quite similar to reduceByKey, but it allows more flexibility for customization. `combineByKey` is very similar to aggregateByKey function.

```scala
val statesPopulationRDD = sc.textFile("statesPopulation.csv")
statesPopulationRDd.take(5)
val pairRDD = statesPopulationRDD.map(record => record.split(",")).map(t => (t(0), (t(1), t(2))))
pairRDD.take(5) // ((State, (Year, Population)))
```

The number of partitions is important because this number directly influences the number of tasks that will be running RDD tranformations. If it is too small, we will use only a few CPUs/cores on a lot of data thus having a slower performance and making cluster underutilized. On the other hand, if partitions are too many, you will use more resources that you actually need and in a multi tenant environment could be causing starvation of resources for other jobs.

### Partitions

Two types of partitioners.

1. HashPartitioner is the default partitioner in Spark and works by calculating a hash value for each key of the RDD elements. The default number of partitions is either from Spark configuration parameter `spark.default.parallelism` or the number of cores in the cluster.
2. RangePartitioner works by partitioning the RDD into roughly equal ranges.

Shuffling determines the Spark job execution process and influences how the job is split into stages. When an RDD can be derived from another RDd using simple one to one tranformation such as a filter function, then the child RDD is said to depend on the parent RDD on a one-to-one basis. This is narrow dependency. When RDD is derived from one or more RDDs by transferring data over the wire or exchanging data, the child RDD is said to depend on the parent RDDs participating in a shuffle operation. This is wide dependency.

**Broadcast variables** are shared variables across all executors. They are created once in the Driver and then read only on executors. They use similar technique as BitTorrent where the dataset is distributed to all executors rather than having each executor pull the data from the Driver one by one.

```scala
val i = 5
val bi = sc.broadcase(i) // broadcast the variable i
bi.value // get the value
```

Broadcast variables do occupy memory on all executors and depending on the size of the data, this could cause resource issues at some point. We can free up resources from memory cache using `unpersist()`. We can also destroy broadcast variables completely removing them from all executors and the Driver too, making them inaccessible using `destroy` method.


```scala
val rdd_one = sc.parallelize(Seq(1,2,3))
val k = 5
val bk = sc.broadcast(k)
rdd_one.map(j => j + bk.value).take(5)
bk.unpersist
rdd_one.map(j => J + bk.value).take(4)

bk.destory
```

**Accumulators** are shared variables across executors typically used to add counters to Spark program. IF you have a spark program and would like to know errors or total records processed, we can add accumulators.

```scala
val acc1 = sc.longAccumulator("acc1")
val someRDD = statesPopulationRDD.map(x => {acc1.add(1); x})
acc1.value  // 0
someRDD.count // 351
acc1.values // 351
```

To run spark from command line,

`spark-submit --class com.piyushpatel2005.spark.MovieSimilarities MovieSims.jar 50`

## Spark SQL

Spark SQL works by parsing the SQL like statement into an Abstract Syntax tree (AST).

### DataFrame

It is an abstractio nof Resilient Distributed Datasets(RDD), dealing with higher level functions optimized using catalyst optimizer. Datasets were added in Spark 1.6 and provides benefits of strong typing on top of DataFrames. Since Spark 2.0, the DataFrame is an alias of a dataset. DataFrame is similar to a table in a relational database. Dataframes are again immutable.

The DataFrame can be created by executing SQL queries, by loading external data such as Parquet, JSON, CSV, Hive, JDBC or by converting RDD into data frames.

```scala
val statesDF = spark.read.option("header", "true").option("inferschema", "true").option("sep", ",").csv("statesPopulation.csv")
statesDF.printSchema
statesDF.explain(true)
statesDF.createOrReplaceTempView("states")
statesDF.show(5)
spark.sql("select * from states limit 5").show
statesDF.sort(col("Population").desc).show(5)
spark.sql("select * from states order by Population desc limit 5").show

statesDF.groupBy("State").sum("Population").show(5)
spark.sql("select State, sum(Population) from states group by State limit 5").show

statesDF.groupBy("State").agg(sum("Population").alias("Total")).show(5)
spark.sql("select State, sum(Population) as Total from states group by State limit 5").show

statesDF.groupBy("State").agg(sum("Population").alias("Total")).explain(true)
statesDF.groupBy("State").agg(sum("Population").alias("Total")).sort(col("Total").desc).show(5)
spark.sql("select State, sum(Population) as Total from states group by State order by Total desc limit 5").show

statesDF.groupBy("State").agg(
  min("Population").alias("minTotal"),
  max("Population").alias("maxTotal"),
  avg("Population").alias("avgTotal")
  ).sort(col("minTotal").desc).show(5)
spark.sql("select State, min(Population) as minTotal, max(Population) as maxTotal, avg(Population) as avgTotal from states group by State order by minTotal desc limit 5").show
```

**Pivots** are used to create a different view, more suitable to doing many summarizations and aggregations.

```scala
statesDF.groupBy("State").pivot("Year").sum("Population").show(5)
```

**Filters** are used to filter DataFrames to filter out certain columns.

```scala
statesDF.filter("State== 'California'").explain(true)
statesDF.filter("States == 'California'").show
```

In Spark defining **User Defined Functions** is pretty straight forward.

```scala
import org.apache.spark.sql.functions._
val toUpper: String => String = _.toUpperCase
val toUpperUDF = udf(toUpper)
statesDF.withColumn("StateUpperCase", toUpperUDF(col("State"))).show(5)
```

We can infer schema from DataFrame using reflection. The Spark API provides case classes which can be used to define the schema of the table.

We can also create schema without case classes by reading the header and specifying separator that is used to split the text lines.

```scala
val statesDF = spark.read.option("header", "true")
  .option("inferschema", "true")
  .option("sep", ",")
  .csv("statesPopulation.csv")
statesDF.schema
statesDF.printSchema

// Schema is described using StructType which is a collection of StructField objects.
import org.apache.spark.sql.types.{StructType, IntegerType, StringType}
val schema = new StructType().add("i", IntegerType).add("s", StringType)
schema.printTreeString
schema.prettyJson

import org.apache.spark.sql.types._
// spark 2.0 support a new way of defining schema using encoders.
import org.apache.spark.sql.Encoders

Encoders.product[(Integer, String)].schema.printTreeString
case class Record(i: Integer, s: String)
Encoders.product[Record].schema.printTreeString

// All data types of Spark SQL are located in package org.apache.spark.sql.types.
import org.apache.spark.sql.types._

// Use DataTypes object to create complex Spark SQL types such as arrays, maps, etc.
import org.apache.spark.sql.types.DataTypes
val arrayType = DataTypes.createArrayType(IntegerType)
```

**Loading and Saving DataSets**

Spark SQL can read data from external storage systems such as files, Hive Tables, JDBC databases through DataFrameReader interface.

`spark.read.inputype`

```scala
val statesPopulationDF = spark.read
    .option("header", "true")
    .option("inferschema", "true")
    .option("sep", ",")
    .csv("statesPopulation.csv")
val statesTaxRatesDF = spark.read
    .option("header", "true")
    .option("inferschema", "true")
    .option("sep", ",")
    .csv("statesTaxRates.csv")

statesPopulationDF.write.option("header", "true").csv("statesPopulation_dup.csv")
statesTaxRatesDF.write.option("header", "true").csv("statesTaxRates_dup.csv")
```

**Aggregation and Window functions**

Most aggregation functions can be found in `org.apache.spark.sql.functions` package. We can also define User defined aggregation functions (UDAF).

```scala
val statesPopulationDF = spark.read
    .option("header", "true")
    .option("inferschema", "true")
    .option("sep", ",")
    .csv("statesPopulation.csv")
statesPopulationDF.select(col("*")).agg(count("State")).show // OR
statesPopulationDF.select(count("State")).show

statesPopulationDF.select(col("*")).agg(countDistinct("State")).show
statesPopulationDF.select(countDistinct("State")).show

statesPopulationDF.select(first("State")).show
statesPopulationDF.select(last("State")).show

// approx_count_distinct counts faster at approximately distinct records.
statesPopulationDF.select(col("*")).agg(approx_count_distinct("State")).show
statesPopulationDF.select(approx_count_distinct("State", 0.2)).show
statesPopulationDF.select(min("Population")).show
statesPopulationDF.select(max("Population")).show
statesPopulationDF.select(avg("Population")).show
statesPopulationDF.select(sum("Population")).show

// Kurtosis is a good measure of the weight of the distribution at the tail of the distribution. It is different than means and variances.
import org.apache.spark.sql.functions._
statesPopulationDF.select(kurtosis("Population")).show

// Skewness measures the asymmetry of the values in your data around the average or mean
statesPopulationDF.select(skewness("Population")).show

// Variance is the average of the squared differences of each of the values from the mean.
statesPopulationDF.select(var_pop("Population")).show

// standard deviation is the square root of the variance
statesPopulationDF.select(stddev("Population")).show

// Covariance is the measure of the joint variability of two random variables. If greater values of one variable mainly corresponds with the greater values of the other variable, and the same holds for the lesser values, then the variables tend to show similar behavior and the covariance is positive.
statesPopulationDF.select(covar_pop("Year", "Population")).show

statesPopulationDF.groupBy("State").count.show(5)
// Rollup is a multi-dimensional aggregation used to perform hierarchical calculations.
// If we want to show number of records for each State and Year group.
statesPopulationDF.rollup("State", "Year").count.show(5)
// Cube is multi-dimensional aggregation used to perform hierarchical calculations just like Rollup
// If we want to show the number of records for each State and Year group, as well as for each State (aggregating over all Years to give a grant total for each State irrespective of the Year)
statesPopulationDF.cube("State", "Year").count.show(5)
```

**Window functions** are used to perform aggregations over a window of data rather than entire data.

```scala
import org.apache.spark.sql.exprssions.Window
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.functions.max

val windowSpec = Window
  .partitionBy("State")
  .orderBy(col("Population").desc)
  .rowBetween(Window.unboundedPreceding, Window.currentRow)

statesPopulationDF.select(col("State"), col("Year"), max("Population").over(windowSpec), rank().over(windowSpec)).sort("State", "Year").show(10)
// ntiles is a popular aggregation over a window and is commonly used to divide input dataset into n parts.
// For example, in predictive analytics, deciles(10 parts) are often used to first group the data and then divide it into 10 parts to get a fair distribution of data.

statesPopulationDF.select(
  col("State"), col("Year"),
  ntile(2).over(windowSpec), rank().over(windowSpec))
  .sort("State", "Year")
```

### Joins

DataFrame joins are similar to table joins.
A join between larger dataset and a smaller dataset can be done by broadcasting the smaller dataset to all executors where a partition from the left dataset exists.

```scala
val statesPopulationDF = spark.read
  .option("header", "true")
  .option("inferschema", "true")
  .option("sep", ",")
  .csv("statesPopulation.csv")
val statesTaxRatesDF = spark.read
  .option("header", "true")
  .option("inferschema", "true")
  .option("sep", ",")
  .csv("statesTaxRates.csv")
statesPopulationDF.createOrReplaceTempView("statePopulationDF")
statesTaxRatesDF.createOrReplaceTempView("statesTaxRatesDF")

val joinDF = statesPopulationDF.join(statesTaxRatesDF, statesPopulationDF("State") === statesTaxRatesDF("State"), "inner") // OR
%sql
val joinDF = spark.sql("""SELECT * FROM statesPopulationDF
  INNER JOIN statesTaxRatesDF
  ON statesPopulationDF.State = statesTaxRatesDF.State
""")
joinDF.count
joinDF.show
// If you have duplicate or multiple copies of the keys on either the left or right side, the join will take longer resulting in Cartesian join

val leftOuter = statesPopulationDF.join(statesTaxRatesDF, statesPopulationDF("State") === statesTaxRatesDF("State"), "leftouter")
%sql
val leftOuter = spark.sql("""
  SELECT * FROM statesPopulationDF
    LEFT OUTER JOIN statesTaxRatesDF
    ON statesPopulationDF.State = statesTaxRatesDF.State
  """
)
// Similarly rightouter join and fullouter join
// If there is little in common, fullouter join can result in very large results and slow performance.

// Left antijoin results in rows from only left DF that are not part of right DF.

val leftantiDF = statesPopulationDF.join(statesTaxRatesDF, statesPopulationDF("State") === statesTaxRatesDF("State"), "leftanti")
%sql
val leftantiDF = spark.sql("""
  SELECT * FROM statesPopulationDF
    LEFT ANTI JOIN statesTaxRatesDF
    ON statesPopulationDF.State = statesTaxRatesDF.State
""")
leftantiDF.count

// Left semi join results in rows from only left if and only if there is a corresponding row in right.
val leftsemiDF = statesPopulationDF.join(statesTaxRatesDF, statesPopulationDF("State") === statesTaxRatesDF("State"), "leftsemi")

// Cross join matches every row from left with every row from right generating cartesian cross product.
// This is worst in performance
val corssDF = statesPopulationDF.crossJoin(statesTaxRatesDF)
```

[Spark Streaming](spark-streaming.md)
