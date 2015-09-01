import scala.math.{pow, sqrt}

/**
 * Takes an image and an image hash and "tones" the image with the hash -- that is, reduces the original image's color
 * palette to just those present in the hash.
 *
 * First arg: path to the original image.
 * Second arg: path to the hash.
 */
object ToningApp {

  def main(args: Array[String]): Unit = {
    val params = new ToningParams(args)
    val image = Image.create(params.fullImagePath)
    val hash = Image.create(params.hashPath)
    val tonedImage = tone(image, hash)
    tonedImage.save(s"${params.outputPath}/toned-${params.imageName}", params.imageType)
  }

  private def tone(source: Image, hash: Image): Image = {
    val tonedPixels = source.pixels.map { sourcePixel =>
      val distancesAndPixels = hash.pixels.map { hashPixel =>
        val squareDistance = hashPixel.getPoint.zip(sourcePixel.getPoint).map( pair => pow(pair._1 - pair._2, 2)).sum
        val distance = sqrt(squareDistance)
        (distance, hashPixel)
      }
      //distancesAndPixels.map(println)
      val closestPixel: Pixel = distancesAndPixels.minBy(pair => pair._1)._2
      new Pixel(closestPixel.red, closestPixel.green, closestPixel.blue, sourcePixel.x, sourcePixel.y)
    }
    Image.create(tonedPixels, source.width, source.height)
  }
}
