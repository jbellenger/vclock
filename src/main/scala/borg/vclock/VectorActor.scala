package borg.vclock

import akka.actor.{ActorRef, Actor}

trait VectorActor {
  myself: Actor =>
  
  import borg.vclock.VectorActor._
  
  private[vclock] var clock: ActorVectorClock = VectorClock.empty(self.path)

  override def receive: Receive = {
    case VectorWrapper(message, otherClock) =>
      this.clock = clock + otherClock
      self forward message
  }
  
  protected def vectorSend(to: ActorRef, msg: Any): Unit = {
    to ! VectorWrapper(msg, clock)
  }

  protected def observe[T](fn: => T): T = {
    val result = try {
      fn
    } finally {
      this.clock = clock.incr
    }
    result
  }
}

object VectorActor {
  case class VectorWrapper(m: Any, clock: ActorVectorClock)
}
