package jimmy

import java.io._
import java.util._
import freemarker.template._
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer

object Plantilla {
    val cfg = new Configuration
    val pathPlantillas = Config("path.plantillas").get
    cfg.setDirectoryForTemplateLoading(new File(pathPlantillas))

    def apply(nombre: String, params: scala.collection.immutable.Map[String, String]): String = {
        try {
            val template = cfg.getTemplate( s"$nombre.ftl" )
            val data = scala.collection.mutable.Map[String, String]()
            params foreach { x => data += x }
            val output = new StringWriter
            template.process(data.asJava, output)
            output.toString
        } catch {
            case t: Throwable => t.toString
        }
    }
}

