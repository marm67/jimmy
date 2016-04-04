import java.io._
import com.typesafe.config.ConfigFactory
import collection.JavaConversions._

    

trait Config {
    private val CONF_PATH = """c:/scala/proyectos/jimmy/resources/conf"""
    val AppConfig = ConfigFactory.parseFile(new File(CONF_PATH + """/app.conf""")).resolve()
    val EntornosConfig = ConfigFactory.parseFile(new File(CONF_PATH + """/entornos.conf""")).resolve()

    def getAppConfig = AppConfig

    def getCicsEntorno(t: String) = EntornosConfig.getStringList(t).toList
}