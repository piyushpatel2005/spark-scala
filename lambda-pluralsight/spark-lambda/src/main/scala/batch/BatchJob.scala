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

    import org.apache.spark.sql.functions._
    import sqlContext.implicits._

    // intialize input RDD
    val sourceFile = "file:///c:/tmp/spark-test/data.tsv"
    val input = sc.textFile(sourceFile)

    // spark action results in job execution

    val inputDF = input.flatMap{line =>
      val record = line.split("\\t")
      val MS_IN_HOUR = 100 * 60 * 60
      if(record.length == 7)
        Some(Activity(record(0).toLong / MS_IN_HOUR * MS_IN_HOUR, record(1), record(2), record(3), record(4), record(5), record(6)))
      else
        None
    }.toDF()

    sqlContext.udf.register("UnderExposed", (pageViewCount: Long, purchaseCount: Long) => if(purchaseCount == 0) 0 else pageViewCount / purchaseCount)

    val df = inputDF.select(
      add_months(from_unixtime(inputDF("timestamp_hour") / 1000), 1).as("timestamp_hour"),
      inputDF("referrer"), inputDF("action"), inputDF("prevPage"), inputDF("page"), inputDF("visitor"), inputDF("product")
    ).cache()

    df.registerTempTable("activity")

    val visitorsByProduct = sqlContext.sql(
      """SELECT product, timestamp_hour, COUNT(DISTINCT visitor) AS unique_visitors
        |FROM activity GROUP BY product, timestamp_hour
      """.stripMargin)
    visitorsByProduct.printSchema()

    val activityByProduct = sqlContext.sql("""SELECT
                                            product,
                                            timestamp_hour,
                                            sum(case when action = 'purchase' then 1 else 0 end) as purchase_count,
                                            sum(case when action = 'add_to_cart' then 1 else 0 end) as add_to_cart_count,
                                            sum(case when action = 'page_view' then 1 else 0 end) as page_view_count
                                            from activity
                                            group by product, timestamp_hour """).cache()

    activityByProduct.registerTempTable("activityByProduct")

    val underExposedProducts = sqlContext.sql(
      """SELECT
        |product,
        |timestamp_hour,
        |UnderExposed(page_view_count, purchase_count) AS negative_exposure
        |FROM activityByProduct
        |ORDER BY negative_exposure DESC
        |limit 5
      """.stripMargin)

    visitorsByProduct.foreach(println)
    activityByProduct.foreach(println)
  }
}
