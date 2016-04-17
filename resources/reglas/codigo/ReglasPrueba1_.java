package reglas;

import java.io.*;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;

import com.steadystate.css.parser.CSSOMParser;

public class ReglasPrueba1 {

	public static void main(String[] args) throws Exception {
		File xmlFile = new File("recursos/xml/pet000001.xml");
		File cssFile = new File("recursos/reglas/css/regla1.css");
		
		String salida = inlineStyles(xmlFile, cssFile, false);
		System.out.println("-----");
		System.out.println(salida);

	}

	public static String inlineStyles(File xmlFile, File cssFile, boolean removeClasses)
			throws IOException {
		InputStream is = new FileInputStream(xmlFile);
		Document document = Jsoup.parse(is, "utf-8", ".");
		CSSOMParser parser = new CSSOMParser();
		InputSource source = new InputSource(new FileReader(cssFile));
		CSSStyleSheet stylesheet = parser.parseStyleSheet(source, null, null);

		CSSRuleList ruleList = stylesheet.getCssRules();
		Map<Element, Map<String, String>> allElementsStyles = new HashMap();
		for (int ruleIndex = 0; ruleIndex < ruleList.getLength(); ruleIndex++) {
			CSSRule item = ruleList.item(ruleIndex);
			if (item instanceof CSSStyleRule) {
				CSSStyleRule styleRule = (CSSStyleRule) item;
				String cssSelector = styleRule.getSelectorText();
				Elements elements = document.select(cssSelector);
				
				System.out.println("elements.size() = " + elements.size());
				for (int elementIndex = 0; elementIndex < elements.size(); elementIndex++) {
					Element element = elements.get(elementIndex);
					Map<String, String> elementStyles = allElementsStyles
							.get(element);
					if (elementStyles == null) {
						elementStyles = new LinkedHashMap<String, String>();
						allElementsStyles.put(element, elementStyles);
					}
					CSSStyleDeclaration style = styleRule.getStyle();
					for (int propertyIndex = 0; propertyIndex < style
							.getLength(); propertyIndex++) {
						String propertyName = style.item(propertyIndex);
						String propertyValue = style
								.getPropertyValue(propertyName);
						elementStyles.put(propertyName, propertyValue);
					}
				}
			}
		}

		for (Map.Entry<Element, Map<String, String>> elementEntry : allElementsStyles.entrySet()) {
			Element element = elementEntry.getKey();
			
			System.out.println("elementEntry: " + elementEntry.toString());
			
			for (Map.Entry<String, String> propiedadValor : elementEntry.getValue().entrySet()) {
				String propiedad = propiedadValor.getKey();
				String valor = propiedadValor.getValue();
				System.out.println("propiedad: " + propiedad);
				System.out.println("valor: " + valor);
				
				element.attr(propiedad, valor);
			}
		}

		return document.body().html();
	}
}
