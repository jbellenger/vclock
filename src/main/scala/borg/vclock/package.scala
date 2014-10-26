package borg

import akka.actor.ActorPath

package object vclock {
  type ActorVectorClock = VectorClock[ActorPath]
}
