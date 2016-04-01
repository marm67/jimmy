package main.scala

import scala.util.parsing.combinator._
import scala.util.parsing.combinator.syntactical._

class Parser extends JavaTokenParsers {
  
  def script: Parser[List[Comando]] = comando ~ rep(comando) ^^
    { case a ~ b => a :: b }

  def comando: Parser[Comando] = define | set

  def define: Parser[Comando] = "DEFINE" ~> (servicio ~ parametros) ^^
    { case a ~ b => Define(a, b) }

  def set: Parser[Comando] = "SET" ~> ident ~ ("(" ~> ident <~ ")") ^^
    { case a ~ b => Set(a, b) }

  def servicio: Parser[Servicio] = "SERVICIO" ~ "(" ~ valor ~ ")" ^^
    { case k ~ "(" ~ v ~ ")" => Servicio(v) }

  def parametros: Parser[Map[String, String]] = rep(parametro) ^^ { _.toMap }

  def parametro: Parser[(String, String)] = ident ~ "(" ~ valor ~ ")" ^^
    { case k ~ "(" ~ v ~ ")" => k -> v }

  def valor: Parser[String] = """(\w|\*)+""".r
  
  def parseUno(s: String) = {
    val presult = parseAll(script, s)
    presult match {
      case Success(r, n) => parseOk(r)
      case Failure(msg, n) => println(presult)
      case Error(msg, n) => println(presult)
    }
  }

  def parseOk(rs: List[Comando]) = {
    println(rs)
    rs map (_.check)
  }

}