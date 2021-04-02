/*
rule = Wither
*/
package fix

import ba.sake.kalem.Wither

@Wither
case class Simple1(x: Int, y: String)


trait A
trait B

@Wither
final case class BitMoreComplex(
  xyz: Long = 123L,
  abc: String = "abc"
) extends A with B
