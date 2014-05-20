package net.canadensys.dataportal.occurrence.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import net.canadensys.dataportal.occurrence.config.OccurrencePortalConfig;
import net.canadensys.exception.web.ResourceNotFoundException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * ControllerAdvice to handle exceptions allow usage of template/decorator.
 * @author canadensys
 *
 */
@ControllerAdvice
@Controller
public class HttpErrorController {
	
	//get log4j handler
	private static final Logger LOGGER = Logger.getLogger(HttpErrorController.class);
	
	@Autowired
	@Qualifier("occurrencePortalConfig")
	private OccurrencePortalConfig appConfig;

	@ExceptionHandler({NoHandlerFoundException.class, ResourceNotFoundException.class})
	@ResponseStatus(value=HttpStatus.NOT_FOUND)
	public ModelAndView handleNotFoundException(HttpServletRequest req){
		HashMap<String,Object> modelRoot = new HashMap<String,Object>();
		//Set common stuff (GoogleAnalytics, language, ...)
		ControllerHelper.setPageHeaderVariables(appConfig, modelRoot);
        return new ModelAndView("error/404","root",modelRoot);
	}
	
	@RequestMapping(value="/404")
	public ModelAndView handleNotFound(HttpServletRequest req){
		HashMap<String,Object> modelRoot = new HashMap<String,Object>();
		//Set common stuff (GoogleAnalytics, language, ...)
		ControllerHelper.setPageHeaderVariables(appConfig, modelRoot);
        return new ModelAndView("error/404","root",modelRoot);
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
	public ModelAndView handleError(HttpServletRequest req, Exception exception){
		LOGGER.error("Error handled by HttpErrorController",exception);
		HashMap<String,Object> modelRoot = new HashMap<String,Object>();
		//Set common stuff (GoogleAnalytics, language, ...)
		ControllerHelper.setPageHeaderVariables(appConfig, modelRoot);
        return new ModelAndView("error/error","root",modelRoot);
	}
}
