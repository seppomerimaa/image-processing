/**
 * Handle any pre-processing before we do k-means here.
 */
object Preprocessor {
  // For now this is a no-op
  def processImage(image: Image): Image = {
    image
  }
}
