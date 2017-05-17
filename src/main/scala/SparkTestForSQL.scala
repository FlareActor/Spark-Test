/**
  * Created by wangdexun on 2017/5/3.
  */

import org.apache.hadoop.io.{IntWritable, Text}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{Row, SQLContext, SparkSession}
import SparkTestForRDD.printRDD
import org.apache.spark.SparkConf

case class Person(name: String, age: Int)

case class Teacher(age: Int)

object SparkTestForSQL {

  Logger.getLogger("org").setLevel(Level.WARN)

  def main(args: Array[String]): Unit = {
    /**
      * SQLContext和HiveContext的结合，封装了SparkContext，一个新的编程入口
      */
    val spark = SparkSession.builder()
      .appName("Spark")
      .config("spark.some.config.option", "some-value")
      .master("local")
      .enableHiveSupport()
      .getOrCreate()
    import spark.implicits._
    import spark.sql

    /**
      * 查看数据库元数据
      */
    //    spark.catalog.listDatabases().show(false)
    //    spark.catalog.listTables("wdx").show(false)
    //    spark.catalog.listColumns("wdx","table1").show(false)

    val text = spark.sparkContext.sequenceFile("src/main/resources/SequenceFileExample", classOf[IntWritable], classOf[Text])
      .map { case (key, value) => (key.get(), value.toString) }
      .toDF("w", "d")

    //    text.createOrReplaceTempView("tempView")

    val caseClassDS = Seq(Person("wdx", 1), Person("gsj", 2)).toDS()
    caseClassDS.show()

    /**
      * SQL On Hive
      */
    sql("use wdx")
    sql("show tables").show()
    val sqlDF = sql("select * from table1")
    sqlDF.show()
  }

}

