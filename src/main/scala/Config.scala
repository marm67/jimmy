package main.scala

import java.io._
import com.typesafe.config.ConfigFactory
import collection.JavaConversions._

object Config {
	private var opciones: CmdOpciones              = null
	private var config: com.typesafe.config.Config = null

	def load(opt: CmdOpciones) = {
		opciones = opt
		config = ConfigFactory.parseFile(opt.config).resolve()		
	}

	def apply(s: String) = s match {
		case "usuario"  => opciones.usuario
		case "password" => opciones.password
		case "script"   => opciones.script
		case _          => config.getString(s)
	}
		
	def getCicsEntorno(t: String) = config.getStringList( s"""plataforma.$t""" ).toList
}
