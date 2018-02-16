# Scala

Scala is scalable language. It is object oriented. Scala does not allow multiple inheritance but it is possible using subclassing and mixin-based composition. Scala supports functional programming. Scala allows to define function easily and allows nested functions.

Scala is statically typed language, means the type of a variable is known at compile time. Scala runs on JVM. The runtimes of Java and Scala are the same. Scala compiles with `scalas` command. Scala can execute Java code. Scala can also do concurrent and synchronized processing. In Scala, everything looks like an object. Scala uses type inference which deducts the type of variable at compile time. Launch Spark REPL using `scala` command.

```scala
var a = 20
var b = 20.5
var  c = "String"
val d = 5000
val f = 'cat' // throws error
val f = "cat"

// There are two types of variables
// mutable variables are declared using var
// immutable variables are declared using val

// We can also explicitly mention the type of value
i:Int = "hello"  // throws error

"abc" map(x => (x + 1).toChar) // bcd

// We can find all the methods on a variable using Tab two times
x = "hello"
x.re // press Tab twice and it will show all functions starting with re

val x = new AnyRef(def helloWord = "Hello World")
x.helloWord
```

In Scala, we can define function inside a function.

```scala
def sum(vector: List[Int]): Int = {
  // nested helper method is not accessible outside this function
  def helper(acc: Int, remaining: List[Int]): Int = remaining match {
    case Nil => acc
    case _ => helper(acc + remaining.head, remaining.tail)
  }
  helper(0, vector)
}
```

Scala supports Byte, Short, Int, Long, Float, Double, Char, String, Boolean, Unit, Null, Nothing, Any, AnyRef. Here Unit is similar to void. Nothing is a subtype of every other type, includes no value. Any is supertype of any type. AnyRef is super type of any reference type.

Create [HelloWorld.scala](HelloWorld.scala) and then compile the code using `scalac HelloWorld.scala`.
We can run the bytecode using `scala HelloWorld`.

If we want to put the generated code in some other directory, we can specify that with -d option while compiling. The directory must exist.

`scalac -d classes HelloWorld.scala`

We can also use generated class files using -cp option.

`scala -cp classes HelloWorld`

Scala has traits that are similar to Java interfaces.
Run the script file [script.sh](script.sh) using `./script.sh` command.

Scala is lazily evaluated. Functions can be defined inline as follows and the last expression is the return value.

```scala
val x = {
  println("x")
  20
}
// Now x = 20
```

## Loops

```scala
var i = 0
while (i < 5) {
  println(i)
  i += 1
}

for (i <- 1 to 10) {
  println(i)
}

for (i <- 1 until 10) {
  println(i)
}
// Scala does not support i++
do {
  println(i)
  i += 1
} while(i < 5)
```

## Object Oriented scala

Methods in Scala are defined with parameter types and return types. Although types can also be inferred

```scala
def min(x1:Int, x2:Int): Int = {
  if (x1 < x2) x1 else x2
}

// curly braces are not absolutely necessary
def getPiValue():Double = 3.14159

def sayHello(person:String): "Hello " + person

def addInt(x:Int, y:Int): Int = {
  var sum:Int = 0
  sum = x + y
  sum
}
```

### classes
Classes are blueprint like any other language.

```scala
class Animal {
  var animalName = null
  var animalAge = -1
  def setAnimalName (name: String) {
    this.animalName = name
  }

  def getAnimalName():String = {
    animalName
  }
}
```

In Scala, **object** is a keyword. Anything declared with object cannot be instantiated. It is a singleton instance.

```scala
object HelloWorld {
  def main(args: Array[String]) {
    println("Hello World")
  }
}
```

When a singleton object is named the same as a class, it is called a companion object. A companion object must be defined in the same source file as the class.

Scala also supports `final` keyword. In scala, we can override variables even if they are declared using `val`. However, `final` variables cannot be overridden.

```scala
class Animal {
  val age = 2 // if this variable is declared as final, it cannot be overridden
}

class Cat extends Animal {
  override val age = 3 // this works fine
  def printAge = {
    println(age)
  }
}
```

### Access and visibility

| Modifier | Class | Companion Object | Package | Subclass | Project |
|--------:|--------:|--------------:|-------------:|------------:|
| Default | Yes | Yes | Yes | Yes | Yes |
| Protected | Yes | Yes | Yes | No | No |
| Private | Yes | Yes | No | No | No |

There are no `public` modifiers. It is by default.

Access modifiers in Scala can be augmented. For example, a modifier can be applied to restrict access to a particular package as follows.

```scala
package Country {
  package Professional {
    class Executive {
      private[Professional] var jobTitle = "Big Data Engineer"
      private[Country] var friend = "Amar Akbar Anthony"
      protected[this] var secret = "Age"

      def getInfo(another:Executive) {
        println(another.jobTitle)
        println(another.friend)
        println(another.secret) // not allowed
        println(this.secret) // allowed
      }
    }
  }
}
```

### constructors

In Scala, there are two types of constructors, primary and auxiliary.  Primary constructor is the class's body and its parameter list appears after the class name.

```scala
class Animal(animalName: String, animalAge: Int) {
  def getAnimalName(): String = {
    animalName
  }
  def getAnimalAge(): Int = {
    animalAge
  }
}
```

We create an use the object as follows:

```scala
object RunAnimalExample extends App {
  val animalObj = new Animal("Cat", -1)
  println(animalObj.getAnimalName)
  println(animalObj.getAnimalAge)
}
```

[Animal Run Example](Animal2.scala)

Parameters are given in the class definition to represent constructors. If we declare a constructor, we cannot create a class without providing the default values of the parameters that are specified in the constructor.

[Auxiliary constructor](AuxiliaryConstructor.scala)

```shell
scalac AuxiliaryConstructor.scala
scala constructors
```

### Traits

It is similar to the notion of interface in Java, execept that it can contain concrete methods. They don't have constructors.

```scala
trait Animal {
  val age:Int
  val gender:String
}
```

We can extend a trait or a class using `extends` keyword. However, traits cannot be instantiated.

The difference between abstract class and traits is that in an abstract class you have constructor parameters, type parameters and multiple parameters. However, a trait in Scala can have only type parameters. Abstract class can extend trait.

Abstract class can have constructor parameters as well as type parameters.

```scala
abstract class Animal(name:String = "notset") {
  def getAnimalAge
  def getAnimalGender: starting
  def getAnimalName: String = {
    animalName
  }
}
```

To overrride a method we can use `override` keyword.

A **case class** is an instantiatiable class that includes several automatically generated methods. It is like switch statement.

**Packages** are supported by scala as well. We can define package using `package` keyword. We can import files using following syntax

```scala
import java.io.File // single import
import java.io._ // everything imported
import java.io.{File, IOException} // multiple imports from a package
import java.util.{List => UtilList} // List renamed to UtilList
```

We can make use of Java programming inside scala.

```scala
import java.util.ArrayList
val animals = new ArrayList[String]
animals.add("cat")
```

**Pattern Matching** is used like a case statement.
Check [this](PatternTest.scala) example.

[Pattern matching example](PatternMatching2.scala)

Scala supports **implicit** conversion. That means it will automatically convert one value to another and some other implicit operations.

```scala
implicit def stringToInt(s:String) = s.toInt

def add (x:Int, y: Int) = x + y
add(1, "2")
```

### Generics in scala

We can define [generic classes in Scala](Stack.scala)

## Build Tool

For scala, we use SBT tools for building, but Maven, Gradle can be used.
1. Go to project folder for example test_proj.
2. Create build.sbt file in this folder.
3. Now run `sbt` command. It will download bunch of files and prompt will change to `>` symbol.
4. Now we can run different commands like `compile`, `test`, `run`, etc. to work on it.
5. We can also use Maven or Gradle with eclipse, but we need to install various plugins for them to work.


## Functional Programming in Scala

Functional programming ensures immutability of data. It is also about writing pure functions.

Functional programming provides three main advantages.

- Close to mathematical thinking
- No side efects, so it allows parallelization and also easy for debugging
- Fewer lines of code without sacrificing clarity.

Scala supports pure functions as well as higher order functions. **Pure functions** are easy to implement

```scala
def pureFunc(cityName: String) = s"I live in $cityName"

def notPureFuunc(cityName: String) = println(s"I live in $cityName")
```

**Anonymous functions** can be declared like JavaScript.

```scala
def transferMoney(money: Double, bankFee:Double => Double) : Double = {
  money + bankFee(money)
}

transferMoney(100, (amount: Double) => amount * 0.05) // 105.0
```

**Higher order functions** allow to pass functions as parameters and even return a function as a result from another function.

```scala
object Test {
  def main(args: Array[String]) {
    println(testHOF(paramFunc, 10))
  }

  def testHOF(func:Int => String, value: Int) = func(value)
}
```

```scala
def applyFunctionOnRange(begin: Int, end: Int, func: Int => AnyVal): Unit = {
  for (i <- begin to end)
    println(func(i))
}
```

Function as a return value

```scala
def transferMoney(money: Double) = {
  if(money > 10000)
    (money: Double) => "Dear Custoemer, we are going to add the following fee: " + money * 0.05
  else
    (money: Double) => "Dear Customer, we are going to add the following fee: " + money * 0.01
}
val returnedFunction = transferMoney(15000)
returnedFunction(1500)
```

### Error Handling
Scala can throw exception using `throws` just like Java language. We catch the exception using `catch` block but it uses case match. It also supports `finally` keyword.

```scala
try {

}
catch {
  case foo: FooException => handleFoo(foo)
  case bar: BarException => handleBar(bar)
  case _: Throwable => println("Some other error")
}
finally {
  // do something to clean up
}
```

[File exception handling example](FileExceptionHandling.scala)

Scala also supports `Either`. Either[X, Y] is an instance that contains either an instance of X or an instance of Y but not both. These subtypes are called Left and Right.

[Either example](EitherExample.scala)

If you want to run tasks in a non-blocking way and need a way to handle the results when they finish, Scala provides **Futures**.


## Collection API

Mutable collection values can be changed, updated or extended when necessary. Most collection classes are located in the package `scala.collection`, `scala.collection.immutable` and `scala.collection.mutable`. Scala collections extend from Traversable trait.

**Traversable** is the root of collections hierarchy. It has one abstract method `foreach`. **Iterable** is the second in the hierarchy. It has abstract method called `iterator` and implements `foreach`.

**Seq**: sequence is divided into LinearSeq and IndexedSeq.

Scala imports immutable collections by default and if you want to use mutable, you need to import them.

### Arrays

Array is a mutable collection.

```scala
val numbers: Array[Int] = Array[Int](1,2,3,5)

println("The elements of array")
for(elem <- numbers) {
  println(" " + elem)
}

println(numbres(2))

var total = 0
for(i <- 0 to (numbers.length - 1)) {
  total += numbers(i)
}
println("Sum : " + total)

// Another way of creating Array
var myArray1 = Range(5, 20, 2)
var myArray2 = Range(5, 20)

var myArray3 = concat(myArray1, myArray2)

var myMatrix = ofDim[Int](4,4)
for(i <- 0 to 3) {
  for (j <- 0 to 3) {
    myMatrix(i)(j) = j
  }
}
```

### Lists

```scala
val numbers = List(1, 2, 3, 4)
numbers(3) = 10 // throws error

val numbers = 1 :: 2 :: 3 :: Nil // another syntax to create list
numbers.head
numbers.tail

numbers.isEmpty
numbers.length

var numbers2 = List (2,3)
numbers ::: numbers2 // combine 2 lists and make third
```

### Sets

In sets, order will not be preserved and sets don't allow duplicate elements.

```scala
val numbers = Set(1,3,5)
val numbers2 = Set(2,4,6)
numbers.head
numbers.tail
numbers.isEmpty
numbers ++ numbers2 // combine two sets
numbers.min
numbers.max
```

### Tuples

It is used to combine fixed numbers of items together. Scala has many Tuple classes based on the numbers of arguments. If we want tuple with three arguments we can create using:

```scala
val t = new Tuple3(20, "hello", Console)
val tup1 = (20, "Hello", Console)
tup1._2 // second element
```

### Maps

Maps is key-value pair.

```scala
Map (1 -> 2)
Map(2 -> "Two", 3 -> "Three")
val capitals = Map("Ireland" -> "Dublin", "Britain" -> "London", "Germany" -> "Berlin")
capitals.keys // returns list
capitals.values
capitals.isEmpty
capitals.get("Ireland")
```

### Option

An option can be either `Some` or `None` type. Scala's map produces Some vale if a value corresponding to a given key has been found, or None if the key is not defined in the Map.

```scala
def show(x: Option[String]) = x match {
  case Some(s) => s
  case None => "?"
}
```

Scala has different functions for working with collections.

```scala
var l = List(1,2,3,4)
l.forall(elem => elem % 2 == 0) // all elements even?
l.filter(elem => elem % 2 == 0)
l map (elem => elem * 2) // function can also be called like this
l.take(2) // takes first two elements

l.groupBy(x => if (x % 2 == 0) "even" else "odd") // returns Map with even and odd.
l.init // get all elements except last one
l.drop(2) // drop first two elements

var l  = List(1,2,3,4,5)
// dropWhile and takeWhile stops as soon as condition fails
l dropWhile (x => x < 5) // drop elements less than 5 and return new list
l takeWhile (x => x < 4)

// flatMap flattens list of Lists
var l = List(List(1,3), List(2,4))
l flatMap (x => x) // List(1,3,2,4)
l flatMap (x => x.map(x => x * x))
```

**Implicit Parameter** is a parameter passed to a constructor or a method and is marked as implicit, which means that the compiler will search for an implicit value within the scope if you don't provide a value for this parameter.
