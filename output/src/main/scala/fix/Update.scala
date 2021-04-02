package fix

import ba.sake.kalem.Wither

@Wither
case class Update1(x: Int, y: Long) {
  def withX(x: Int): Update1 = ??? // should skip
def withY(y: Long): Update1 = new Update1(x = x, y = y)
}