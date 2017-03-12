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

    //tuple
    val (myVal1, myVal2) = Pair(40, "FILL")

    // 列表
    // 列表是不可变的，值一旦被定义了就不能改变
    val x = List(1, 2, 3, "wdx")
    val y = 2 :: (3 :: ("gsj" :: Nil))
    val z = y ::: x
    //    for (i <- z.indices) {
    //      println(z(i))
    //    }
    //    x.productIterator.foreach(f => println(f))
    //    println(x.count(p => p != 2))


    //Map
    val m: mutable.Map[AnyVal, Int] = mutable.Map(5 -> 3, 3 -> 5)
    m += (7 -> 3)
    //    for (i <- m.keys) {
    //      println(m(i))
    //    }


    //元组
    val t = ("wdx", "love", "gsj", 520)
    //    t.productIterator.foreach(i => println(i))


    //数组
    //    var z: Array[String] = new Array[String](3)
    //    z = Array("wdx", "love", "gsj")
    //    val myList = range(10, 20, 3)


    //StringBuffer
    //    val greeting = "gsj love wdx"
    //    val buf = new StringBuilder
    //    buf += '5'
    //    buf ++= greeting
    //    println(buf.toString())

    //每次使用传名调用时，解释器都会计算一次表达式的值（不同于传值调用）
    //    delayed(p = 3, t = time())

    //for循环和可变参数
    //    pringStrings("wdx", "gsj", "wire")

    //高阶函数
    //    println(apply(layout, 10, false))

    //匿名函数
    //    println(inc(3, "wdx"))

    //函数柯里化
    //    println(stract("wdx")("gsj"))

    //类和对象
    val p: Location = Location.getInstance(91)
    val q: Location = Location.getInstance(32)
    //    p.draw()

    //"Switch"如果没有匹配则会抛出异常
    //    val xx = 1
    //    xx match {
    //      case 1 =>
    //        println("wdx")
    //        println("love")
    //      case 5 => println("gsj")
    //      case _=>println("whatever")
    //    }

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

    //变换
    val l = List(1, 2, 3, 4)
    val ll = l.map(f => List(f, f * 2))
    val lll = List('w', 'd', 'x')
    //    println(ll.toString())
    //    println(ll.flatten)
    //    println(l.flatMap(1 to _))
    //    println(l.reduceRight((a, b) => b * 2 - a)) //合并成单一元素
    //    println(l.sortWith((a, b) => a > b))
    //    println(l.filter(_ > 2), l.count(_ >= 2))
    //    println(l.zip(lll))

    //Option
    val mm = Map("wdx" -> "gsj")
    println(mm.get("wdx"))
    println(mm.get("love"))
  }

  //函数柯里化
  def stract(s1: String)(s2: String) = {
    s1 + s2
  }

  //匿名函数
  val inc = (x: Int, y: String) => x + y


  //函数作为参数
  def apply(f: (Int, Boolean) => String, v: Int, w: Boolean) = {
    f(v, w)
  }

  //泛型
  def layout[A, B](x: A, y: B) = {
    println(x.getClass.getName, y.getClass.getName)
    "[" + x.toString + "," + y.toString + "]"
  }


  def pringStrings(args: String*) = {
    for (s <- args) {
      println(s)
    }
    for (j <- 1 to 10; k <- 2 until 3 if j != 3; if j > 4) {
      println(j, k)
    }
  }

  def time() = {
    println("获取时间，单位为纳秒")
    System.nanoTime
  }

  //传名调用
  def delayed(t: => Long, p: Int = 3) = {
    println("在 delayed 方法内")
    println("参数： " + t)
    t
  }

  //类参数
  class Point(x: Int, y: Float) {
    var xx: Int = x

    def draw() = {
      println(x, y)
    }
  }

  class Location(x: Int, y: Float) extends Point(x: Int, y: Float) with Car {
    override def draw() = {
      super.draw()
      println("Child Class")
    }

    override def run(x: Any): String = {
      "run car"
    }
  }

  //伴生对象：object中的对象都是static静态的
  object Location {

    var location: Location = _

    val xxx: String = "GSJ"

    def getInstance(x: Int): Location = {
      if (location == null) {
        println("Init Location Instance")
        location = new Location(x, 3.9f)
      }
      location
    }

  }

  //接口。其实更像抽象类
  trait Car {
    def run(x: Any): String

    //允许方法实现
    def stop(y: Int): String = "Stop Car"
  }


  //  val conf = new SparkConf().setAppName("LogisticRegressionMail").setMaster("local")
  //  val sc = new SparkContext(conf)
  //  val file = sc.textFile("hdfs://localhost:9000/input/NOTICE4.txt")
  //  println(file.first())
  //  val examples: RDD[LabeledPoint] = MLUtils.loadLibSVMFile(sc, "src/main/resources/sample_libsvm_data.txt")
  //  println("Answer:" + examples.count())
}
