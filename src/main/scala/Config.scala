package jimmy

import java.io._
import com.typesafe.config.ConfigFactory
import collection.JavaConversions._
import grizzled.slf4j.Logging


object Config extends Logging {
	private var opciones: CmdOpciones              = null
	private var config: com.typesafe.config.Config = null

	def load(opt: CmdOpciones) = {
		opciones = opt
		config = ConfigFactory.parseFile(opt.config).resolve()		
	}

	def apply(s: String): Option[String] = s match {
		case "usuario"  => Some(opciones.usuario)
		case "password" => Some(opciones.password)
		case "script"   => Some(opciones.script)
		case _          => {
			if ( config.hasPath(s) ) Some(config.getString(s))
			else {
				val msg = s"""Parametro de configuracion "$s" no definido"""
				logger.error(msg)
				None
			}
		}
	}

	def getConfig: com.typesafe.config.Config = config
		
	def getCicsEntorno(t: String) = config.getStringList( s"""plataforma.$t""" ).toList
}
