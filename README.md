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

Installation
------------

    sbt compile
    sbt package
    sbt publishLocal # if you want to use it in other projects locally

Quick Reference
---------------

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

To build test suites simply put a sequence of `test` calls into methods.
Parallel collections of lazy values (`.par.map(_())`) may be useful to run tests
in parallel

Implementation
--------------

Implicit class `Assertions` decorates values of any type with the above
mentioned methods. The companion object `Test` provides two exception types that
are triggere internally, `Bug` and `Abort`, where the first signals a failed test
and the second signals an explicit cancellation via `abort(msg)`.
