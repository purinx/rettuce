package com.higherkindpud.rettuce.infra.redis.free

import com.higherkindpud.rettuce.infra.redis.common.Cache

import cats.~>
import cats.effect.{Async, ContextShift, ExitCase}
import cats.free.{Free => FF} // alias because some algebras have an op called FF
import scala.concurrent.ExecutionContext

import redis.clients.jedis.Jedis

object Nyan { module =>

  sealed trait RedisOp[A] {
    def visit[F[_]](v: RedisOp.Visitor[F]): F[A]
  }

  type RedisIO[A] = FF[RedisOp, A]

  object RedisOp {

    trait Visitor[F[_]] extends (RedisOp ~> F) {
      final def apply[A](fa: RedisOp[A]): F[A] = fa.visit(this)

      // Common
      def raw[A](f: Jedis => A): F[A]
      // def embed[A](e: Embedded[A]): F[A]
      def delay[A](a: () => A): F[A]
      def handleErrorWith[A](fa: RedisIO[A], f: Throwable => RedisIO[A]): F[A]
      def raiseError[A](e: Throwable): F[A]
      def async[A](k: (Either[Throwable, A] => Unit) => Unit): F[A]
      def asyncF[A](k: (Either[Throwable, A] => Unit) => RedisIO[Unit]): F[A]
      def bracketCase[A, B](acquire: RedisIO[A])(use: A => RedisIO[B])(
          release: (A, ExitCase[Throwable]) => RedisIO[Unit]
      ): F[B]
      def shift: F[Unit]
      def evalOn[A](ec: ExecutionContext)(fa: RedisIO[A]): F[A]

      // Cache
      def get[K, V](key: K): F[Option[V]]
      def mget[K, V](keys: collection.immutable.Set[K]): F[Map[K, V]]
      def getAll[K, V](): F[Map[K, V]]
      def set[K, V](key: K, value: V): F[Unit]
      def mset[K, V](keysvalues: Map[K, V]): F[Unit]
      def delete[K, V](key: K): F[Unit]
      def flush[K, V](): F[Unit]
    }

    // Common operations for all algebras.
    final case class Raw[A](f: Jedis => A) extends RedisOp[A] {
      def visit[F[_]](v: Visitor[F]) = v.raw(f)
    }
    // final case class Embed[A](e: Embedded[A]) extends RedisOp[A] {
    //   def visit[F[_]](v: Visitor[F]) = v.embed(e)
    // }
    final case class Delay[A](a: () => A) extends RedisOp[A] {
      def visit[F[_]](v: Visitor[F]) = v.delay(a)
    }
    final case class HandleErrorWith[A](fa: RedisIO[A], f: Throwable => RedisIO[A]) extends RedisOp[A] {
      def visit[F[_]](v: Visitor[F]) = v.handleErrorWith(fa, f)
    }
    final case class RaiseError[A](e: Throwable) extends RedisOp[A] {
      def visit[F[_]](v: Visitor[F]) = v.raiseError(e)
    }
    final case class Async1[A](k: (Either[Throwable, A] => Unit) => Unit) extends RedisOp[A] {
      def visit[F[_]](v: Visitor[F]) = v.async(k)
    }
    final case class AsyncF[A](k: (Either[Throwable, A] => Unit) => RedisIO[Unit]) extends RedisOp[A] {
      def visit[F[_]](v: Visitor[F]) = v.asyncF(k)
    }
    final case class BracketCase[A, B](
        acquire: RedisIO[A],
        use: A => RedisIO[B],
        release: (A, ExitCase[Throwable]) => RedisIO[Unit]
    ) extends RedisOp[B] {
      def visit[F[_]](v: Visitor[F]) = v.bracketCase(acquire)(use)(release)
    }
    final case object Shift extends RedisOp[Unit] {
      def visit[F[_]](v: Visitor[F]) = v.shift
    }
    final case class EvalOn[A](ec: ExecutionContext, fa: RedisIO[A]) extends RedisOp[A] {
      def visit[F[_]](v: Visitor[F]) = v.evalOn(ec)(fa)
    }

    // Connection-specific operations.

    final case class Get[K, V](key: K) extends RedisOp[Option[V]] {
      def visit[F[_]](v: Visitor[F]) = v.get(key)
    }
    final case class Mget[K, V](keys: collection.immutable.Set[K]) extends RedisOp[Map[K, V]] {
      def visit[F[_]](v: Visitor[F]) = v.mget(keys)
    }
    final case class GetAll[K, V]() extends RedisOp[Map[K, V]] {
      def visit[F[_]](v: Visitor[F]) = v.getAll()
    }
    final case class Set[K, V](key: K, value: V) extends RedisOp[Unit] {
      def visit[F[_]](v: Visitor[F]) = v.set(key, value)
    }
    final case class Mset[K, V](keysvalues: Map[K, V]) extends RedisOp[Unit] {
      def visit[F[_]](v: Visitor[F]) = v.mset(keysvalues)
    }
    final case class Delete[K, V](key: K) extends RedisOp[Unit] {
      def visit[F[_]](v: Visitor[F]) = v.delete(key)
    }
    final case class Flush[K, V]() extends RedisOp[Unit] {
      def visit[F[_]](v: Visitor[F]) = v.flush()
    }

  }

  import RedisOp._

  val unit: RedisIO[Unit]               = FF.pure[RedisOp, Unit](())
  def pure[A](a: A): RedisIO[A]         = FF.pure[RedisOp, A](a)
  def raw[A](f: Jedis => A): RedisIO[A] = FF.liftF(Raw(f))
  // def embed[F[_], J, A](j: J, fa: FF[F, A])(implicit ev: Embeddable[F, J]): FF[RedisOp, A] = FF.liftF(Embed(ev.embed(j, fa)))
  def delay[A](a: => A): RedisIO[A] = FF.liftF(Delay(() => a))
  def handleErrorWith[A](fa: RedisIO[A], f: Throwable => RedisIO[A]): RedisIO[A] =
    FF.liftF[RedisOp, A](HandleErrorWith(fa, f))
  def raiseError[A](err: Throwable): RedisIO[A]                                 = FF.liftF[RedisOp, A](RaiseError(err))
  def async[A](k: (Either[Throwable, A] => Unit) => Unit): RedisIO[A]           = FF.liftF[RedisOp, A](Async1(k))
  def asyncF[A](k: (Either[Throwable, A] => Unit) => RedisIO[Unit]): RedisIO[A] = FF.liftF[RedisOp, A](AsyncF(k))
  def bracketCase[A, B](acquire: RedisIO[A])(use: A => RedisIO[B])(
      release: (A, ExitCase[Throwable]) => RedisIO[Unit]
  ): RedisIO[B]                                       = FF.liftF[RedisOp, B](BracketCase(acquire, use, release))
  val shift: RedisIO[Unit]                            = FF.liftF[RedisOp, Unit](Shift)
  def evalOn[A](ec: ExecutionContext)(fa: RedisIO[A]) = FF.liftF[RedisOp, A](EvalOn(ec, fa))

  // RedisIO is an Async
  implicit val AsyncRedisIO: Async[RedisIO] =
    new Async[RedisIO] {
      val asyncM = FF.catsFreeMonadForFree[RedisOp]
      def bracketCase[A, B](acquire: RedisIO[A])(use: A => RedisIO[B])(
          release: (A, ExitCase[Throwable]) => RedisIO[Unit]
      ): RedisIO[B]                                                                  = module.bracketCase(acquire)(use)(release)
      def pure[A](x: A): RedisIO[A]                                                  = asyncM.pure(x)
      def handleErrorWith[A](fa: RedisIO[A])(f: Throwable => RedisIO[A]): RedisIO[A] = module.handleErrorWith(fa, f)
      def raiseError[A](e: Throwable): RedisIO[A]                                    = module.raiseError(e)
      def async[A](k: (Either[Throwable, A] => Unit) => Unit): RedisIO[A]            = module.async(k)
      def asyncF[A](k: (Either[Throwable, A] => Unit) => RedisIO[Unit]): RedisIO[A]  = module.asyncF(k)
      def flatMap[A, B](fa: RedisIO[A])(f: A => RedisIO[B]): RedisIO[B]              = asyncM.flatMap(fa)(f)
      def tailRecM[A, B](a: A)(f: A => RedisIO[Either[A, B]]): RedisIO[B]            = asyncM.tailRecM(a)(f)
      def suspend[A](thunk: => RedisIO[A]): RedisIO[A]                               = asyncM.flatten(module.delay(thunk))
    }

  // RedisIO is a ContextShift
  implicit val ContextShiftRedisIO: ContextShift[RedisIO] =
    new ContextShift[RedisIO] {
      def shift: RedisIO[Unit]                            = module.shift
      def evalOn[A](ec: ExecutionContext)(fa: RedisIO[A]) = module.evalOn(ec)(fa)
    }

}
