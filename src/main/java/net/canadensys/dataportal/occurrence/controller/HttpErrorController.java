package net.canadensys.dataportal.occurrence.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import net.canadensys.dataportal.occurrence.config.OccurrencePortalConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller to handle HTTP error (like 404) in a template.
 * @author canadensys
 *
 */
@Controller
public class HttpErrorController {
	
	@Autowired
	@Qualifier("occurrencePortalConfig")
	private OccurrencePortalConfig appConfig;
	
	@RequestMapping(value="/errors/404.html")
    public ModelAndView handle404(HttpServletRequest request) {
		HashMap<String,Object> modelRoot = new HashMap<String,Object>();
		//Set common stuff (GoogleAnalytics, language, ...)
		ControllerHelper.setPageHeaderVariables(appConfig, modelRoot);
        return new ModelAndView("error/404","root",modelRoot);
    }
}
