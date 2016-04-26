package jimmy

import jimmy._
import scala.util.parsing.combinator._
import scala.util.parsing.combinator.syntactical._

object Parser extends JavaTokenParsers {

  def script: Parser[Seq[Comando]] = comando ~ rep(comando) ^^ { case a ~ b => a :: b }

  def comando: Parser[Comando] = cemt | ceda | set

  def set: Parser[Comando] = "set" ~> ident ~ ("(" ~> ident <~ ")") ^^ { case a ~ b => SET(a, b) }

  def cemt: Parser[Comando] = "cemt" ~> ("inquire" | "create" | "set" | "discard") ~ recursoT ^^ { case a ~ b  => CEMT(a, b) }

  def ceda: Parser[Comando] = "ceda" ~> ("expand" | "define" | "alter" | "delete") ~ recursoT ^^ { case a ~ b => CEDA(a, b) }

  def recursoT: Parser[RecursoT] = servicio | recurso ^^ { case a => a }

  def servicio: Parser[Servicio] = "servicio" ~ "(" ~ valor ~ ")" ~ parametros ^^ { case a ~ "(" ~ b ~ ")" ~ c => Servicio(b, c) }

  def recurso: Parser[Recurso] = ident ~ "(" ~ valor ~ ")" ~ parametros ^^ { case a ~ "(" ~ b ~ ")" ~ c => Recurso(a, b, c) }

  def parametros: Parser[Map[String, String]] = rep(parametro) ^^ { _.toMap }

  def parametro: Parser[(String, String)] = ident ~ "(" ~ valor ~ ")" ^^ { case k ~ "(" ~ v ~ ")" => k -> v }

  def valor: Parser[String] = """(\w|\*)+""".r
  
  def apply(s: String) = {
    val S = s.toLowerCase
    val presult = parseAll(script, S)
    presult match {
      case Success(r, n) => parseOk( Script(r) )
      case Failure(msg, n) => println(presult)
      case Error(msg, n) => println(presult)
    }
  }

  def parseOk(rs: Script) = {
//    println(rs)
    Interprete.execute(rs)
//    rs map (_.check)
  }

}