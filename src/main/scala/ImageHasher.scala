import collection.JavaConversions._
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer

/**
 * An ImageHasher takes an image and hashes it into a (presumably) much smaller image.
 */
trait ImageHasher {
  def hash(image: Image): Image
}

/**
 * An ImageHasher that uses k-means clustering with each pixel as a point and the R, G, and B values of pixel
 * as the spatial dimensions.
 * @param k The number of clusters to use.
 */
class KmeansImageHasher(k: Int) extends ImageHasher {
  override def hash(image: Image) = {
    val clusterer = new KMeansPlusPlusClusterer[Pixel](k, 1000)
    val pixels: java.util.Collection[Pixel] = image.pixels.toSeq
    val centroids: Array[Array[Double]] = clusterer.cluster(pixels).map(cc => cc.getCenter.getPoint).toArray
    // Need to sort or the same image will probably end up with hashed pixels in different orders each
    // time it goes through the clusterer
    val sortedCentroids = centroids.sortBy(centroid => (centroid(0), centroid(1), centroid(2)))
    Image.create(sortedCentroids)
  }
}
