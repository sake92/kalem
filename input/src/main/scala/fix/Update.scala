/*
rule = Wither
*/
package fix

import ba.sake.kalem.Wither

@Wither
case class Update1(x: Int, y: Long) {
  def withX(x: Int): Update1 = ??? // should skip
}