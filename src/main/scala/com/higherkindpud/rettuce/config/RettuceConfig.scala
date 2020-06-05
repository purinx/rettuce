package com.higherkindpud.rettuce.config

case class RettuceConfig(
    redis: Redis
)

case class Redis(
    host: String,
    port: Int
)
