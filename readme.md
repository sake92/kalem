# Scalafix rules for Wither

Install scalafix as usual.

Add this to your `build.sbt`:
```scala
libraryDependencies += "ba.sake" %% "kalem-core" % "0.0.1"

ThisBuild / scalafixDependencies += "ba.sake" %% "kalem-rules" % "0.0.1"
```

and this to your `.scalafix.conf`:
```
rules = [
  Wither
]
```

Run scalafix to turn this:
```scala
import ba.sake.kalem.Wither

@Wither
case class MyClass(x: Int, y: String)
```
into this:
```scala
@Wither
case class MyClass(x: Int, y: String){
  def withX(x: Int): MyClass = new MyClass(x = x, y = y)
  def withY(y: String): MyClass = new MyClass(x = x, y = y)
}
```

Why?
- more readable than named args
- autocomplete is nicer
- additional goodies for Options and Lists

### Develop
To develop rule:
```
sbt ~tests/test
# edit rules/src/main/scala/fix/Wither.scala
```
