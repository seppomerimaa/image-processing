

/**
 * @param fullImagePath e.g. src/main/resources/baby-goat.jpg
 * @param imageName e.g. baby-goat.jpg
 * @param imageType e.g. jpg, png, gif, etc.
 */
case class ImageInputParams(fullImagePath: String, imageName: String, imageType: String)

/**
 * Parse the arguments that we need out of those provided, falling back on defaults where appropriate.
 * Exposed params:
 *
 * outputPath -- e.g., src/main/resources
 * k -- e.g., 4
 * imageParams -- an array of ImageInputParams
 */
class ComparisonParams(private val args: Array[String]) {
  val outputPath = if (args.length == 0 || args(0).length == 0) {
    println("No output directory provided, defaulting to src/main/resources")
    "src/main/resources"
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

  val imageParams = args.tail.tail.map(parseImageParam)


  private def parseImageParam(imagePath: String): ImageInputParams = {
    val fullImagePath = if (imagePath.length < 5) {
      "src/main/resources/baby-goat.jpg"
    } else {
      imagePath
    }

    val splitByPeriods = fullImagePath.split('.')
    val imageType = splitByPeriods.reverse.head


    val splitBySlashes = fullImagePath.split('/').reverse
    val imageName = splitBySlashes.head

    new ImageInputParams(fullImagePath, imageName, imageType)
  }
}
