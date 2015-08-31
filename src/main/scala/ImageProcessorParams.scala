/**
 * Parse the arguments that we need out of those provided, falling back on defaults where appropriate.
 * Exposed arguments:
 *
 * fullImagePAth -- e.g. src/main/resources/baby-goat.jpg
 * imageName -- e.g., baby-goat.jpg
 * imageType -- e.g., jpg, png, gif, etc.
 * outputPath -- e.g., src/main/resources
 * k -- e.g., 4
 */
class ImageProcessorParams(private val args: Array[String]) {
  val fullImagePath: String = if (args.length == 0 || args(0).length < 5) {
    println("No source image provided, defaulting to an adorable baby goat.")
    "src/main/resources/baby-goat.jpg"
  } else {
    args(0)
  }

  private var mutableK = 4
  try {
    mutableK = args(1).toInt
  } catch {
    case e: NumberFormatException => "Failed to parse second argument to an int, defaulting to 4."
    case e: ArrayIndexOutOfBoundsException => "No second argument provided, defaulting to 4."
  }
  val k = mutableK

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
