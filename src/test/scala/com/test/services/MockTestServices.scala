package com.test.services

import akka.http.scaladsl.model.HttpRequest
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig

import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, ExecutionContext, Future }
import scala.io.Source
import org.scalatest._
import spray.json._
import com.DTO._
import Matchers._
import com.fasterxml.jackson.annotation.JacksonInject

class Mytest @JacksonInject() extends FlatSpec with BeforeAndAfterEach {

  private val puerto = 7879
  private val url = "localhost"
  implicit val ec = ExecutionContext.global

  val wireMockServer = new WireMockServer(wireMockConfig().port(puerto))
  configureFor("localhost", 7879)

  def loadResourceFile(rutaArchivo: String): String = {
    Source.fromURL(getClass.getResource(s"$rutaArchivo"))("UTF-8").mkString
  }

  val requestFailed = Future(loadResourceFile("/com/test/services/Objetos.json"))

  def pruebaMockWireFailed(): Unit = {
    val path = s"/users"
    stubFor(WireMock.get(urlPathEqualTo(path))
      .withHeader("Accept", containing("json"))
      .willReturn(aResponse()
        .withStatus(500)
        .withHeader("Content-Type", "application/json")
        .withBody(loadResourceFile(Await.result(requestFailed, Duration.Inf)))))
  }

  "Prueba ejemplo de WireMock Status 500" should "say hello" in {
    println(s"el mensaje que esta llegado es $requestFailed")
    val request = HttpRequest(uri = "/users")
    val response = requestFailed
    val result: String = Await.result(response, Duration.Inf)
    val mensajeEnJson: JsValue = result.parseJson
    val mensajeDetalle: DetalleResponse = mensajeEnJson.convertTo[DetalleResponse](DetalleResponse.detalleResponse)
    val mensajeFinalizado: DetalleFailed = mensajeDetalle.detalleFailed

    mensajeFinalizado match {
      case d: DetalleFailed =>
        d.status shouldBe "500"
        d.mensaje shouldBe "error"

      case _ => fail()
    }
  }

  val requestResponseOk = Future(loadResourceFile("/com/test/services/Objetos.json"))

  def pruebaMockWireOk(): Unit = {
    val path = s"/users"
    stubFor(WireMock.get(urlPathEqualTo(path))
      .withHeader("Accept", containing("json"))
      .willReturn(aResponse()
        .withStatus(200)
        .withHeader("Content-Type", "application/json")
        .withBody(loadResourceFile(Await.result(requestResponseOk, Duration.Inf)))))
  }

  "Prueba ejemplo de WireMock Status 200" should "say hello" in {
    println(s"el mensaje que esta llegado es $requestResponseOk")
    val request = HttpRequest(uri = "/users")
    val response = requestResponseOk
    val result: String = Await.result(response, Duration.Inf)
    val mensajeEnJson: JsValue = result.parseJson
    val mensajeDetalle: DetalleResponse = mensajeEnJson.convertTo[DetalleResponse](DetalleResponse.detalleResponse)
    val mensajeFinalizado: DetalleOk = mensajeDetalle.detalleOk

    mensajeFinalizado match {
      case d: DetalleOk =>
        d.nombre shouldBe "Jairo"
        d.apellido shouldBe "Boada"
        d.cedula shouldBe "123456"

      case _ => fail()
    }
  }
}
