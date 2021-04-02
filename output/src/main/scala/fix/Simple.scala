package fix

import ba.sake.kalem.Wither

@Wither
case class Simple1(x: Int, y: String){
  def withX(x: Int): Simple1 = new Simple1(x = x, y = y)
  def withY(y: String): Simple1 = new Simple1(x = x, y = y)
}


trait A
trait B

@Wither
final case class BitMoreComplex(
  xyz: Long = 123L,
  abc: String = "abc"
) extends A with B{
  def withXyz(xyz: Long): BitMoreComplex = new BitMoreComplex(xyz = xyz, abc = abc)
  def withAbc(abc: String): BitMoreComplex = new BitMoreComplex(xyz = xyz, abc = abc)
}
