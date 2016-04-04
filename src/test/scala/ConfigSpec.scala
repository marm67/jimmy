import org.specs2.mutable._

class ConfigSpec extends Specification with Config {

  "cmci.produccion.port === 8110" >> {
    AppConfig.getInt("cmci.produccion.port") === 8110
  }

  "getCicsEntorno('produccion.atlas.elos') === List('cicselo1', 'cicselo2', 'cicselo3', 'cicselo4')" >> {
    getCicsEntorno("produccion.atlas.elos") === List("cicselo1", "cicselo2", "cicselo3", "cicselo4")
  }

}