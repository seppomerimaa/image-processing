import java.awt.Color
import java.awt.image.BufferedImage
import java.io.{IOException, File}
import javax.imageio.ImageIO

import org.apache.commons.math3.ml.clustering.Clusterable

/**
 * Some image processing wraper classes and utilities.
 */

case class Pixel(red: Int, green: Int, blue: Int, x: Int, y: Int) extends Clusterable {
  override def getPoint = {
    Array(red.toDouble, green.toDouble, blue.toDouble)
  }
}

trait Image {
  def pixels: Array[Pixel]
  def save(outputPath: String, imageType: String): Unit
  override def equals(a: Any): Boolean = {
    a match {
      case i: Image => pixels.toList == i.pixels.toList
      case _ => false
    }
  }
}

object Image {

  /**
   * Create an image from the file found at the given path.
   * @param inputPath The file to parse into an Image
   * @return An Image
   */
  def create(inputPath: String): Image = {
    val buff = ImageIO.read(new File(inputPath))
    //val raster = buff.getRaster
//    match buff.getC

    val pixels = for (
      x <- buff.getMinX to buff.getWidth - 1;
      y <- buff.getMinY to buff.getHeight - 1
    ) yield {
      val color = new Color(buff.getRGB(x, y))
      new Pixel(color.getRed, color.getGreen, color.getBlue, x, y)
    }
    new ConcreteImage(pixels.toArray, buff.getWidth, buff.getHeight)
  }

  /**
   * Create an image from a array of raw pixels.
   * @param rawPixels An array where each slot represents a pixel, and the array within that slot is of length 3, holding
   *                  the R, G, and B values for that pixel in that order.
   * @return An Image
   */
  def create(rawPixels: Array[Array[Double]]): Image = {
    val pixels =  rawPixels.zipWithIndex.map {
      case (pixel: Array[Double], index: Int) => new Pixel(pixel(0).toInt, pixel(1).toInt, pixel(2).toInt, index, 0)
    }
    new ConcreteImage(pixels, pixels.length, 1)
  }

  private class ConcreteImage(pixelArr: Array[Pixel], width: Int, height: Int) extends Image {
    override def pixels = {
      pixelArr
    }
    override def save(outputPath: String, imageType: String): Unit = {
      val bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

      pixelArr.map(p => bufferedImage.setRGB(p.x, p.y, new Color(p.red, p.green, p.blue).getRGB))

      val output = new File(outputPath)
      try {
        ImageIO.write(bufferedImage, imageType, output)
      } catch {
        case e: IOException=> println("oops")
      }
    }
  }
}

