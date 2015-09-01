/**
 *  Parses an array of strings into the arguments that we need to run our toning app.
 */
class ToningParams(private val args: Array[String]) {

  val fullImagePath: String = if (args.length == 0 || args(0).length < 5) {
    println("No source image provided, defaulting to an adorable baby goat.")
    "src/main/resources/baby-goat.jpg"
  } else {
    args(0)
  }

  val hashPath: String = if (args.length < 2 || args(1).length < 5) {
    println("No image hash provided, defaulting to the hash of an adorable baby goat.")
    "src/main/resources/hashed-baby-goat.jpg"
  } else {
    args(1)
  }

  val outputPath = if (args.length < 3 || args(2).length == 0) {
    println("No output directory provided, defaulting to src/main/resources")
    "src/main/resources"
  } else {
    args(2)
  }

  private val splitByPeriods = fullImagePath.split('.')
  val imageType = splitByPeriods.reverse.head


  private val splitBySlashes = fullImagePath.split('/').reverse
  val imageName = splitBySlashes.head
}
