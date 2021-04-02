# Scalafix rules for Wither

---
## @Wither

Run scalafix to turn this:
```scala
import ba.sake.kalem.Wither

@Wither
case class MyClass(x: Int, opt: Option[String])
```
into this:
```scala
@Wither
case class MyClass(x: Int, opt: Option[String], lst: List[String]){
  def withX(x: Int): MyClass = new MyClass(x = x, opt = opt, lst = lst)

  def withOpt(opt: Option[String]): MyClass = MyClass(x = x, opt = opt, lst = lst)
  def withOpt(opt: String): MyClass = new MyClass(x = x, opt = Option(opt), lst = lst)

  def withLst(lst: List[String]): MyClass = new MyClass(x = x, opt = opt, lst = lst)
  def withLst(lst: String*): MyClass = new MyClass(x = x, opt = opt, lst = lst.toList)
}
```

Why ?
- more readable than named args
- autocomplete is nicer
- additional goodies for Options and Lists

How ?  

Install scalafix as usual.  
Add this to your `build.sbt`:
```scala
libraryDependencies += "ba.sake" %% "kalem-core" % "0.0.1"

ThisBuild / scalafixDependencies += "ba.sake" %% "kalem-rules" % "0.0.1"
```

and this to `.scalafix.conf`:
```
rules = [
  Wither
]
```

---
### Develop
To develop rule:
```
sbt ~tests/test
# edit rules/src/main/scala/fix/Wither.scala
```
