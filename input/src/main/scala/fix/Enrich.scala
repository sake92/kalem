/*
rule = Wither
*/
package fix

import ba.sake.kalem.Wither

@Wither
case class EnrichOpt(opt: Option[String])

@Wither
case class EnrichSeq(seq: List[String])