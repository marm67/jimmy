package main.scala

import scala.util.parsing.combinator._
import scala.util.parsing.combinator.syntactical._

class Parser extends JavaTokenParsers {
  
  def script: Parser[Seq[Comando]] = comando ~ rep(comando) ^^
    { case a ~ b => a :: b }

  def comando: Parser[Comando] = cemt | ceda | set

  def cemt: Parser[Comando] = "CEMT" ~ cemtOp ~ recurso ~ parametros ^^
    { case a ~ b ~ c ~ d => Cemt(b, c, d) }

  def cemtOp: Parser[CemtOp] = "INQUIRE" | "CREATE" | "SET" | "DISCARD" ^^  { 
      case "INQUIRE" => Inquire
      case "CREATE"  => Create
      case "SET"     => Set
      case "DISCARD" => Discard
  }

  def ceda: Parser[Comando] = "CEDA" ~> accionCeda ~ recurso ~ parametros ^^
    { case a ~ b ~ c => Ceda(a,b,c) }

  def accionCeda: Parser[String] = "EXPAND" | "DEFINE" | "ALTER" | "DELETE" ^^  { 
      case "EXPAND" => Expand
      case "DEFINE" => Define
      case "ALTER"  => Alter
      case "DELETE" => Delete
  }

  def define: Parser[Comando] = "DEFINE" ~> (servicio("define") ~ parametros) ^^
    { case serv ~ param => servDefine(a, b) }

  def set: Parser[Comando] = "SET" ~> ident ~ ("(" ~> ident <~ ")") ^^
    { case a ~ b => Set(a, b) }

  def servicio(op: String): Parser[Servicio] = "SERVICIO" ~ "(" ~ valor ~ ")" ^^
    { case k ~ "(" ~ v ~ ")" => Servicio(v) }

  def parametros: Parser[Map[String, String]] = rep(parametro) ^^ { _.toMap }

  def parametro: Parser[(String, String)] = ident ~ "(" ~ valor ~ ")" ^^
    { case k ~ "(" ~ v ~ ")" => k -> v }

  def valor: Parser[String] = """(\w|\*)+""".r
  
  def parseUno(s: String) = {
    val S = s.toUpperCase
    val presult = parseAll(script, S)
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