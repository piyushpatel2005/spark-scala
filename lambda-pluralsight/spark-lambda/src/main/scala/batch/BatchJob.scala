package batch

import java.lang.management.ManagementFactory

import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.sql.SQLContext
import domain._


object BatchJob {
  def main (args: Array[String]): Unit = {
    // get spark configuration
    val conf = new SparkConf()
        .setAppName("Lambda with Spark")

    // check if running from IDE
    if(ManagementFactory.getRuntimeMXBean.getInputArguments.toString.contains("IntelliJ IDEA")) {
      System.setProperty("hadoop.home.dir", "C:\\Hadoop\\bin")
      conf.setMaster("local[*]")
    }

    // set up spark context
    val sc = new SparkContext(conf)
    implicit val sqlContext = new SQLContext(sc)

//    import org.apache.spark.sql.functions._
//    import sqlContext.implicits._

    // intialize input RDD
    val sourceFile = "file:///c:/tmp/spark-test/data.tsv"
    val input = sc.textFile(sourceFile)

    // spark action results in job execution

    val inputRDD = input.flatMap{line =>
      val record = line.split("\\t")
      val MS_IN_HOUR = 100 * 60 * 60
      if(record.length == 7)
        Some(Activity(record(0).toLong / MS_IN_HOUR * MS_IN_HOUR, record(1), record(2), record(3), record(4), record(5), record(6), record(7)))
      else
        None
    }

    val keyedByProduct = inputRDD.keyBy(a => (a.product, a.timestamp_hour)).cache()
    val visitorsByProduct = keyedByProduct
      .mapValues(a => a.visitor)
      .distinct()
      .countByKey()

    val activityByProduct = keyedByProduct
      .mapValues{ a =>
        a.action match {
           case "purchase" => (1, 0, 0)
           case "add_to_cart" => (1, 1, 0)
           case "page_view" => (0, 0, 1)
        }
      }
      .reduceByKey((a,b) => (a._1 + b._1, a._2 + b._2, a._3 + b._3))

    visitorsByProduct.foreach(println)
    activityByProduct.foreach(println)
  }
}
