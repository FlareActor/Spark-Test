import java.io.File

import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}
import temp.JavaSample

import scala.tools.nsc.io.Path

/**
  * Created by wangdexun on 2017/2/16.
  * RDD操作（转化和行动）
  */
object SparkTestForRDD {

  /**
    * 设置log打印级别
    */
  Logger.getLogger("org").setLevel(Level.WARN)

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf()
      .setMaster("local")
      //      .set("deploy-mode", "client")
      .setAppName("RDD Test")
    val sc = new SparkContext(conf)
    /**
      * 创建一个标准RDD
      */
    //保证每个执行器节点上相同的位置上有相同的本地文件
    //    val input = sc.textFile("file:///Users/wangdexun/Desktop/SparkTest/src/main/resources/README.md")
    val input = sc.textFile("hdfs://localhost:9000/input/NOTICE 2.txt")
    /**
      * 单词统计
      */
    val words = input.flatMap(_.split(" "))
    val counts = words.map((_, 1)).reduceByKey((x, y) => x + y)
    //    val counts2 = words.countByValue()
    //    counts.saveAsTextFile("src/main/resources/WordCount")
    /**
      * 将RDD中的数据持久化
      */
    counts.persist()
    /**
      * 返回不同类型的"reduce"
      */
    val input2 = sc.parallelize(List(1, 3, 2, 2))
    val result = input2.aggregate((0, 0))((U, x) => (U._1 + x, U._2 + 1), (U1, U2) => (U1._1 + U2._1, U1._2 + U2._2))
    //    println(result)
    /**
      * Pair RDD
      */
    val input3 = input2.map(x => (x, x + 10))
    val input4 = input2.map(x => (x + 1, x - 10))
    //    printRDD(input3)
    //    printRDD(input4)
    //    printRDD(input3.aggregateByKey((0, 0))((U, x) => (U._1 + x, U._2 + 1), (U1, U2) => (U1._1 + U2._1, U1._2 + U2._2))
    //      .mapValues(U => U._1 / U._2)) //求平均值
    //    println(avgResult.collectAsMap().mkString(","))
    //    println(input3.lookup(2)) //查找键对应的所有值

    /**
      * RDD分区方式
      */
    val pairs = sc.parallelize(List((1, 1), (5, -1), (2, 2), (3, 3), (2, 4)))
      .partitionBy(new HashPartitioner(2)).persist()
    //自动生成HashPartitioner
    val pairs02 = pairs.groupByKey()
    //自动生成RangePartitioner
    val pairs03 = pairs.sortByKey()
    //map操作使新RDD丢失了原RDD的分区方式
    val pairs04 = pairs.map(a => (a._1, a._2 * 2))
    val paris05 = pairs.mapValues(_ + 20) //由于键不变，也继承了相同的分区方式
    //    println(input3.join(input4).partitioner) //自动设置Hash分区

    /**
      * 数值计算
      */
    val digits = sc.parallelize(List(1, 2, 3, 4, 5))
    val stats = digits.stats() //汇总统计一次数据
    //    println(stats.sum, stats.mean, stats.stdev)

    //    sc.stop()
  }

  def printRDD[T](rdd: RDD[T]): Unit = {
    println(rdd.collect().mkString(","))
  }
}
