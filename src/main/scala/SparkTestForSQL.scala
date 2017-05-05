/**
  * Created by wangdexun on 2017/5/3.
  */

import org.apache.hadoop.io.{IntWritable, Text}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{Row, SparkSession}
import SparkTestForRDD.printRDD


case class Person(name: String, age: Int)

case class Teacher(age: Int)

object SparkTestForSQL {

  Logger.getLogger("org").setLevel(Level.WARN)

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("Spark")
      .config("spark.some.config.option", "some-value")
      .master("local")
      //      .enableHiveSupport()
      .getOrCreate()
    import spark.implicits._
    import spark.sql

    val text = spark.sparkContext.sequenceFile("src/main/resources/SequenceFileExample", classOf[IntWritable], classOf[Text])
      .map { case (key, value) => (key.get(), value.toString) }
      .toDF()
    //    text.createOrReplaceTempView("temp")
    //    text.show()
    println(text.getClass)
    text.map(_.mkString).show()

    //    val caseClassDS = Seq(Person("wdx", 1)).toDS()
    //    caseClassDS.show()

    //    val pDS = Seq(1, 2, 3).toDS().as[Int]
    //    pDS.show()

    //    sql("CREATE TABLE IF NOT EXISTS src (key INT, value STRING)")
    //    sql("use wdx")
    //    sql("show tables").show()
    //    val sqlDF = sql("select * from table1")
    //    sqlDF.show()
  }

}

