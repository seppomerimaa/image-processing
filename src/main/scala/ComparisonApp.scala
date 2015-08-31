/**
 * Compare some images by their hashes.
 */
object ComparisonApp {
  def main(args: Array[String]): Unit = {
    val params = new ComparisonParams(args)
    val hashes = params.imageParams.map { imageParam =>
      val image = Image.create(imageParam.fullImagePath)
      val preprocessedImage = Preprocessor.processImage(image)
      val hasher = new KmeansImageHasher(params.k)
      hasher.hash(preprocessedImage)
    }

    hashes.zip(params.imageParams)foreach {
      case (hash: Image, imageParams: ImageInputParams) => hash.save(s"${params.outputPath}/hashed-${imageParams.imageName}", imageParams.imageType)
    }

    val imageNames = params.imageParams.map(p => p.imageName)
    val hashesAndNames = hashes.zip(imageNames)

    hashesAndNames.combinations(2).toList.foreach {
      case Array((hash1: Image, name1: String), (hash2: Image, name2: String)) => {
        if (hash1 == hash2) {
          println(s"$name1 equals $name2")
        } else {
          println(s"$name1 doesn't equal $name2")
        }
      }
    }
  }
}

object TestApp {
  def main(args: Array[String]): Unit = {
    val p1 = new Pixel(1,2,3, 1, 1)
    val p2 = new Pixel(1,2,3, 1, 1)
    println(p1 == p2)

    val i1 = Image.create("src/main/resources/baby-goat.jpg")
    val i2 = Image.create("src/main/resources/baby-goat.jpg")
    println(i1 == i2)
  }
}
