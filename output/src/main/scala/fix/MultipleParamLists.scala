package fix

import ba.sake.kalem.Wither

@Wither
class MultipleParamLists1(val x: Int)(val y: String){
  def withX(x: Int): MultipleParamLists1 = new MultipleParamLists1(x = x)(y = y)
  def withY(y: String): MultipleParamLists1 = new MultipleParamLists1(x = x)(y = y)
}