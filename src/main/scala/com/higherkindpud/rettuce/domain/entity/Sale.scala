package com.higherkindpud.rettuce.domain.entity

import java.time.Instant

case class Sale(
    id: Int,
    name: String,
    price: Int,
    quantity: Int,
    amount: Int,
    date: Instant
)
