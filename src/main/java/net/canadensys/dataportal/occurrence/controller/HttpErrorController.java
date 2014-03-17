package net.canadensys.dataportal.occurrence.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import net.canadensys.dataportal.occurrence.config.OccurrencePortalConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * ControllerAdvice to handle exceptions allow usage of template/decorator.
 * @author canadensys
 *
 */
@ControllerAdvice
public class HttpErrorController {
	
	@Autowired
	@Qualifier("occurrencePortalConfig")
	private OccurrencePortalConfig appConfig;
	
	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(value=HttpStatus.NOT_FOUND)
	public ModelAndView handleNotFound(HttpServletRequest req){
		HashMap<String,Object> modelRoot = new HashMap<String,Object>();
		//Set common stuff (GoogleAnalytics, language, ...)
		ControllerHelper.setPageHeaderVariables(appConfig, modelRoot);
        return new ModelAndView("error/404","root",modelRoot);
	}
	
}
