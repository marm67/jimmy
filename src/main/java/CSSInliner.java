package main.java;

// http://stackoverflow.com/questions/4521557/automatically-convert-style-sheets-to-inline-style

import java.io.*; 
import java.util.*;
 
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.ErrorHandler;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Locator;
import org.w3c.css.sac.Parser;
import org.w3c.css.sac.SACMediaList;
import org.w3c.css.sac.SelectorList;

import org.w3c.css.sac.helpers.ParserFactory;

// import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.DOMException;

import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSValue;

import com.steadystate.css.dom.CSSCharsetRuleImpl;
import com.steadystate.css.dom.CSSFontFaceRuleImpl;
import com.steadystate.css.dom.CSSImportRuleImpl;
import com.steadystate.css.dom.CSSMediaRuleImpl;
import com.steadystate.css.dom.CSSOMObject;
import com.steadystate.css.dom.CSSPageRuleImpl;
import com.steadystate.css.dom.CSSRuleListImpl;
import com.steadystate.css.dom.CSSStyleDeclarationImpl;
import com.steadystate.css.dom.CSSStyleRuleImpl;
import com.steadystate.css.dom.CSSStyleSheetImpl;
import com.steadystate.css.dom.CSSUnknownRuleImpl;
import com.steadystate.css.dom.CSSValueImpl;
import com.steadystate.css.dom.MediaListImpl;
import com.steadystate.css.dom.Property;

import com.steadystate.css.sac.DocumentHandlerExt;

import com.steadystate.css.userdata.UserDataConstants;

import com.steadystate.css.parser.CSSOMParser;


public class CSSInliner {

    public static String inlineStyles(String html, File cssFile, boolean removeClasses) throws IOException {
        Document document = Jsoup.parse(html);
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
                for (int elementIndex = 0; elementIndex < elements.size(); elementIndex++) {
                    Element element = elements.get(elementIndex);
                    Map<String, String> elementStyles = allElementsStyles.get(element);
                    if (elementStyles == null) {
                        elementStyles = new LinkedHashMap<String, String>();
                        allElementsStyles.put(element, elementStyles);
                    }
                    CSSStyleDeclaration style = styleRule.getStyle();
                    for (int propertyIndex = 0; propertyIndex < style.getLength(); propertyIndex++) {
                        String propertyName = style.item(propertyIndex);
                        String propertyValue = style.getPropertyValue(propertyName);
                        elementStyles.put(propertyName, propertyValue);
                    }
                }
            }
        }

        for (Map.Entry<Element, Map<String, String>> elementEntry : allElementsStyles.entrySet()) {
            Element element = elementEntry.getKey();
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, String> styleEntry : elementEntry.getValue().entrySet()) {
                builder.append(styleEntry.getKey()).append(":").append(styleEntry.getValue()).append(";");
            }
            builder.append(element.attr("style"));
            element.attr("style", builder.toString());
            if (removeClasses) {
                element.removeAttr("class");
            }
        }

        return document.html();
    }

}