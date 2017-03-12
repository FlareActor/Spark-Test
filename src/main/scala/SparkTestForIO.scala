import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}
import SparkTestForRDD.printRDD
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.io.{IntWritable, LongWritable, Text}
import org.apache.hadoop.mapreduce.lib.input.{KeyValueTextInputFormat, TextInputFormat}
import org.apache.hadoop.mapreduce.lib.output.{FileOutputFormat, TextOutputFormat}


/**
  * Created by wangdexun on 2017/2/17.
  * 数据的读取和存储
  */
object SparkTestForIO {

  Logger.getLogger("org").setLevel(Level.WARN)

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("IO Test")
    val sc = new SparkContext(conf)

    /**
      * 读取文件夹中的所有文本文件
      */
    //    val dirRDD = sc.wholeTextFiles("src/main/resources/temp*.txt")
    //    printRDD(dirRDD)
    //    printRDD(dirRDD.mapValues(nums => {
    //      val numbers = nums.split(" ").map(_.toDouble)
    //      numbers.sum / numbers.length
    //    }))
    /**
      * 读取SequenceFile,由于Writable类没有实现java序列化接口，使用map转换
      */
    //    val data = sc.sequenceFile("src/main/resources/SequenceFileExample", classOf[IntWritable], classOf[Text])
    //      .map { case (key, value) => (key.get(), value.toString) }
    val data2 = sc.sequenceFile[Int, String]("src/main/resources/SequenceFileExample")
    //    printRDD(data)
    printRDD(data2)
    val outputData = data2.map { case (key, value) => (key + "wdx&gsj", value.hashCode) }
    //    outputData.saveAsSequenceFile("src/main/resources/SequeceFileOutput")
    /**
      * Hadoop输入输出格式
      * TextInputFormat默认数据类型为<LongWritable,Text>,KeyValueTextInputFormat默认<Text,Text>
      * TextOutputFormat却可以指定数据类型
      */
    val path: Path = new Path("src/main/resources/FileOutputFormatExample")
    val fs: FileSystem = FileSystem.getLocal(new Configuration())
    if (fs.exists(path))
      fs.delete(path, true)
    outputData.saveAsNewAPIHadoopFile(path.toString, classOf[Text],
      classOf[IntWritable], classOf[TextOutputFormat[Text, IntWritable]])
    val data3 = sc.newAPIHadoopFile[Text, Text, KeyValueTextInputFormat](path.toString)
    //          .map { case (key, value) => (key.toString, value.toString.toInt) }
    //    printRDD(data3)//rdd中的数据要从工作节点处序列化传送到驱动器节点上，所以NotSerializableException
    //    println(data3.count(), data3.getStorageLevel.toString()) //不需要序列化传送数据，不报错
    //    data3.foreach(println(_)) //不需要序列化传送数据
    /**
      * 读取HDFS中的数据
      */
    val hdfsPath = "hdfs://localhost:9000/input"
    val hdfsData = sc.wholeTextFiles(hdfsPath)
    //    printRDD(hdfsData)
    //    val outputData2 = sc.parallelize(List(("wdx", 520), ("gsj", 111)))
    //    outputData2.saveAsNewAPIHadoopFile(hdfsPath + "/sparkexample", classOf[Text],
    //      classOf[IntWritable], classOf[TextOutputFormat[Text, IntWritable]])

  }
}
