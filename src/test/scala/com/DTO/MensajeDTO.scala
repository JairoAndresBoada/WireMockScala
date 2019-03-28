package com.DTO

import spray.json.DefaultJsonProtocol

case class DetalleResponse(
  detalleOk: DetalleOk,
  detalleFailed: DetalleFailed)

case class DetalleOk(
  nombre: String,
  apellido: String,
  cedula: String)

case class DetalleFailed(
  status: String,
  mensaje: String)

object DetalleResponse extends DefaultJsonProtocol {
  implicit val detalleOk = jsonFormat3(DetalleOk.apply)
  implicit val detalleFailed = jsonFormat2(DetalleFailed.apply)
  implicit val detalleResponse = jsonFormat2(DetalleResponse.apply)
}

