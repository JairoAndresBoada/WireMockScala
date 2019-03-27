package com.DTO

import spray.json.DefaultJsonProtocol

case class DetalleResponse(
  nombre: String,
  apellido: String,
  cedula: String)

case class DetalleResponseFailed(
  status: String,
  mensaje: String)

object DetalleResponse extends DefaultJsonProtocol {
  implicit val formatMensajeOk = jsonFormat3(DetalleResponse.apply)
}

object DetalleResponseFailed extends DefaultJsonProtocol {
  implicit val formatMensajeFailed = jsonFormat2(DetalleResponseFailed.apply)
}

