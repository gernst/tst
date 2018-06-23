// TST Unit Test Library
// (c) 2016 Gidon Ernst <gidonernst@gmail.com>
// This code is licensed under MIT license (see LICENSE for details)

package tst

import scala.reflect.ClassTag

object Test {
  def time[A](f: => A): (A, Long) = {
    val start = System.currentTimeMillis
    val r = f
    val end = System.currentTimeMillis
    (r, end - start)
  }

  def progress(m: String, i: Int, n: Int, p: Int) = {
    val q = i * 100 / n
    if (p != q) println(m + ": " + q + "%")
    q
  }

  case class Bug(override val toString: String) extends Exception
  case class Abort(override val toString: String) extends Exception
}

trait Test {
  import Test._

  implicit class Assertions[A](a: => A) {
    def expect[B <: A](b: B) {
      if (a != b)
        throw Bug("actual: " + a + ", expected: " + b)
    }

    def inspect(p: Any => Boolean) {
      try {
        if (!p(a))
          throw Bug("actual: " + a + " is unexpected (malformed)")
      } catch {
        case _: MatchError =>
          throw Bug("actual: " + a + " is unexpected (no match)")
      }
    }
    
    def fails = throws[Throwable]

    def throws[E <: Throwable](implicit ev: ClassTag[E]) {
      val r = try {
        a
      } catch {
        case _: E => return
        case r: Throwable => r
      }
      throw Bug("actual: " + r + ", expected: " + ev)
    }
  }

  def abort(reason: String) = throw Abort(reason)

  def test(name: String)(f: => Any) = {
    try {
      val (_, t) = time(f)
      System.out.println("'" + name + "' passed in " + t + "ms")
    } catch {
      case Bug(message) =>
        System.err.println("'" + name + "' failed: " + message)
      case Abort(reason) =>
        System.err.println("'" + name + "' was aborted: " + reason)
      case e: Throwable =>
        System.err.println("'" + name + "' failed: " + e + " (" + e.getClass.getSimpleName + ")")
      // throw e
    }
  }
}
