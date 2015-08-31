/**
 * Reads an image and copies it.
 * First arg should be the path to the image. Copied image will be output to src/main/resources.
 */
object CopyApp {
  def main(args: Array[String]): Unit = {
    val params = new ImageProcessorParams(args)
    val bebeGoat = Image.create(params.fullImagePath)
    bebeGoat.save(s"${params.outputPath}/copied-${params.imageName}", params.imageType)
  }
}
