package com.higherkindpud.rettuce.config

case class RettuceConfig(
    redis: RedisConfig,
    mysql: MySQLConfig
)

case class RedisConfig(
    host: String,
    port: Int
)

case class MySQLConfig(
    host: String,
    port: Int,
    dbname: String,
    username: String,
    password: String,
    threads: Int
)
