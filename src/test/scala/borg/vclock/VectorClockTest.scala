package borg.vclock

import org.scalatest._

class VectorClockTest extends FlatSpec with Matchers {
  "A VectorClock" should "initialize" in {
    VectorClock.empty("a").map shouldBe Map("a" -> 0)
  }

  it should "incr" in {
    VectorClock.empty("a").incr.map shouldBe Map("a" -> 1)
  }

  it should "+" in {
    val result = VectorClock.empty("a") + VectorClock.empty("b")
    result.map shouldBe Map("a" -> 0, "b" -> 0)
  }

  it should "+ when populated" in {
    val result =
      VectorClock.empty("a").incr +
      VectorClock.empty("b").incr.incr +
      VectorClock.empty("c").incr.incr.incr
    result.map shouldBe Map("a" -> 1, "b" -> 2, "c" -> 3)
  }
}
