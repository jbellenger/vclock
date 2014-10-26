package borg.vclock

import akka.actor.{ActorRef, Props, Actor, ActorSystem}
import akka.testkit.{TestProbe, TestActorRef, TestKit}
import org.scalatest.{FlatSpecLike, Matchers}
import scala.concurrent.duration._

class VectorActorTest extends TestKit(ActorSystem("test")) with FlatSpecLike with Matchers {
  case class Observe(ref: ActorRef)
  case class VectorSend(ref: ActorRef, payload: Any)

  class TestActor extends Actor with VectorActor {
    override def receive = super.receive orElse {
      case Observe(ref) => observe(Unit); ref ! clock
      case VectorSend(ref, payload) => vectorSend(ref, payload)
    }
  }

  "A VectorActor" should "increment on observe" in {
    val ref = TestActorRef[TestActor](Props(new TestActor))
    val probe = TestProbe()
    ref ! Observe(probe.ref)
    probe.receiveOne(1.second)
    ref.underlyingActor.clock.map shouldBe Map(ref.path -> 1)
  }

  it should "include clock in vector send" in {
    val ref = TestActorRef[TestActor](Props(new TestActor))
    val probe = TestProbe()
    ref ! VectorSend(probe.ref, "foo")
    val VectorActor.VectorWrapper(_, clock) = probe.receiveOne(1.second)
    clock.map shouldBe Map(ref.path -> 0)
    ref.underlyingActor.clock.map shouldBe Map(ref.path -> 0)
  }
}
