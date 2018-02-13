class Animal(animalName: String, animalAge: Int) {
  def getAnimalName(): String = {
    animalName
  }
  def getAnimalAge(): Int = {
    animalAge
  }
}

object RunAnimalExample extends App {
  val animalObj = new Animal("Cat", -1)
  println(animalObj.getAnimalName)
  println(animalObj.getAnimalAge)
}
