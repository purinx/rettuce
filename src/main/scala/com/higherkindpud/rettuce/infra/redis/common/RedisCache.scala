package infra.redis.common

import redis.clients.jedis.{Jedis, JedisPool}
import scala.jdk.CollectionConverters._

abstract class RedisCache(pool: JedisPool) extends Cache[String, String] {
  final protected def withJedis[A](f: Jedis => A) = {
    val jedis = pool.getResource
    try {
      f(jedis)
    } finally {
      jedis.close()
    }
  }
}

class DefaultRedisCache(pool: JedisPool) extends RedisCache(pool) {

  override def get(key: String): Option[String] = {
    Option(withJedis(_.get(key)))
  }
  override def mget(keys: Set[String]): Map[String, String] = {
    val keyList: List[String]        = keys.toList
    val valuesWithNull: List[String] = withJedis(_.mget(keyList: _*)).asScala.toList
    (keyList zip valuesWithNull).collect {
      case (key, value) if value != null => key -> value
    }.toMap
  }
  override def set(key: String, value: String): Unit = {
    withJedis(_.set(key, value))
  }
  override def mset(keysvalues: Map[String, String]): Unit = {
    withJedis(_.mset(keysvalues.toList.flatMap {
      case (key, value) => key :: value :: Nil
    }: _*))
  }
  override def delete(key: String): Unit = {
    withJedis(_.del(key))
  }
  override def flush(): Unit = {
    withJedis(_.flushDB())
  }

  def withHash(h: String): Cache[String, String] = new RedisCacheWithHash(pool, h)

}

class RedisCacheWithHash(pool: JedisPool, hash: String) extends RedisCache(pool) {

  override def get(key: String): Option[String] = {
    Option(withJedis(_.hget(hash, key)))
  }
  override def mget(keys: Set[String]): Map[String, String] = {
    val keyList: List[String]        = keys.toList
    val valuesWithNull: List[String] = withJedis(_.hmget(hash, keyList: _*)).asScala.toList
    (keyList zip valuesWithNull).collect {
      case (key, value) if value != null => key -> value
    }.toMap
  }
  override def set(key: String, value: String): Unit = {
    withJedis(_.hset(hash, key, value))
  }
  override def mset(keysvalues: Map[String, String]): Unit = {
    withJedis(_.hmset(hash, keysvalues.asJava))
  }
  override def delete(key: String): Unit = {
    withJedis(_.hdel(hash, key))
  }
  override def flush(): Unit = {
    withJedis(_.del(hash))
  }

}
