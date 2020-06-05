package com.higherkindpud.rettuce.config

import pureconfig._
import pureconfig.generic.auto._

case class Play(
    application: Application
)

case class Application(
    loader: String
)

case class Redis(
    host: String,
    port: Int
)

case class RettuceConfig(
    play: Play,
    redis: Redis
)
