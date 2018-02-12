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
