package com.higherkindpud.rettuce.domain.entity

import java.time.Instant

// 確定済みの売り上げ in MySQL
case class Sale(
    vegetableId: VegetableId,
    quantity: Int, // 個数
    amount: Int,   // 売り上げ金額
    date: Instant
)
