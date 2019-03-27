package com.DTO

import spray.json.DefaultJsonProtocol

case class DetalleResponse(
  nombre: String,
  apellido: String,
  cedula: String
                          )

object DetalleResponse extends DefaultJsonProtocol {
  implicit val formatMensaje = jsonFormat3(DetalleResponse.apply)
}

