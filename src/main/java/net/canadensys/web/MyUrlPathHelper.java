package net.canadensys.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.util.UriUtils;
import org.springframework.web.util.UrlPathHelper;

public class MyUrlPathHelper extends UrlPathHelper {
	
	@Override
	public void setUrlDecode(boolean urlDecode) {
		System.out.println("setUrlDecode called");
		super.setUrlDecode(urlDecode);
	}

	@Override
	public Map<String, String> decodePathVariables(HttpServletRequest request,
			Map<String, String> vars) {
		//System.out.println("DECODED CALLED!!," + this.);
		//String servletPathAttribute = (String) request.getAttribute(WebUtils.INCLUDE_SERVLET_PATH_ATTRIBUTE);
		
		String pathWithinApp = getPathWithinApplication(request);
		String servletPath = getServletPath(request);
		String path = getRemainingPath(pathWithinApp, servletPath, false);
		
		System.out.println("request.getServletPath()"+request.getServletPath());
		System.out.println("pathWithinApp"+pathWithinApp);
		System.out.println("servletPath"+servletPath);
		System.out.println("path"+path);
		
		for(String currKey : vars.keySet()){
			System.out.println("Content " + currKey +"," + vars.get(currKey));
		}
		
		Map<String, String> decoded = super.decodePathVariables(request, vars);
		
		for(String currKey : decoded.keySet()){
			System.out.println("Decoded Content " + currKey +"," + decodeInternal(request,decoded.get(currKey)));
		}
		
		return super.decodePathVariables(request, vars);
	}
	
	private String decodeInternal(HttpServletRequest request, String source) {
		String enc = determineEncoding(request);
		System.out.println("determineEncoding=" + enc);
		try {
			return UriUtils.decode(source, enc);
		}
		catch (UnsupportedEncodingException ex) {
			System.out.println("Could not decode request string [" + source + "] with encoding '" + enc +
						"': falling back to platform default encoding; exception message: " + ex.getMessage());
		}
		return URLDecoder.decode(source);
	}
	
	private String getRemainingPath(String requestUri, String mapping, boolean ignoreCase) {
		int index1 = 0;
		int index2 = 0;
		for (; (index1 < requestUri.length()) && (index2 < mapping.length()); index1++, index2++) {
			char c1 = requestUri.charAt(index1);
			char c2 = mapping.charAt(index2);
			if (c1 == ';') {
				index1 = requestUri.indexOf('/', index1);
				if (index1 == -1) {
					return null;
				}
				c1 = requestUri.charAt(index1);
			}
			if (c1 == c2) {
				continue;
			}
			if (ignoreCase && (Character.toLowerCase(c1) == Character.toLowerCase(c2))) {
				continue;
			}
			return null;
		}
		if (index2 != mapping.length()) {
			return null;
		}
		if (index1 == requestUri.length()) {
			return "";
		}
		else if (requestUri.charAt(index1) == ';') {
			index1 = requestUri.indexOf('/', index1);
		}
		return (index1 != -1 ? requestUri.substring(index1) : "");
	}
}
