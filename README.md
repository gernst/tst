TST Library
===========

Minimal Unit Testing Library.

Author: Gidon Ernst <gidonernst@gmail.com>

Feedback is welcome! I'd appreciate to hear whether anyone found this library useful.

Motivation & Overview
---------------------

Inspired by [minunit](#).
[ScalaTest](http://scalatest.org) is too large for my taste.

The library consists of a single trait, `Test`, that is inherited by test suites.
You can then write

    test("simple examples) {
        x expect 2
        x inspect { _ % 2 == 0 }
        x throws[IOException]
    }

Quick Reference

- `x expect y` compares an actual value `x` to an expected one `y` and requires
  these to be equal
- `x inspect p` requires that the result of `x` satisfies predicate `p`
- `x throws[E]` requires that evaluating `x` throws an exception of the
  indicated type `E` (or one of its subtypes)
- `abort(msg)` aborts a running test with a given message
- Feel free to add your own checkers, throw a `Bug(msg)` exception on failure

You will get a summary for each test set up in this way,
indicating the first failed assertion resp. success of the entire test
plus some timing information.
Note that `expect` and `inspect` fail when `x` throws an exception.

Multiple tests and suites, including set up and tear down,
can be set up low ceremony simply. A test suite is just a plain function.
Of course, you can organized the suites into as many objects as you like.
If you want parallel test execution, consider using `map` on a parallel
collection of lazy values.

    object AllTests {
        def suite1() {
            setup code
            test("1") { ... }
            ...
            test("n") { ... }
            teardown code
        }

        def suite2() {
            ...
        }

        def main(args: Array[String]) {
            // sequential
            suite1()
            suite2()

            // parallel
            List(suite1, suite2).par.map(_())
        }
    }

Implementation
--------------

Implicit class `Assertions` decorates values of any type with the above
mentioned methods. The companion object `Test` provides two exception types that
are triggere internally, `Bug` and `Abort`, where the first signals a failed test
and the second signals an explicit cancellation via `abort(msg)`.
