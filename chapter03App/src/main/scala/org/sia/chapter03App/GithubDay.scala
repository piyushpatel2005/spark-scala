package org.sia.chapter03App

import org.apache.spark.sql.SparkSession

/**
 * @author Piyush Patel
 */
object GithubDay {

  def main(args : Array[String]) {
    val spark = SparkSession.builder()
      .getOrCreate()

    val sc = spark.sparkContext
    
    val ghLog = spark.read.json(inputPath)
    
    val pushes = ghLog.filter("type = 'PushEvent'")

    val grouped = pushes.groupBy("actor.login").count
    val ordered = grouped.orderBy(grouped("count").desc)
    
    val empPath = homeDir + "/first-edition/ch03/ghEmployees.txt"
    val employees = Set() ++ (
        for {
          line <- fromFile(empPath).getLines
        } yield line.trim
    )
    val bcEmployees = sc.broadcast(employees)
    
    import spark.implicits._
    val isEmp = (user:String) => bcEmployees.value.contains(user)
    val isEmployee = spark.udf.register("SetContainsUdf", isEmp)
    val filtered = ordered.filter(isEmployee($"login"))
    filtered.write.format(args(3)).save(args(2))
  }

}
