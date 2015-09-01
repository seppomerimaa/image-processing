import scala.math._

/**
 * Compare some images by their hashes.
 *
 * Example args:
 * src/main/resources/4 4 src/main/resources/baby-goat.jpg src/main/resources/baby-goat-copy.jpg src/main/resources/baby-goat-2.jpg src/main/resources/baby-goat-3.jpg
 * src/main/resources/8 8 src/main/resources/baby-goat.jpg src/main/resources/baby-goat-copy.jpg src/main/resources/baby-goat-2.jpg src/main/resources/baby-goat-3.jpg
 * src/main/resources/16 16 src/main/resources/baby-goat.jpg src/main/resources/baby-goat-copy.jpg src/main/resources/baby-goat-2.jpg src/main/resources/baby-goat-3.jpg
 *
 * src/main/resources/ 4 src/main/resources/baby-goat.jpg src/main/resources/baby-goat-overexposed.jpg src/main/resources/baby-goat-underexposed.jpg
 * src/main/resources/ 4 src/main/resources/baby-goat.jpg src/main/resources/baby-goat-cropped.jpg
 */
object ComparisonApp {
  def main(args: Array[String]): Unit = {
    val params = new ComparisonParams(args)
    val hashes = params.imageParams.map { imageParam =>
      val start = System.currentTimeMillis()
      val image = Image.create(imageParam.fullImagePath)

      val read = System.currentTimeMillis()
      //println(s"${imageParam.imageName} | read image: ${(read - start) / 1000.0}")

      val preprocessedImage = Preprocessor.processImage(image)

      val preprocess = System.currentTimeMillis()
      //println(s"${imageParam.imageName} | preprocess image: ${(preprocess - read) / 1000.0}")

      val hasher = new KmeansImageHasher(params.k)
      val hash = hasher.hash(preprocessedImage)

      val hashTime = System.currentTimeMillis()
      //println(s"${imageParam.imageName} | hash image: ${(hashTime - preprocess) / 1000.0}")

      hash
    }

    hashes.zip(params.imageParams)foreach {
      case (hash: Image, imageParams: ImageInputParams) => hash.save(s"${params.outputPath}/hashed-${imageParams.imageName}", imageParams.imageType)
    }

    val imageNames = params.imageParams.map(p => p.imageName)
    val hashesAndNames = hashes.zip(imageNames)

    hashesAndNames.combinations(2).toList.foreach {
      case Array((hash1: Image, name1: String), (hash2: Image, name2: String)) => {
        val distance = hash1.pixels.zip(hash2.pixels).map {
          case (pixel1: Pixel, pixel2: Pixel) => sqrt(pow(pixel1.red - pixel2.red, 2) + pow(pixel1.green - pixel2.green, 2) + pow(pixel1.blue - pixel2.blue, 2))
        }.sum / params.k
        println(distance)

        // brightness...
        val brightness1 = hash1.pixels.map(brightness)
        val brightness2 = hash2.pixels.map(brightness)
        val brightnessDiffs: Array[Double] = brightness1.zip(brightness2).map(pair => pair._1 - pair._2)
        println(brightnessDiffs.toList)

        if (hash1 == hash2) {
          println(s"$name1 equals $name2")
        } else {
          println(s"$name1 doesn't equal $name2")
        }
      }
    }
  }

  /**
   * From some random-ass blog from the Aughts http://alienryderflex.com/hsp.html
   * @param p
   * @return
   */
  def brightness(p: Pixel): Double = {
    sqrt(p.red * p.red * .299 + p.green * p.green * .587 + p.blue * p.blue * .114)
  }
}
