package org.sia.chapter03App

import org.apache.spark.sql.SparkSession

/**
 * @author Piyush Patel
 */
object App {

  def main(args : Array[String]) {
    val spark = SparkSession.builder()
      .appName("Github push counter")
      .master("local[*]")
      .getOrCreate()

    val sc = spark.sparkContext
    
    val homeDir = System.getenv("HOME")
    val inputPath = homeDir + "/sia/github-archive/2015-03-01-0.json"
    val ghLog = spark.read.json(inputPath)
    
    val pushes = ghLog.filter("type = 'PushEvent'")
    
    pushes.printSchema
    println("all events: " + ghLog.count)
    println("only pushes: " + pushes.count)
    pushes.show(5)

//    val col = sc.parallelize(0 to 100 by 5)
//    val smp = col.sample(true, 4)
//    val colCount = col.count
//    val smpCount = smp.count
//
//    println("orig count = " + colCount)
//    println("sampled count = " + smpCount)
  }

}
