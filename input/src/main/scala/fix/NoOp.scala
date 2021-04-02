/*
rule = Wither
*/
package fix

import ba.sake.kalem.Wither

@Wither
class NoOp1

@Wither
class NoOp2 {}

@Wither
class NoOp3() {}

@Wither
class NoOp4()(implicit s: String)

@Wither
case class NoUpdate(x: Int) {
  def withX(x: Int): NoUpdate = ??? // should skip
}

@Wither
class PrivateY(y: Int) // not public

@Wither
case class PrivateZ(private val z: Int) // private