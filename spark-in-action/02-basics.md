```scala
val licLines = sc.textFile("/usr/local/spark/LICENSE")
val lineCnt = licLines.count

val bsdLines = licLines.filter(line => line.contains("BSD"))
bsdLines.count

def isBSD(line: String) = { line.contains("BSD") }
val isBSD = (line: String) => line.contains("BSD")
val bsdLines1 = licLines.filter(isBSD)
bsdLines1.count
bsdLines.foreach(bLine => println(bLine))

//### 2.3.1
//# def map[U](f: (T) => U): RDD[U]

val numbers = sc.parallelize(10 to 50 by 10)
numbers.foreach(x => println(x))
val numbersSquared = numbers.map(num => num * num)
numbersSquared.foreach(x => println(x))

val reversed = numbersSquared.map(x => x.toString.reverse)
reversed.foreach(x => println(x))

val alsoReversed = numbersSquared.map(_.toString.reverse)
alsoReversed.first
alsoReversed.top(4)

//### 2.3.2

/************************************ terminal:
echo "15,16,20,20
77,80,94
94,98,16,31
31,15,20" > ~/client-ids.log
end terminal ********************************/

val lines = sc.textFile("/home/spark/client-ids.log")

val idsStr = lines.map(line => line.split(","))
idsStr.foreach(println(_))

idsStr.first

idsStr.collect

//# def flatMap[U](f: (T) => TraversableOnce[U]): RDD[U]

val ids = lines.flatMap(_.split(","))

ids.collect

ids.first

ids.collect.mkString("; ")

val intIds = ids.map(_.toInt)
intIds.collect

//# def distinct(): RDD[T]

val uniqueIds = intIds.distinct
uniqueIds.collect
val finalCount  = uniqueIds.count

val transactionCount = ids.count

//Pasting blocks of code
scala> val lines = sc.textFile("/home/spark/client-ids.log")
lines: org.apache.spark.rdd.RDD[String] = client-ids.log MapPartitionsRDD[12] at textFile at <console>:21
scala> val ids = lines.flatMap(_.split(","))
ids: org.apache.spark.rdd.RDD[String] = MapPartitionsRDD[13] at flatMap at <console>:23
scala> ids.count
res8: Long = 14
scala> val uniqueIds = ids.distinct
uniqueIds: org.apache.spark.rdd.RDD[String] = MapPartitionsRDD[16] at distinct at <console>:25
scala> uniqueIds.count
res17: Long = 8
scala> uniqueIds.collect
res18: Array[String] = Array(16, 80, 98, 20, 94, 15, 77, 31)

//### 2.3.3
//# def sample(withReplacement: Boolean, fraction: Double, seed: Long = Utils.random.nextLong): RDD[T]

val s = uniqueIds.sample(false, 0.3)
s.count
s.collect

val swr = uniqueIds.sample(true, 0.5)
swr.count
swr.collect

//# def takeSample(withReplacement: Boolean, num: Int, seed: Long = Utils.random.nextLong): Array[T]

val taken = uniqueIds.takeSample(false, 5)
uniqueIds.take(3)

//### 2.4

//Implicit conversion:
class ClassOne[T](val input: T) { }
class ClassOneStr(val one: ClassOne[String]) {
    def duplicatedString() = one.input + one.input
}
class ClassOneInt(val one: ClassOne[Int]) {
    def duplicatedInt() = one.input.toString + one.input.toString
}
implicit def toStrMethods(one: ClassOne[String]) = new ClassOneStr(one)
implicit def toIntMethods(one: ClassOne[Int]) = new ClassOneInt(one)

scala> val oneStrTest = new ClassOne("test")
oneStrTest: ClassOne[String] = ClassOne@516a4aef
scala> val oneIntTest = new ClassOne(123)
oneIntTest: ClassOne[Int] = ClassOne@f8caa36
scala> oneStrTest.duplicatedString()
res0: String = testtest
scala> oneIntTest.duplicatedInt()
res1: 123123

//### 2.4.1
intIds.mean
intIds.sum

intIds.variance
intIds.stdev

//### 2.4.2
intIds.histogram(Array(1.0, 50.0, 100.0))
intIds.histogram(3)

//# def sumApprox(timeout: Long, confidence: Double = 0.95): PartialResult[BoundedDouble]
//# def meanApprox(timeout: Long, confidence: Double = 0.95): PartialResult[BoundedDouble]
```
