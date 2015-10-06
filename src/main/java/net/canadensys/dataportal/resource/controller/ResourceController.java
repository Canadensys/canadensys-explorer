package net.canadensys.dataportal.resource.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.canadensys.dataportal.occurrence.OccurrenceService;
import net.canadensys.dataportal.occurrence.config.OccurrencePortalConfig;
import net.canadensys.dataportal.occurrence.controller.ControllerHelper;
import net.canadensys.dataportal.occurrence.model.DwcaResourceModel;
import net.canadensys.dataportal.occurrence.model.ResourceMetadataModel;
import net.canadensys.dataportal.resource.service.ResourceService;
import net.canadensys.exception.web.ResourceNotFoundException;
import net.canadensys.web.i18n.annotation.I18nTranslation;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller of all resource related features of the explorer.
 * 
 * @author Pedro Guimarães
 * 
 */
@Controller
public class ResourceController {

	// get log4j handler
	private static final Logger LOGGER = Logger
			.getLogger(ResourceController.class);

	@Autowired
	private ResourceService resourceService;

	@Autowired
	private OccurrenceService occurrenceService;

	@Autowired
	@Qualifier("occurrencePortalConfig")
	private OccurrencePortalConfig appConfig;

	// Define maximum number of records per page
	private static final int pageSize = 15;

	// Occurrence view tags
	private static final String PAGE_PARAM = "page";

	private static final String PAGE_ONE = "1";

	/**
	 * Display a list of all current available resources with pagination
	 * support.
	 * 
	 */
	@RequestMapping(value = "/resources", method = RequestMethod.GET)
	@I18nTranslation(resourceName = "resources", translateFormat = "/resources")
	public ModelAndView handleResources(HttpServletRequest request) {
		List<DwcaResourceModel> resources = occurrenceService.loadResources();
		// filter to display only resources with records
		resources = resourceService.filterResourcesWithoutRecords(resources);
		HashMap<String, Object> modelRoot = new HashMap<String, Object>();
		String pageNumber = request.getParameter(PAGE_PARAM);
		if (resources != null) {
			List<DwcaResourceModel> pageResources = null;
			// Get total number of resources
			int totalResources = resources.size();
			// Provide the number of pages
			int totalPages = totalResources / pageSize;
			if (totalResources % pageSize != 0)
				totalPages++;
			modelRoot.put("totalResources", totalResources);
			modelRoot.put("totalPages", totalPages);
			modelRoot.put("pageSize", pageSize);
			// A page number has been provided
			if (pageNumber != null) {
				int page = Integer.parseInt(pageNumber);
				modelRoot.put("currentPage", page);
				// If the page is valid:
				if (page > 0) {
					// Treat page top limit:
					if (page > totalPages) {
						modelRoot.put("currentPage", PAGE_ONE);
						modelRoot.put("resources",
								resources.subList(0, pageSize));
					} else {
						/**
						 * Logic to load the records to a given page Ex.: Page 4
						 * → Get From 61 to 80 → Shift interval on the index
						 * list is from 60 final 79
						 */
						int shift = (page - 1) * pageSize;
						// Avoid ouf of bounds by the upper limit:
						if ((shift + pageSize) <= totalResources) {
							pageResources = resources.subList(shift,
									(shift + pageSize));
						} else {
							pageResources = resources.subList(shift,
									(shift + (totalResources - shift)));
						}
						modelRoot.put("resources", pageResources);
					}
				}
			}
			// No page provided, return first page
			else {
				modelRoot.put("currentPage", PAGE_ONE);
				// If the number of resources equals the page size
				if (totalResources >= pageSize) {
					pageResources = resources.subList(0, pageSize);
				} else {
					pageResources = resources.subList(0, totalResources);
				}
				modelRoot.put("resources", pageResources);
			}
		}
		// Set common stuff
		ControllerHelper.setResourceVariables(request, "resources", null,
				appConfig, modelRoot);

		return new ModelAndView("resources",
				OccurrencePortalConfig.PAGE_ROOT_MODEL_KEY, modelRoot);
	}

	/**
	 * Display a page with information about the current resource given its
	 * auto_id value.
	 * 
	 */
	@RequestMapping(value = "/resource/{auto_id}", method = RequestMethod.GET)
	@I18nTranslation(resourceName = "resource", translateFormat = "/resource/{}")
	public ModelAndView handleResource(@PathVariable String auto_id,
			HttpServletRequest request) {
		HashMap<String, Object> modelRoot = new HashMap<String, Object>();
		DwcaResourceModel resource = occurrenceService
				.loadResourceModelByAutoId(auto_id);
		ResourceMetadataModel information = occurrenceService
				.loadResourceMetadataModel(resource.getGbif_package_id());
		// Get current time to display in citation:
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Date date = new Date(System.currentTimeMillis());
		if (!resource.equals(null) && !information.equals(null)) {
			modelRoot.put("resource", resource);
			modelRoot.put("information", information);
			modelRoot.put("currentTime", sdf.format(date));
		} else {
			LOGGER.error("ResourceNotFoundException at ResourceController.handleResource()");
			throw new ResourceNotFoundException();
		}
		// Set common stuff
		ControllerHelper.setResourceVariables(request, "resource", auto_id,
				appConfig, modelRoot);

		return new ModelAndView("resource",
				OccurrencePortalConfig.PAGE_ROOT_MODEL_KEY, modelRoot);
	}
}