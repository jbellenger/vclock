package borg.vclock

case class VectorClock[Key] private (selfKey: Key, map: Map[Key, Int]) {
  def incr: VectorClock[Key] = {
    val newVersion = map(selfKey) + 1
    copy(map = map + (selfKey -> newVersion))
  }

  def +(other: VectorClock[Key]): VectorClock[Key] = {
    val pairs = for {
      key <- map.keySet ++ other.map.keySet
      // List(Some(1), None).max   -> Some(1)
      value <- List(map.get(key), other.map.get(key)).max
    } yield key -> value
    copy(map = pairs.toMap)
  }
}

object VectorClock {
  def empty[Key](selfKey: Key): VectorClock[Key] = {
    VectorClock(selfKey, Map(selfKey -> 0))
  }
}
