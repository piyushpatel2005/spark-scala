# Spark Streaming

Spark streaming is used for real time streaming with different input systems. It consumes continuous streams of data and then processes the collected data in the form of micro batches. Spark streaming requires StreamingContext just like SparkContext. It is the main entry point for streaming and takes care of the streaming application.

- Creating streaming context

```scala
StreamingContext(sparkContext: SparkContext, batchDuration: Duration)
val ssc = new StreamingContext(sc, Seconds(10))

// Create by providing SparkConf
val conf = new SparkConf().setMaster("local[1]").setAppName("TextStreams")
val ssc = new StreamingContext(conf, Seconds(10))

// using getOrCreate method
def getOrCreate(
  checkpointPath: String,
  creatingFunc: () => StreamingContext,
  hadoopConf: Configuration = SparkHadoopUtil.get.conf,
  createOnError: Boolean = false
): StreamingContext
```

The start method starts the execution of the streams defined by StreamingContext using `ssc.start()`.

We can stop StreamingContext using stop method. `ssc.stop(false)`. The argument is stopSparkContext along with StreamingContext.
Another overloading method of stopping is:
```scala
def stop(stopSparkContext: Boolean, stopGracefully: Boolean)
```

There are several types of streams such as receiverStream and fileStream that can be created using StreamingContext. *socketTextStream* creates an input stream from TCP source hostname:port. *rawSocketStream* is used to create an input stream from network source hostname:port where data is received as serialized blocks. *fileStream* creates an input stream that monitors a Hadoop compatible filesystem for new files and reads them using given key-value types and input format. Files must be written to the monitored directory by moving them from another location within the same filesystem. The files starting with dot (.) are ignored. *textFileStream* monitors a Hadoop compatible filesystem for new files and reads them as text files (using a key LongWritable, value as Text, and input format as TextInputFormat). *binaryRecordStream* creates an input stream that monitors a Hadoop compatible filesystem for new files and reads them as flat binary files. *queueStream* create an input stream from a queue of RDDs. In each batch it will process one or all of the RDDs returned by the queue.

```scala
import org.apache.spark._
import org.apache.spark.streaming._
val ssc = new StreamingContext(sc, Seconds(10))
val filestream = ssc.textFileStream("streamfilesDirectory")
filestream.foreachRDD(rdd => {
  println(rdd.count())
})
ssc.start
```

Download `spark-streaming-twitter_2.11-2.1.0.jar`, `twitter4j-core-4.0.6.jar` and `twitter4j-stream-4.0.6.jar` for twitter streaming project.

### Discretized streams
Spark streaming is built on an abstraction called Discretized Streams (DStreams) represented as stream of RDDs. DStream essentially divides never ending stream into smaller chunks known as micro-batches based on time interval. No state is maintained between micro-batches thus making the processing stateless by nature.

Steps involved in building a streaming applications are as follows.
1. Create a StreamingContext from sparkContext
2. Create a DStream from StreamingContext
3. Provide transformations and actions that can be applied to each RDD.
4. Finally, the Streaming application is started by calling start() method on StreamingContext. This starts the entire processing in real time.

A stopped context cannot be restarted and a new one needs to be created.

```scala
val ssc = new StreamingContext(sc, Seconds(5))
val twitterStream = TwitterUtils.createStream(ssc, None)
val aggStream = twitterStream.flatMap(x => x.getText.split(" "))
  .filter(_.startsWith("#"))
  .map(x => (x, 1))
  .reduceByKey(_ + _)

ssc.start()
```

Transformations on DStream are similar to the transformations to a Spark RDD. There are different types of DStream classes.

**Window Operations**

Spark Streaming provides windowed processing which allows you to apply transformations over a sliding window of events. There are two parameters that need to be specified: *window length* which is length in interval considered as window and another is *sliding interval* that is the interval at which the window is created.

Some of the operations on window are as follows.

| window(windowLength, slideInterval) | creates a window on the source DStream and returns the same as new DStream |
| countByWindow(windowLength, slideInterval) | count of elements in the DStream by applying sliding window.
| reduceByWindow(func, windowLength, slideInterval) | returns new DStream by applying reduce on each element of source after creating window. |
| reduceByKeyAndWindow(func, windowLength, slideInterval, [numTasks]) | aggregates the data by key in the window applied to the source DStream's RDDs |
| reduceByKeyAndWindow(func, invFunc, windowLength, slideInterval, [numTasks]) | aggregates the data by key in the window applied to the source DStream's RDDs and returns a new DStream of key,value pairs. |
| countByValueAndWindow(windowLength, slideInterval, [numTasks]) | computes the frequency of each key and returns a new DStream |

Real time streaming applications need to be resilient to failures. Spark streaming implements a checkpointing mechanism that maintains enough information to recover from failures.

Two types of checkpointing: metadata checkpointing and data checkpointing. Checkpointing can be enabled by calling checkpoint() function on sc.

```scala
def checkpoint(directory: String)
```

Metadata checkpointing saves information defining the streaming operations which are represented by Directed Acyclic Graph (DAG) to HDFS. Data checkpointing saves the actual RDDs to HDFS so that if there is a failures of the Streaming application, the application can recover the checkpointed RDDs and continue from where it left off.

Once the spark StreamingContext has been started at the end we need to add

`ssc.awaitTermination()

ssc.awaitTerminationOrTimeout(2000) // milliseconds`

to stop the process from terminating.

Spark Streaming has nice interoperability with Kafka.
