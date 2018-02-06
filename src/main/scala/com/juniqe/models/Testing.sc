import java.io.File
import java.nio.file.{Files, Path, StandardCopyOption}

val regex = """(\d)-(\d)-(\d+)(\w)-(\d+)x(\d+)\.jpg""".r

"2-4-100L-20x40.jpg" match {
  case regex(designerId, designId, productType, ori, length, breadth) =>
    s"$designerId - $designId - $productType - $ori - $length - $breadth"
  case _ => s"no match found"
}

val file = new File("/Users/riuser/rioffice/code/testFile.txt")

val path = Files.move(file.toPath, new File("/Users/riuser/testFile.txt").toPath, StandardCopyOption.REPLACE_EXISTING)

path.getFileName.toString