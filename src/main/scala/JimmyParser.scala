// package jimmy

// import jimmy._

//   def script: Parser[Seq[Comando]] = comando ~ rep(comando) ^^
//     { case a ~ b => a :: b }

//   def comando: Parser[Comando] = cemt | ceda | set

// object JimmyParser extends ComandoParser {
//   def script : Parser[Script] = listaComandos ^^ (Script(_))
//   def load(s: String) : Script = parseAll(script,s).get
// }

// trait ComandoParser extends RegexParsers { self: ExpressionParser with SetParser with CemtParser with CedaParser =>
//   def listaComandos = comandos ^^ (`{}`(_))
//   def comandos =  comando*
//   def comando: Parser[Comando] =  cemt | ceda | set
// }

// trait SetParser extends JavaTokenParsers { 
//   def set: Parser[Comando] = "SET" ~> ident ~ ("(" ~> ident <~ ")") ^^ { case a ~ b => SET(a, b) }
// }

// trait CemtParser extends JavaTokenParsers { self: 
//   def cemt: Parser[Comando] = "CEMT" ~> _cemtOp ~ recursoT ^^ { case a ~ b  => CEMT(a, b) }

//   private def _cemtOp: Parser[CemtOp] = "INQUIRE" | "CREATE" | "SET" | "DISCARD" ^^  { 
//       case "INQUIRE" => Inquire
//       case "CREATE"  => Create
//       case "SET"     => Set
//       case "DISCARD" => Discard
//   }

// trait RecursoTParser extends JavaTokenParsers { self: 
//   def cemt: Parser[Comando] = "CEMT" ~> _cemtOp ~ recursoT ^^ { case a ~ b  => CEMT(a, b) }

//   private def _cemtOp: Parser[CemtOp] = "INQUIRE" | "CREATE" | "SET" | "DISCARD" ^^  { 
//       case "INQUIRE" => Inquire
//       case "CREATE"  => Create
//       case "SET"     => Set
//       case "DISCARD" => Discard
//   }
// }

