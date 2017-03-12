import org.apache.hadoop.io.IntWritable
import org.apache.log4j.{Level, Logger}
import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}
import SparkTestForRDD.printRDD
import org.apache.spark.util.{AccumulatorV2, LongAccumulator}


/**
  * Created by wangdexun on 2017/2/20.
  */
object SparkTestForAdvance {


  Logger.getLogger("org").setLevel(Level.WARN)

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("IO Test")
    val sc = new SparkContext(conf)
    /**
      * Scala函数支持序列化
      */
    val m = (a: Int) => a * a
    //    println(m.isInstanceOf[Serializable])

    /**
      * 累加器accumulator
      */
    val acc = new LongAccumulator
    val myAcc = new myAccumulatorV2
    sc.register(acc)
    sc.register(myAcc)
    var r = -100
    val data01 = sc.parallelize(List(3, 4, 5, 6))
    val data02 = data01.map(v => {
      r = r + 1 //0,1,2,3 变量的副本
      acc.add(r)
      myAcc.add("GSJ" + r)
      r + v //3,5,7,9
    })
    data02.count() //转化操作是惰性的，行动操作触发之
    //    println(acc.value, acc.count)
    //    println(myAcc.value, myAcc.names)
    //    println(r) //驱动器程序中的变量不会改变
    /**
      * 广播共享变量(只读)
      */
    val signPrefixes = sc.broadcast(Map("gsj" -> 520)) //只读变量
    val data03 = data01.map(v => {
      println("广播变量", signPrefixes.value)
      v
    })
    //    data03.count()
    /**
      * 基于分区进行操作
      */
    val data04 = sc.parallelize(List((1, 2), (1, 5), (2, 3), (4, 6)))
      .partitionBy(new HashPartitioner(3))
    val data05 = data04.mapPartitionsWithIndex((index, iter) => {
      //只在每个分区上运行一次
      println("mapPartitions", index, iter.length)
      iter.map(pair => pair._1 * pair._2)
    }).persist()
    data05.count()
    printRDD(data05)
    println(data05.partitioner, data05.getNumPartitions)

  }

  class A extends Serializable {
    var a: Int = -4

    def add() = {
      a += 3
    }
  }

  /**
    * 自定义累加器AccumulatorV2
    */
  class myAccumulatorV2 extends AccumulatorV2[String, Int] {

    private var _names = ""
    private var _count = 0

    override def isZero: Boolean = _names.isEmpty && _count == 0

    override def copy(): AccumulatorV2[String, Int] = {
      val newAcc = new myAccumulatorV2
      newAcc._names = this._names
      newAcc._count = this._count
      newAcc
    }

    override def reset(): Unit = {
      this._names = ""
      this._count = 0
    }

    override def add(v: String): Unit = {
      this._names = this._names.concat(v)
      this._count += -1
    }

    override def merge(other: AccumulatorV2[String, Int]): Unit = {
      other match {
        case o: myAccumulatorV2 =>
          this._names = this._names.concat(o._names)
          this._count += o._count
        case _ => throw new RuntimeException("使用了不匹配的类")
      }
    }

    override def value: Int = _count

    def names: String = this._names

  }

}
