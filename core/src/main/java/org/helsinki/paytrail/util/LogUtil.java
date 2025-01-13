package org.helsinki.paytrail.util;


import org.slf4j.Logger;

public class LogUtil {
    public static void filteredLog(Logger log, String text){
        // filter possible cardtoken out of logs
        text = filterJsonElementFromString(
                log,
                text,
                "token",
                "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx");
        log.info(text);
    }

    public static String filterJsonElementFromString(Logger log, String stringToFilter, String elementName, String valueReplace){
        try{
            String elementNameString = "\"" + elementName + "\"";
            String valueReplaceString = "\"" + valueReplace + "\"";
            int tokenElementStartIndex = stringToFilter.indexOf(elementNameString);
            if( tokenElementStartIndex != -1) {
                int tokenEndIndex = stringToFilter.indexOf(",", tokenElementStartIndex);
                if (tokenEndIndex == -1) {
                    // was not array or end of an array
                    // search end of an object
                    tokenEndIndex = stringToFilter.indexOf("}", tokenElementStartIndex);
                    if (tokenEndIndex == -1) {
                        // could not locate section to filter, return unfiltered
                        return stringToFilter;
                    }
                }
                String tokenElement = stringToFilter.substring(tokenElementStartIndex, tokenEndIndex);
                return stringToFilter.replace(tokenElement, elementNameString + ":" + valueReplaceString);
            }
        } catch (Exception e) {
            log.error("Error while filtering log.", e);
        }
        return stringToFilter;
    }
}
