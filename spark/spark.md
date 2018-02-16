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
