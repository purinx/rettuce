package com.higherkindpud.rettuce.domain.entity

// 速報値 in redis
case class Report(
    id: Int,
    name: String,
    quantity: Int
)
