package com.higherkindpud.rettuce.domain.entity

import io.circe.{Encoder, Decoder, HCursor, Json}
import io.circe.parser._
import io.circe.generic.auto._

case class Vegetable(name: String, price: Int)
