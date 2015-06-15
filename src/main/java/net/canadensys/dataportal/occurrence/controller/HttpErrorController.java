package net.canadensys.dataportal.occurrence.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import net.canadensys.dataportal.occurrence.config.OccurrencePortalConfig;
import net.canadensys.exception.web.ResourceNotFoundException;
import net.canadensys.web.i18n.annotation.I18nTranslation;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestMethodNotSupportedException;
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
		ControllerHelper.setPageHeaderVariables(req,"404",null,appConfig, modelRoot);
        return new ModelAndView("error/404",OccurrencePortalConfig.PAGE_ROOT_MODEL_KEY,modelRoot);
	}
	
	@RequestMapping(value="/404")
	@I18nTranslation(resourceName="404", translateFormat = "/404")
	@ResponseStatus(value=HttpStatus.NOT_FOUND)
	public ModelAndView handleNotFound(HttpServletRequest req){
		HashMap<String,Object> modelRoot = new HashMap<String,Object>();
		ControllerHelper.setPageHeaderVariables(req,"404",null,appConfig, modelRoot);
        return new ModelAndView("error/404",OccurrencePortalConfig.PAGE_ROOT_MODEL_KEY,modelRoot);
	}
	
	/**
	 * Exception thrown when the Method is not supported by our definitions.
	 * Normally, it's a HEAD request.
	 * 
	 * @param req
	 * @return
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(value=HttpStatus.METHOD_NOT_ALLOWED)
	public ModelAndView handleHttpRequestMethodNotSupported(HttpServletRequest req){
		LOGGER.debug("RequestMethodNotSupported handled by HttpErrorController:" + req.getMethod() + " at " + req.getRequestURI());
		HashMap<String,Object> model = new HashMap<String,Object>();
		ControllerHelper.setPageHeaderVariables(req,"404",null,appConfig, model);
        return new ModelAndView("error/404", OccurrencePortalConfig.PAGE_ROOT_MODEL_KEY, model);
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
	public ModelAndView handleError(HttpServletRequest req, Exception exception){
		LOGGER.error("Error handled by HttpErrorController",exception);
		HashMap<String,Object> modelRoot = new HashMap<String,Object>();
		ControllerHelper.setPageHeaderVariables(req,"search",null,appConfig, modelRoot);
        return new ModelAndView("error/error",OccurrencePortalConfig.PAGE_ROOT_MODEL_KEY,modelRoot);
	}
}
