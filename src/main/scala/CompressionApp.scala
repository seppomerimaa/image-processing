/**
 *  First arg should be the path to the image that you want.
 *  Second should be the number of clusters to use when hashing the image.
 *  Third should be the output directory
 */
object CompressionApp {
  def main(args: Array[String]): Unit = {
    val params = new ImageProcessorParams(args)


    val image = Image.create(params.fullImagePath)
    val preprocessedImage = Preprocessor.processImage(image)
    val hasher = new KmeansImageHasher(params.k)
    val hashedImage: Image = hasher.hash(preprocessedImage)
    hashedImage.save(s"${params.outputPath}/hashed-${params.imageName}", params.imageType)
  }
}
