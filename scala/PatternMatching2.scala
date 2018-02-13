object PatternMatching2 {
  def main(args: Array[String]): Unit = {
    println(comparison("two"))
    println(comparison("test"))
  }

  def comparison(x: Any): Any = x match {
    case 1 => "one"
    case "five" => 5
    case _ => "nothing else"
  }
}
