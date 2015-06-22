package net.canadensys.dataportal.occurrence.controller;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import net.canadensys.dataportal.occurrence.OccurrenceService;
import net.canadensys.dataportal.occurrence.config.OccurrencePortalConfig;
import net.canadensys.dataportal.occurrence.controller.formatter.FormatterUtility;
import net.canadensys.dataportal.occurrence.dao.ResourceMetadataDAO;
import net.canadensys.dataportal.occurrence.model.ContactModel;
import net.canadensys.dataportal.occurrence.model.DwcaResourceModel;
import net.canadensys.dataportal.occurrence.model.MultimediaViewModel;
import net.canadensys.dataportal.occurrence.model.OccurrenceExtensionModel;
import net.canadensys.dataportal.occurrence.model.OccurrenceModel;
import net.canadensys.dataportal.occurrence.model.OccurrenceViewModel;
import net.canadensys.dataportal.occurrence.model.ResourceMetadataModel;
import net.canadensys.exception.web.ResourceNotFoundException;
import net.canadensys.web.i18n.I18nUrlBuilder;
import net.canadensys.web.i18n.annotation.I18nTranslation;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.gbif.dwc.terms.GbifTerm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.ConfigurableMimeFileTypeMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Controller of all occurrence related features of the occurrence portal.
 * Occurrence related excludes the search functionality.
 * This controller must be stateless.
 * @author canadensys
 *
 */
@Controller
public class OccurrenceController {
	
	//get log4j handler
	private static final Logger LOGGER = Logger.getLogger(OccurrenceController.class);
	private static final ConfigurableMimeFileTypeMap MIME_TYPE_MAP = new ConfigurableMimeFileTypeMap();
	
	//separators
	private static final String ASSOCIATED_SEQUENCES_SEPARATOR = "|";
	private static final String ASSOCIATED_SEQUENCES_PROVIDER_SEPARATOR = ":";
	
	//IPT variable
	private static final String IPT_ARCHIVE_PATTERN = "/archive.do?r=";
	private static final String IPT_RESOURCE_PATTERN = "/resource.do?r=";
	
	private static String VIEW_PARAM = "view";
	private static String DWC_VIEW_NAME = "dwc";
	
	@Autowired
	private OccurrenceService occurrenceService;
	
	@Autowired
	@Qualifier("occurrencePortalConfig")
	private OccurrencePortalConfig appConfig;
	
	@RequestMapping(value="/resources/{iptResource}/occurrences/{dwcaId:.+}", method=RequestMethod.GET)
	@I18nTranslation(resourceName="occurrence", translateFormat = "/resources/{}/occurrences/{}")
	public ModelAndView handleOccurrencePerResource(@PathVariable String iptResource, @PathVariable String dwcaId, HttpServletRequest request){
		Locale locale = RequestContextUtils.getLocale(request);
		
		OccurrenceModel occModel = occurrenceService.loadOccurrenceModel(iptResource,dwcaId,true);
		if(occModel == null){
			throw new ResourceNotFoundException();
		}
		
		//loadDwcaResource is using cache
		DwcaResourceModel resourceModel = occurrenceService.loadDwcaResource(occModel.getResource_id());
		if(resourceModel == null){
			throw new ResourceNotFoundException();
		}
		
		//loadResourceMetadata is using cache
		ResourceMetadataModel resourceMetadata = occurrenceService.loadResourceMetadata(resourceModel.getId());
		if(resourceMetadata == null){
			throw new ResourceNotFoundException();
		}
		
		//Get main contact
		ContactModel contactModel = null;
		if(resourceMetadata.getContacts() != null){
			for(ContactModel currContact : resourceMetadata.getContacts()){
				if(ResourceMetadataDAO.ContactRole.CONTACT.getKey().equals(currContact.getRole())){
					contactModel = currContact;
					break;
				}
			}
		}
		
		// load multimedia extension data
		List<OccurrenceExtensionModel> occMultimediaExtModelList = occurrenceService.loadOccurrenceExtensionModel(
				GbifTerm.Multimedia.simpleName(), resourceModel.getSourcefileid(), dwcaId);
		
		HashMap<String,Object> modelRoot = new HashMap<String,Object>();
		modelRoot.put("occModel", occModel);
		modelRoot.put("occRawModel", occModel.getRawModel());
		modelRoot.put("occViewModel", buildOccurrenceViewModel(occModel, resourceModel, occMultimediaExtModelList, locale));
		modelRoot.put("contactModel", contactModel);

		//Set common stuff
		ControllerHelper.setPageHeaderVariables(request,"occurrence",new String[]{iptResource,dwcaId},appConfig, modelRoot);
		
		//handle view stuff
		String view = request.getParameter(VIEW_PARAM);
		if(DWC_VIEW_NAME.equalsIgnoreCase(view)){
			return new ModelAndView("occurrence-dwc",OccurrencePortalConfig.PAGE_ROOT_MODEL_KEY,modelRoot);
		}
		return new ModelAndView("occurrence",OccurrencePortalConfig.PAGE_ROOT_MODEL_KEY,modelRoot);
	}
	
	/**
	 * Redirect this URL to a search on an IPT resource.
	 * We support this to have a clean URL to an IPT resource.
	 * @param iptResource
	 * @return
	 */
	@RequestMapping(value="/resources/{iptResource}", method=RequestMethod.GET)
	@I18nTranslation(resourceName="resource", translateFormat = "/resources/{}")
	public ModelAndView handleIptResource(@PathVariable String iptResource, HttpServletRequest request){
		if(!occurrenceService.resourceExists(iptResource)){
			throw new ResourceNotFoundException();
		}
		Locale locale = RequestContextUtils.getLocale(request);
		String searchUrl = I18nUrlBuilder.generateI18nResourcePath(locale.getLanguage(), OccurrencePortalConfig.I18N_TRANSLATION_HANDLER.getTranslationFormat("search"), (String)null);
		RedirectView rv = new RedirectView(request.getContextPath()+searchUrl+"?iptresource="+iptResource);
		rv.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
		ModelAndView mv = new ModelAndView(rv);
		return mv;
	}
	
	/**
	 * Build and fill A OccurrenceViewModel based on a OccurrenceModel.
	 * @param occModel
	 * @return OccurrenceViewModel instance, never null
	 */
	public OccurrenceViewModel buildOccurrenceViewModel(OccurrenceModel occModel, DwcaResourceModel resourceModel, List<OccurrenceExtensionModel> occMultimediaExtModelList, Locale locale){
		OccurrenceViewModel occViewModel = new OccurrenceViewModel();
		ResourceBundle bundle = appConfig.getResourceBundle(locale);
		
		//handle multimedia first (priority over associatedmedia)
		if(occMultimediaExtModelList != null){
			String multimediaFormat, multimediaLicense, multimediaReference, multimediaIdentifier, licenseShortname;
			boolean isImage;
			MultimediaViewModel multimediaViewModel;
			Map<String,String> extData;
			
			for(OccurrenceExtensionModel currMultimediaExt : occMultimediaExtModelList){
				extData = currMultimediaExt.getExt_data();
				
				multimediaFormat = StringUtils.defaultString(extData.get("format"));
				multimediaLicense = StringUtils.defaultString(extData.get("license"));
				multimediaIdentifier = extData.get("identifier");
				//if reference is blank, use the identifier
				multimediaReference = StringUtils.defaultIfBlank(extData.get("references"), multimediaIdentifier);
				
				//check if it's an image
				isImage = multimediaFormat.startsWith("image");
				licenseShortname = appConfig.getLicenseShortName(multimediaLicense);
				
				multimediaViewModel = new MultimediaViewModel(multimediaIdentifier, multimediaReference,
						extData.get("title"), multimediaLicense, extData.get("creator"), isImage, licenseShortname);
				occViewModel.addMultimediaViewModel(multimediaViewModel);
			}
		}
		
		//handle media (only if occMultimediaExtModelList was not provided)
		if((occMultimediaExtModelList == null || occMultimediaExtModelList.isEmpty()) && StringUtils.isNotEmpty(occModel.getAssociatedmedia())){
			//assumes that data are coming from harvester
			String[] media = occModel.getAssociatedmedia().split("; ");
			
			MultimediaViewModel multimediaViewModel;
			boolean isImage;
			String title;
			int imageNumber=1, otherMediaNumber=1;
			for(String currentMedia : media){
				isImage = MIME_TYPE_MAP.getContentType(currentMedia).startsWith("image");
				if(isImage){
					title = bundle.getString("occ.image") + " " + imageNumber;
					imageNumber++;
				}
				else{
					title = bundle.getString("occ.associatedmedia") + " " + otherMediaNumber;
					otherMediaNumber++;
				}
				
				multimediaViewModel = new MultimediaViewModel(currentMedia,currentMedia,
						title, null, null, isImage, null);
				occViewModel.addMultimediaViewModel(multimediaViewModel);
			}
		}
		
		//handle associated sequences
		if(StringUtils.isNotEmpty(occModel.getAssociatedsequences())){
			String[] sequences = StringUtils.split(occModel.getAssociatedsequences(), ASSOCIATED_SEQUENCES_SEPARATOR);
			
			String seqProvider, seqId, seqProviderUrlFormat;
			for(String currentSequence : sequences){
				seqProvider = StringUtils.substringBefore(currentSequence, ASSOCIATED_SEQUENCES_PROVIDER_SEPARATOR).trim().toLowerCase();
				seqId = StringUtils.substringAfter(currentSequence, ASSOCIATED_SEQUENCES_PROVIDER_SEPARATOR).trim();
				seqProviderUrlFormat = appConfig.getSequenceProviderUrlFormat(seqProvider);
				
				//set display name if defined, otherwise keep extracted name
				if(appConfig.getSequenceProviderDisplayName(seqProvider) != null){
					seqProvider = appConfig.getSequenceProviderDisplayName(seqProvider);
				}
				
				if(seqProvider != null && seqId != null){
					if(StringUtils.isBlank(seqProviderUrlFormat)){
						occViewModel.addAssociatedSequenceLink(seqProvider, seqId, "");
					}
					else{
						occViewModel.addAssociatedSequenceLink(seqProvider,seqId,MessageFormat.format(seqProviderUrlFormat, seqId));
					}
				}
				else{
					LOGGER.warn("associatedSequences [" + occModel.getAssociatedsequences() + "] cannot be parsed." );
				}
			}
		}
		
		//handle data source page URL (url to the resource page)
		if(resourceModel != null){
			if(StringUtils.contains(resourceModel.getArchive_url(),IPT_ARCHIVE_PATTERN)){
				occViewModel.setDataSourcePageURL(StringUtils.replace(resourceModel.getArchive_url(), IPT_ARCHIVE_PATTERN, IPT_RESOURCE_PATTERN));
			}
		}
		
		//handle Recommended Citation
		occViewModel.setRecommendedCitation(
				FormatterUtility.buildRecommendedCitation(occModel, occViewModel.getDataSourcePageURL(), bundle));
		
		return occViewModel;
	}
}
