object CaseClass {
  def main(args: Array[String]) {
    case class Character(name: String, isHacker: Boolean)
    val nail = Character ("Nail", true)
    val joyce = nail.copy(name="Joyce")
    println(nail == joyce)
    println(nail.equals(joyce))
    println(nail.equals(nail))
    println(nail.hashCode())

    println(nail)
    joyce match {
      case Character(x, true) => s"$x is a hacker"
      case Character(x, false) => s"$x is not a hacker"
    }
  }
}
