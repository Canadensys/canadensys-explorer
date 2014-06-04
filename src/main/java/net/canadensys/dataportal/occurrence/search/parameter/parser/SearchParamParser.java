package net.canadensys.dataportal.occurrence.search.parameter.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.canadensys.dataportal.occurrence.search.config.SearchServiceConfig;
import net.canadensys.query.QueryOperatorEnum;
import net.canadensys.query.SearchQueryPart;
import net.canadensys.query.interpreter.InsidePolygonFieldInterpreter;
import net.canadensys.utils.NumberUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;

/**
 * This class is responsible to create a collection of SearchQueryPart based
 * on URL parameters. This class is designed for Occurrence Portal search.
 * @author canadensys
 *
 */
public class SearchParamParser {
	//get log4j handler
	private static final Logger LOGGER = Logger.getLogger(SearchParamParser.class);
	
	//Pattern to roughly extract values from a QueryPart value
	private static final Pattern SEARCH_PARAM_PATTERN = Pattern.compile("^(\\d+)(_f|_o|_v(_\\d+)*)$");
	private static final Pattern DATE_VALUE_PATTERN = Pattern.compile("^([0-9]{0,4})-([0-9]{0,2})-([0-9]{0,2})$");
	private static final Pattern GEO_COORDINATES_PARAM_PATTERN = Pattern.compile("^([-+]?[0-9]*\\.?[0-9]+),([-+]?[0-9]*\\.?[0-9]+)$");
	
	private static final int GROUP_POS = 0;
	private static final int PARAM_TYPE_POS = 1;
	private static final int VAL_NUMBER_POS = 2;
	
	private static final String PARAM_TYPE_FIELD = "f";
	private static final String PARAM_TYPE_OPERATOR = "o";
	private static final String PARAM_TYPE_VALUE = "v";
	
	private SearchServiceConfig searchConfig;
	
	/**
	 * You should not use this function directly, look at SearchParamHandler.
	 * Main parsing function to convert Map<String,String> into Collection<SearchQueryPart>.
	 * The order of SearchQueryPart into the list will be kept according to the groupId.
	 * This function will skip additional parameters if they don't contain the underscore character.
	 * TODO : use a Regex
	 * @param parametersMap
	 * @return
	 */
	public Collection<SearchQueryPart> parse(Map<String,String[]> parametersMap){
		//we use a TreeMap to keep the SearchQueryPart ordered by groupId
		Map<Integer,SearchQueryPart> groupNumberQueryPartMap = new TreeMap<Integer, SearchQueryPart>();

		String[] splittedParam;

		String groupId;
		Integer groupIdInt;
		String paramType;
		String valueNumber;
		SearchQueryPart queryPart;
		String parameterValue;
		
		for(String currParameterKey : parametersMap.keySet()){
			splittedParam = currParameterKey.split("_");
			//we do not support multiple value per key
			parameterValue = parametersMap.get(currParameterKey)[0];
			
			if(splittedParam.length >= 2){
				//we use groupId to regroup all related parameters
				groupId = splittedParam[GROUP_POS];
				paramType = splittedParam[PARAM_TYPE_POS];
				
				if(StringUtils.isNumeric(groupId)){
					groupIdInt = Integer.parseInt(groupId);
					queryPart = groupNumberQueryPartMap.get(groupIdInt);
					if(queryPart == null){
						//eventually, we could defined the type of SearchQueryPart based on the fieldId
						queryPart = new SearchQueryPart();
						groupNumberQueryPartMap.put(groupIdInt, queryPart);
					}
					
					if(paramType.equalsIgnoreCase(PARAM_TYPE_FIELD)){
						if(StringUtils.isNumeric(parameterValue)){
							queryPart.setSearchableField(searchConfig.getSearchableFieldbyId(Integer.valueOf(parameterValue)));
						}
						else{//we put the parameterValue inside the field but after?
							queryPart.setSearchableField(searchConfig.getSearchableFieldbyName(parameterValue));
						}
					}
					else if(paramType.equalsIgnoreCase(PARAM_TYPE_OPERATOR)){
						//http://prideafrica.blogspot.com/2007/01/javalangclasscastexception.html
						queryPart.setOp(QueryOperatorEnum.fromString(parameterValue));
					}
					else if(paramType.equalsIgnoreCase(PARAM_TYPE_VALUE)){
						if(splittedParam.length == 3){
							valueNumber = splittedParam[VAL_NUMBER_POS];
							if(StringUtils.isNumeric(valueNumber)){
								queryPart.addValue(parameterValue, Integer.valueOf(valueNumber));
							}
						}
						else{
							queryPart.addValue(parameterValue);
						}
					}
				}
			}
		}
		return parseValues(new ArrayList<SearchQueryPart>(groupNumberQueryPartMap.values()));
	}
	
	/**
	 * Inverse function of parse(Map<String,String[]>
	 * Takes SearchQueryPart collection and turn it back to Map<String,String> so it can be used as query string.
	 * The main usage is when some SearchQueryPart are added dynamically and we need to adjust the query string.
	 * @param searchQueryParts
	 * @return
	 */
	public Map<String,String> toQueryStringMap(Collection<SearchQueryPart> searchQueryParts){
		Map<String,String> queryString = new TreeMap<String, String>();
		int currField=1;
		int currFieldValue=1;
		for(SearchQueryPart sqp: searchQueryParts){
			queryString.put(currField+"_"+PARAM_TYPE_FIELD,Integer.toString(sqp.getSearchableField().getSearchableFieldId()));
			queryString.put(currField+"_"+PARAM_TYPE_OPERATOR,sqp.getOp().toString().toLowerCase());
			currFieldValue=1;
			for(String sqpValue : sqp.getValueList()){
				queryString.put(currField+"_"+PARAM_TYPE_VALUE+"_"+currFieldValue,sqpValue);
				currFieldValue++;
			}
			currField++;
		}
		return queryString;
	}
	
	/**
	 * Parses values according to special rules in the OccurrencePortal.
	 * This need to be done after the regular parsing because we accept query part in different order.
	 * Each SearchableFieldType is handle differently according to the proper Interpreter.
	 * @param searchQueryPartList
	 * @return
	 */
	private Collection<SearchQueryPart> parseValues(Collection<SearchQueryPart> searchQueryPartList){
		for(SearchQueryPart currSearchQueryPart : searchQueryPartList){
			//validation is not done yet, be careful
			if(currSearchQueryPart.getSearchableField() != null){
				switch(currSearchQueryPart.getSearchableField().getSearchableFieldTypeEnum()){
					case SINGLE_VALUE : parseSingleValue(currSearchQueryPart);
						break;
					case MIN_MAX_NUMBER : parseMinMaxNumber(currSearchQueryPart);
						break;
					case START_END_DATE : parseStartEndDate(currSearchQueryPart);
						break;
					case INSIDE_ENVELOPE_GEO : parseInsidePolygon(currSearchQueryPart);
						break;
					case INSIDE_POLYGON_GEO : parseInsidePolygon(currSearchQueryPart);
						break;
					case WITHIN_RADIUS_GEO : parseWithinRadius(currSearchQueryPart);
						break;
					default : LOGGER.error("Unknown SearchableFieldTypeEnum value");
				}
			}
		}
		return searchQueryPartList;
	}

	public void setSearchConfig(SearchServiceConfig searchConfig) {
		this.searchConfig = searchConfig;
	}
	
	/**
	 * Tests if the parameter is a search parameter.
	 * @param value
	 * @return
	 */
	public static boolean isSearchParam(String param){
		return SEARCH_PARAM_PATTERN.matcher(param).find();
	}
	
	/**
	 * Parse the value of the currSearchQueryPart into the proper type.
	 * @param currSearchQueryPart
	 */
	protected void parseSingleValue(SearchQueryPart currSearchQueryPart){
		if(String.class.equals(currSearchQueryPart.getSearchableField().getType())){
			currSearchQueryPart.addParsedValue(currSearchQueryPart.getSingleValue(), currSearchQueryPart.getSingleField(), currSearchQueryPart.getSingleValue());
		}
		else if(Number.class.isAssignableFrom(currSearchQueryPart.getSearchableField().getType())){
			currSearchQueryPart.addParsedValue(currSearchQueryPart.getSingleValue(), currSearchQueryPart.getSingleField(), NumberUtils.parseNumber(currSearchQueryPart.getSingleValue(),currSearchQueryPart.getSearchableField().getType()));
		}
		else if(Boolean.class.equals(currSearchQueryPart.getSearchableField().getType())){
			currSearchQueryPart.addParsedValue(currSearchQueryPart.getSingleValue(), currSearchQueryPart.getSingleField(), Boolean.parseBoolean(currSearchQueryPart.getSingleValue()));
		}
		else{
			LOGGER.error("Couldn't parse values["+currSearchQueryPart.getValueList()+"] into a valid single value query. QueryPart dropped.");
		}
	}
	
	protected void parseMinMaxNumber(SearchQueryPart currSearchQueryPart){
		List<String> valueList = currSearchQueryPart.getValueList();
		List<String> fieldList = currSearchQueryPart.getFieldList();
		//if only 1 value is provided, set min and max as the same value
		if(valueList.size() == 1){
			currSearchQueryPart.addParsedValue(valueList.get(0), fieldList.get(0), NumberUtils.parseNumber(valueList.get(0),currSearchQueryPart.getSearchableField().getType()));
			currSearchQueryPart.addParsedValue(valueList.get(0), fieldList.get(1), NumberUtils.parseNumber(valueList.get(0),currSearchQueryPart.getSearchableField().getType()));
		}
		else if(valueList.size() == 2){
			currSearchQueryPart.addParsedValue(valueList.get(0), fieldList.get(0), NumberUtils.parseNumber(valueList.get(0),currSearchQueryPart.getSearchableField().getType()));
			currSearchQueryPart.addParsedValue(valueList.get(1), fieldList.get(1), NumberUtils.parseNumber(valueList.get(1),currSearchQueryPart.getSearchableField().getType()));
		}
		else{
			currSearchQueryPart.clearValues();
			LOGGER.error("Couldn't parse values["+currSearchQueryPart.getValueList()+"] into a valid min/max number query. QueryPart dropped.");
		}
	}
	
	protected void parseStartEndDate(SearchQueryPart currSearchQueryPart){
		//Composed date are received as 2009-05-27 but the Interpreter wants this as separated values
		List<String> valueList = currSearchQueryPart.getValueList();
		//try to parse all values, if one is not valid discard them all
		for(int i=0;i<valueList.size();i++){
			String value = valueList.get(i);
			Matcher m = DATE_VALUE_PATTERN.matcher(value);
			if(m.find()){
				currSearchQueryPart.addParsedValue(value, SearchServiceConfig.START_YEAR_PROPERTY, NumberUtils.parseNumber(m.group(1),Integer.class)); //year
				currSearchQueryPart.addParsedValue(value, SearchServiceConfig.START_MONTH_PROPERTY, NumberUtils.parseNumber(m.group(2),Integer.class)); //month
				currSearchQueryPart.addParsedValue(value, SearchServiceConfig.START_DAY_PROPERTY, NumberUtils.parseNumber(m.group(3),Integer.class)); //day
			}
			else{//simply clear the value to make this query part invalid
				currSearchQueryPart.clearValues();
				LOGGER.error("Couldn't parse value["+value+"] into a valid date query. QueryPart dropped.");
			}
		}
	}
	
	protected void parseInsidePolygon(SearchQueryPart currSearchQueryPart){
		//Geo coordinates are received as -125.3,34.88 but the Interpreter wants this in a Pair object
		List<String> valueList = currSearchQueryPart.getValueList();
		String relatedField = currSearchQueryPart.getSearchableField().getRelatedFields().get(InsidePolygonFieldInterpreter.GEOM_FIELD_IDX);
		for(int i=0;i<valueList.size();i++){
			String value = valueList.get(i);
			Matcher m = GEO_COORDINATES_PARAM_PATTERN.matcher(value);
			if(m.find()){
				currSearchQueryPart.addParsedValue(value, relatedField,
						Pair.of(m.group(1),m.group(2))); //lat,long
			}
			else{//simply clear the value to make this query part invalid
				currSearchQueryPart.clearValues();
				LOGGER.error("Couldn't parse value["+value+"] into a valid polygon query. QueryPart dropped.");
			}
		}
	}
	
	protected void parseWithinRadius(SearchQueryPart currSearchQueryPart){
		List<String> valueList = currSearchQueryPart.getValueList();
		boolean parsed = true;
		
		//parse coordinates, Interpreter wants this in a Pair object
		String value = valueList.get(0);
		Matcher m = GEO_COORDINATES_PARAM_PATTERN.matcher(value);
		if(m.find()){
			currSearchQueryPart.addParsedValue(value, currSearchQueryPart.getSearchableField().getRelatedField(),
				Pair.of(m.group(1),m.group(2))); //lat,long
		}
		else{
			parsed = false;
		}
		
		//parse radius
		value = valueList.get(1);
		try{
			currSearchQueryPart.addParsedValue(value, currSearchQueryPart.getSearchableField().getRelatedField(),
					Double.valueOf(value).intValue());
		}
		catch (NumberFormatException e) {
			parsed = false;
		}
		
		if(!parsed){
			//simply clear the value to make this query part invalid
			currSearchQueryPart.clearValues();
			LOGGER.error("Couldn't parse value["+value+"] into a valid within radius query. QueryPart dropped.");
		}
	}
}
