package fix

import ba.sake.kalem.Wither

@Wither
case class EnrichOpt(opt: Option[String]){
  def withOpt(opt: Option[String]): EnrichOpt = new EnrichOpt(opt = opt)
  def withOpt(opt: String): EnrichOpt = new EnrichOpt(opt = Option(opt))
}

@Wither
case class EnrichSeq(seq: List[String]){
  def withSeq(seq: List[String]): EnrichSeq = new EnrichSeq(seq = seq)
  def withSeq(seq: String*): EnrichSeq = new EnrichSeq(seq = seq.toList)
}