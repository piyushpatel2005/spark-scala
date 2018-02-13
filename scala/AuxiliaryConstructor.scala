class Hello (primaryMessage: String, secondaryMessage: String) {
  def this(primaryMessage:String) = this(primaryMessage, "") // auxiliary constructor
  def sayHello() = println(primaryMessage + secondaryMessage)
}

object Constructors {
  def main(args: Array[String]): Unit = {
    val hello = new Hello("Hello world", "I'm in trouble.")
    hello.sayHello()
  }
}
