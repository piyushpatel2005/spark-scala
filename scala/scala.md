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
