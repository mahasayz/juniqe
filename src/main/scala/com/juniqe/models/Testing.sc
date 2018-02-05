val regex = """(\d)-(\d)-(\d+)(\w)-(\d+)x(\d+)\.jpg""".r

"2-4-100L-20x40.jpg" match {
  case regex(designerId, designId, productType, ori, length, breadth) =>
    s"$designerId - $designId - $productType - $ori - $length - $breadth"
  case _ => s"no match found"
}