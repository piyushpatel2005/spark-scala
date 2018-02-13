class Stack[A] {
  private var elements: List[A] = Nil
  def push (x:A) {
    elements = x :: elements
  }

  def peek: A = elements.head
  def pop(): A = {
    val currentTop = peek
    elements = elements.tail
    currentTop
  }
}

object GenericStack {
  def main(args: Array[String]) {
    val stack = new Stack[Int]
    stack.push(1)
    stack.push(2)
    stack.push(3)
    println(stack.pop)
    println(stack.pop)
  }
}
