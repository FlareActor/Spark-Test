import javax.annotation.processing.FilerException

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

import Array._
import collection.mutable
import java.io._

import scala.io.{BufferedSource, Source}

/**
  * Created by wangdexun on 2017/2/11.
  */


object FirstScala {

  def main(args: Array[String]): Unit = {

    // 列表
    // 列表是不可变的，值一旦被定义了就不能改变
    val x = List(1, 2, 3, "wdx")
    val y = 2 :: (3 :: ("gsj" :: Nil))
    val z = x ::: y
    z.productIterator.foreach(println)


    //每次使用传名调用时，解释器都会计算一次表达式的值（不同于传值调用）
    delayed(p = 23, t = time())

    //高阶函数
    println(apply(layout, 10, w = false))

    //匿名函数
    println(inc(3, "wdx"))

    //文件IO
    //    val f: File = new File("src/main/resources/sample_libsvm_data.txt")
    //    val bufferedReader: BufferedReader = new BufferedReader(new FileReader(f))
    //    var line: String = bufferedReader.readLine()
    //    //    while (line != null) {
    //    //      println(line)
    //    //      line = bufferedReader.readLine()
    //    //    }
    //    val ff = Source.fromFile("src/main/resources/sample_libsvm_data.txt")
    //    //        for (line <- ff.getLines()) {
    //    //          println(line)
    //    //        }
    //    val writer: PrintStream = new PrintStream(new File("src/main/resources/temp.txt"))
    //    for (i <- 1 to 10) {
    //      writer.write(97)
    //    }
    //    writer.flush()
    //    writer.close()
  }


  //匿名函数
  val inc = (x: Int, y: String) => x + y

  //函数作为参数
  def apply(f: (Int, Boolean) => String, v: Int, w: Boolean = true) = {
    f(v, w)
  }

  //泛型
  def layout[A, B](x: A, y: B) = {
    println(x.getClass.getName, y.getClass.getName)
    "[" + x.toString + "," + y.toString + "]"
  }


  def time() = {
    println("获取时间，单位为纳秒")
    System.nanoTime
  }

  //传名调用
  def delayed(t: => Long, p: Int = 3) = {
    println("在 delayed 方法内")
    println("参数： " + t)
    println(t)
  }
}
