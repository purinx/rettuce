package com.higherkindpud.rettuce.infra.redis.common

trait Cache[K, V] {
  self =>

  def get(key: K): Option[V]
  def mget(keys: Set[K]): Map[K, V]
  def set(key: K, value: V): Unit
  def mset(keysvalues: Map[K, V]): Unit
  def delete(key: K): Unit
  def flush(): Unit

  final def mapValue[VV](v2vv: V => VV, vv2v: VV => V): Cache[K, VV] =
    new Cache[K, VV] {
      override def get(key: K): Option[VV] = self.get(key).map(v2vv)
      override def mget(keys: Set[K]): Map[K, VV] =
        self.mget(keys).map {
          case (key, value) => key -> v2vv(value)
        }
      override def mset(keysvalues: Map[K, VV]): Unit =
        self.mset(keysvalues.map {
          case (key, value) => key -> vv2v(value)
        })
      override def set(key: K, value: VV): Unit = self.set(key, vv2v(value))
      override def delete(key: K): Unit         = self.delete(key)
      override def flush(): Unit                = self.flush()
    }

  final def mapKey[KK](k2kk: K => KK, kk2k: KK => K): Cache[KK, V] =
    new Cache[KK, V] {
      override def get(key: KK): Option[V] = self.get(kk2k(key))
      override def mget(keys: Set[KK]): Map[KK, V] =
        self.mget(keys.map(kk2k)).map {
          case (key, value) => k2kk(key) -> value
        }
      override def mset(keysvalues: Map[KK, V]): Unit =
        self.mset(keysvalues.map {
          case (key, value) => kk2k(key) -> value
        })
      override def set(key: KK, value: V): Unit = self.set(kk2k(key), value)
      override def delete(key: KK): Unit        = self.delete(kk2k(key))
      override def flush(): Unit                = self.flush()
    }

}
