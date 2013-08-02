<#include "inc/functions.ftl">
<#assign page={"title":rc.getMessage("page.search.title"),"cssList":[rc.getContextUrl('/styles/occportal.css')],"prefetchList":["http://tiles.canadensys.net"],
"javaScriptIncludeList":
["https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js",
rc.getContextUrl("/js/"+formatFileInclude("occurrence-portal",root.currentVersion?if_exists,root.useMinified,".js")),
rc.getContextUrl("js/lib/sorttable.js")]}>
<#include "inc/header.ftl">
<div id="feedback_bar"><a href="http://code.google.com/p/canadensys/issues/entry?template=Explorer%20-%20Interface%20issue" target="_blank" title="${rc.getMessage("feedback.hover")}">&nbsp;</a></div>
	<#include "inc/canadensys-header.ftl">
	<div id="body" class="fullscreen">
		<div id="side_bar">
			<p><a class="round big_button no_margin" href="${root.occRawModel.datasetid?if_exists}">${rc.getMessage("occpage.menu.datasetcontact")}</a></p>
			<#if root.occRawModel._references?has_content >
			<p><a class="round big_button" href="${root.occRawModel._references?if_exists}">${rc.getMessage("occpage.menu.sourcerecord")}</a></p>
			</#if>
		</div>
		<div id="content" class="clear_fix">
			<h1>${root.occModel.scientificname?if_exists} (${root.occModel.collectioncode?if_exists} ${root.occModel.catalognumber?if_exists})</h1>
			<p class="details">${ltext("occpage.header.details")}: ${root.occModel.sourcefileid?if_exists}/${root.occModel.dwcaid?if_exists}</p>
			<div class="nav_container" id="occpage_navigation">
				<ul class="buttons">
					<li><a href="?view=normal">${ltext("occpage.header.button.normal")}</a></li>
					<li><a href="?view=dwc" class="selected">${ltext("occpage.header.button.dwc")}</a></li>
				</ul>
				<a id="dwc_table_toggle" href="#">${ltext("occpage.header.toggle.unused")}</a>
			</div>
			
			<table id="dwc_table" class="sortable">
			<thead>
				<tr><th class="sorttable_numeric">${ltext("occpage.dwctable.order")}</th><th class="sorttable_alpha">${ltext("occpage.dwctable.category")}</th><th class="sorttable_alpha">${ltext("occpage.dwctable.term")}</th><th class="sorttable_alpha">${ltext("occpage.dwctable.raw")}</th><th class="sorttable_alpha">${ltext("occpage.dwctable.interpreted")}</th></tr>
			</thead>
			<tbody>
				<tr ><td>1</td><td>root</td><td><a href="http://purl.org/dc/terms/type">type</a></td><td>${root.occRawModel.type?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>2</td><td>root</td><td><a href="http://purl.org/dc/terms/modified">modified</a></td><td> ${root.occRawModel.modified?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>3</td><td>root</td><td><a href="http://purl.org/dc/terms/language">language</a></td><td>${root.occRawModel.language?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>4</td><td>root</td><td><a href="http://purl.org/dc/terms/rights">rights</a></td><td>${root.occRawModel.rights?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>5</td><td>root</td><td><a href="http://purl.org/dc/terms/rightsHolder">rightsHolder</a></td><td>${root.occRawModel.rightsholder?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>6</td><td>root</td><td><a href="http://purl.org/dc/terms/accessRights">accessRights</a></td><td>${root.occRawModel.accessrights?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>7</td><td>root</td><td><a href="http://purl.org/dc/terms/bibliographicCitation">bibliographicCitation</a></td><td>${root.occRawModel.bibliographiccitation?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>8</td><td>root</td><td><a href="http://purl.org/dc/terms/references">references</a></td><td>${root.occRawModel._references?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>9</td><td>root</td><td><a href="http://rs.tdwg.org/dwc/terms/institutionID">institutionID</a></td><td>${root.occRawModel.institutionid?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>10</td><td>root</td><td><a href="http://rs.tdwg.org/dwc/terms/collectionID">collectionID</a></td><td>${root.occRawModel.collectionid?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>11</td><td>root</td><td><a href="http://rs.tdwg.org/dwc/terms/datasetID">datasetID</a></td><td>${root.occRawModel.datasetid?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>12</td><td>root</td><td><a href="http://rs.tdwg.org/dwc/terms/institutionCode">institutionCode</a></td><td>${root.occRawModel.institutioncode?if_exists}</td><td>${root.occModel.institutioncode?if_exists}</td></tr>
				<tr ><td>13</td><td>root</td><td><a href="http://rs.tdwg.org/dwc/terms/collectionCode">collectionCode</a></td><td>${root.occRawModel.collectioncode?if_exists}</td><td>${root.occModel.collectioncode?if_exists}</td></tr>
				<tr ><td>14</td><td>root</td><td><a href="http://rs.tdwg.org/dwc/terms/datasetName">datasetName</a></td><td>${root.occRawModel.datasetname?if_exists}</td><td>${root.occModel.datasetname?if_exists}</td></tr>
				<tr ><td>15</td><td>root</td><td><a href="http://rs.tdwg.org/dwc/terms/ownerInstitutionCode">ownerInstitutionCode</a></td><td>${root.occRawModel.ownerinstitutioncode?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>16</td><td>root</td><td><a href="http://rs.tdwg.org/dwc/terms/basisOfRecord">basisOfRecord</a></td><td>${root.occRawModel.basisofrecord?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>17</td><td>root</td><td><a href="http://rs.tdwg.org/dwc/terms/informationWithheld">informationWithheld</a></td><td>${root.occRawModel.informationwithheld?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>18</td><td>root</td><td><a href="http://rs.tdwg.org/dwc/terms/dataGeneralizations">dataGeneralizations</a></td><td>${root.occRawModel.datageneralizations?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>19</td><td>root</td><td><a href="http://rs.tdwg.org/dwc/terms/dynamicProperties">dynamicProperties</a></td><td>${root.occRawModel.dynamicproperties?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>20</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/occurrenceID">occurrenceID</a></td><td>${root.occRawModel.occurrenceid?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>21</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/catalogNumber">catalogNumber</a></td><td>${root.occRawModel.catalognumber?if_exists}</td><td>${root.occModel.catalognumber?if_exists}</td></tr>
				<tr ><td>22</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/occurrenceRemarks">occurrenceRemarks</a></td><td>${root.occRawModel.occurrenceremarks?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>23</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/recordNumber">recordNumber</a></td><td>${root.occRawModel.recordnumber?if_exists}</td><td>${root.occModel.recordnumber?if_exists}</td></tr>
				<tr ><td>24</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/recordedBy">recordedBy</a></td><td>${root.occRawModel.recordedby?if_exists}</td><td>${root.occModel.recordedby?if_exists}</td></tr>
				<tr class="unused"><td>25</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/individualID">individualID</a></td><td>${root.occRawModel.individualid?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>26</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/individualCount">individualCount</a></td><td>${root.occRawModel.individualcount?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>27</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/sex">sex</a></td><td>${root.occRawModel.sex?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>28</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/lifeStage">lifeStage</a></td><td>${root.occRawModel.lifestage?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>29</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/reproductiveCondition">reproductiveCondition</a></td><td>${root.occRawModel.reproductivecondition?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>30</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/behavior">behavior</a></td><td>${root.occRawModel.behavior?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>31</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/establishmentMeans">establishmentMeans</a></td><td>${root.occRawModel.establishmentmeans?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>32</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/occurrenceStatus">occurrenceStatus</a></td><td>${root.occRawModel.occurrencestatus?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>33</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/preparations">preparations</a></td><td>${root.occRawModel.preparations?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>34</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/disposition">disposition</a></td><td>${root.occRawModel.disposition?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>35</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/otherCatalogNumbers">otherCatalogNumbers</a></td><td>${root.occRawModel.othercatalognumbers?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>36</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/previousIdentifications">previousIdentifications</a></td><td>${root.occRawModel.previousidentifications?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>37</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/associatedMedia">associatedMedia</a></td><td>${root.occRawModel.associatedmedia?if_exists}</td><td>${root.occModel.associatedmedia?if_exists}</td></tr>
				<tr ><td>38</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/associatedReferences">associatedReferences</a></td><td>${root.occRawModel.associatedreferences?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>39</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/associatedOccurrences">associatedOccurrences</a></td><td>${root.occRawModel.associatedoccurrences?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>40</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/associatedSequences">associatedSequences</a></td><td>${root.occRawModel.associatedsequences?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>41</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/associatedTaxa">associatedTaxa</a></td><td>${root.occRawModel.associatedtaxa?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>42</td><td>event</td><td><a href="http://rs.tdwg.org/dwc/terms/eventID">eventID</a></td><td>${root.occRawModel.eventid?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>43</td><td>event</td><td><a href="http://rs.tdwg.org/dwc/terms/samplingProtocol">samplingProtocol</a></td><td>${root.occRawModel.samplingprotocol?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>44</td><td>event</td><td><a href="http://rs.tdwg.org/dwc/terms/samplingEffort">samplingEffort</a></td><td>${root.occRawModel.samplingeffort?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>45</td><td>event</td><td><a href="http://rs.tdwg.org/dwc/terms/eventDate">eventDate</a></td><td>${root.occRawModel.eventdate?if_exists}</td><td>${formatdate(root.occModel.syear!-1,root.occModel.smonth!-1,root.occModel.sday!-1)}</td></tr>
				<tr ><td>46</td><td>event</td><td><a href="http://rs.tdwg.org/dwc/terms/eventTime">eventTime</a></td><td>${root.occRawModel.eventtime?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>47</td><td>event</td><td><a href="http://rs.tdwg.org/dwc/terms/startDayOfYear">startDayOfYear</a></td><td>${root.occRawModel.startdayofyear?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>48</td><td>event</td><td><a href="http://rs.tdwg.org/dwc/terms/endDayOfYear">endDayOfYear</a></td><td>${root.occRawModel.enddayofyear?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>49</td><td>event</td><td><a href="http://rs.tdwg.org/dwc/terms/year">year</a></td><td>${root.occRawModel.year?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>50</td><td>event</td><td><a href="http://rs.tdwg.org/dwc/terms/month">month</a></td><td>${root.occRawModel.month?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>51</td><td>event</td><td><a href="http://rs.tdwg.org/dwc/terms/day">day</a></td><td>${root.occRawModel.day?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>52</td><td>event</td><td><a href="http://rs.tdwg.org/dwc/terms/verbatimEventDate">verbatimEventDate</a></td><td>${root.occRawModel.verbatimeventdate?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>53</td><td>event</td><td><a href="http://rs.tdwg.org/dwc/terms/habitat">habitat</a></td><td>${root.occRawModel.habitat?if_exists}</td><td>${root.occModel.habitat?if_exists}</td></tr>
				<tr class="unused"><td>54</td><td>event</td><td><a href="http://rs.tdwg.org/dwc/terms/fieldNumber">fieldNumber</a></td><td>${root.occRawModel.fieldnumber?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>55</td><td>event</td><td><a href="http://rs.tdwg.org/dwc/terms/fieldNotes">fieldNotes</a></td><td>${root.occRawModel.fieldnotes?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>56</td><td>event</td><td><a href="http://rs.tdwg.org/dwc/terms/eventRemarks">eventRemarks</a></td><td>${root.occRawModel.eventremarks?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>57</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/locationID">locationID</a></td><td>${root.occRawModel.locationid?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>58</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/higherGeographyID">higherGeographyID</a></td><td>${root.occRawModel.highergeographyid?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>59</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/higherGeography">higherGeography</a></td><td>${root.occRawModel.highergeography?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>60</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/continent">continent</a></td><td>${root.occRawModel.continent?if_exists}</td><td>${root.occModel.continent?if_exists}</td></tr>
				<tr ><td>61</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/waterBody">waterBody</a></td><td>${root.occRawModel.waterbody?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>62</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/islandGroup">islandGroup</a></td><td>${root.occRawModel.islandgroup?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>63</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/island">island</a></td><td>${root.occRawModel.island?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>64</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/country">country</a></td><td>${root.occRawModel.country?if_exists}</td><td>${root.occModel.country?if_exists}</td></tr>
				<tr ><td>65</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/countryCode">countryCode</a></td><td>${root.occRawModel.countrycode?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>66</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/stateProvince">stateProvince</a></td><td>${root.occRawModel.stateprovince?if_exists}</td><td>${root.occModel.stateprovince?if_exists}</td></tr>
				<tr ><td>67</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/county">county</a></td><td>${root.occRawModel.county?if_exists}</td><td>${root.occModel.county?if_exists}</td></tr>
				<tr ><td>68</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/municipality">municipality</a></td><td>${root.occRawModel.municipality?if_exists}</td><td>${root.occModel.municipality?if_exists}</td></tr>
				<tr ><td>69</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/locality">locality</a></td><td>${root.occRawModel.locality?if_exists}</td><td>${root.occModel.locality}</td></tr>
				<tr class="unused"><td>70</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/verbatimLocality">verbatimLocality</a></td><td>${root.occRawModel.verbatimlocality?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>71</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/verbatimElevation">verbatimElevation</a></td><td>${root.occRawModel.verbatimelevation?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>72</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/minimumElevationInMeters">minimumElevationInMeters</a></td><td>${root.occRawModel.minimumelevationinmeters?if_exists}</td><td>${root.occModel.minimumelevationinmeters?if_exists}</td></tr>
				<tr ><td>73</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/maximumElevationInMeters">maximumElevationInMeters</a></td><td>${root.occRawModel.maximumelevationinmeters?if_exists}</td><td>${root.occModel.maximumelevationinmeters?if_exists}</td></tr>
				<tr ><td>74</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/verbatimDepth">verbatimDepth</a></td><td>${root.occRawModel.verbatimdepth?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>75</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/minimumDepthInMeters">minimumDepthInMeters</a></td><td>${root.occRawModel.minimumdepthinmeters?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>76</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/maximumDepthInMeters">maximumDepthInMeters</a></td><td>${root.occRawModel.maximumdepthinmeters?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>77</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/minimumDistanceAboveSurfaceInMeters">minimumDistanceAboveSurfaceInMeters</a></td><td>${root.occRawModel.minimumdistanceabovesurfaceinmeters?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>78</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/maximumDistanceAboveSurfaceInMeters">maximumDistanceAboveSurfaceInMeters</a></td><td>${root.occRawModel.maximumdistanceabovesurfaceinmeters?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>79</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/locationAccordingTo">locationAccordingTo</a></td><td>${root.occRawModel.locationaccordingto?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>80</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/locationRemarks">locationRemarks</a></td><td>${root.occRawModel.locationremarks?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>81</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/verbatimCoordinates">verbatimCoordinates</a></td><td>${root.occRawModel.verbatimcoordinates?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>82</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/verbatimLatitude">verbatimLatitude</a></td><td>${root.occRawModel.verbatimlatitude?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>83</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/verbatimLongitude">verbatimLongitude</a></td><td>${root.occRawModel.verbatimlongitude?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>84</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/verbatimCoordinateSystem">verbatimCoordinateSystem</a></td><td>${root.occRawModel.verbatimcoordinatesystem?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>85</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/verbatimSRS">verbatimSRS</a></td><td>${root.occRawModel.verbatimsrs?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>86</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/decimalLatitude">decimalLatitude</a></td><td>${root.occRawModel.decimallatitude?if_exists}</td><td>${safeNumber(root.occModel.decimallatitude!"","")}</td></tr>
				<tr ><td>87</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/decimalLongitude">decimalLongitude</a></td><td>${root.occRawModel.decimallongitude?if_exists}</td><td>${safeNumber(root.occModel.decimallongitude!"","")}</td></tr>
				<tr ><td>88</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/geodeticDatum">geodeticDatum</a></td><td>${root.occRawModel.geodeticdatum?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>89</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/coordinateUncertaintyInMeters">coordinateUncertaintyInMeters</a></td><td>${root.occRawModel.coordinateuncertaintyinmeters?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>90</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/coordinatePrecision">coordinatePrecision</a></td><td>${root.occRawModel.coordinateprecision?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>91</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/pointRadiusSpatialFit">pointRadiusSpatialFit</a></td><td>${root.occRawModel.pointradiusspatialfit?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>92</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/footprintWKT">footprintWKT</a></td><td>${root.occRawModel.footprintwkt?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>93</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/footprintSRS">footprintSRS</a></td><td>${root.occRawModel.footprintsrs?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>94</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/footprintSpatialFit">footprintSpatialFit</a></td><td>${root.occRawModel.footprintspatialfit?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>95</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/georeferencedBy">georeferencedBy</a></td><td>${root.occRawModel.georeferencedby?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>96</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/georeferencedDate">georeferencedDate</a></td><td>${root.occRawModel.georeferenceddate?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>97</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/georeferenceProtocol">georeferenceProtocol</a></td><td>${root.occRawModel.georeferenceprotocol?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>98</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/georeferenceSources">georeferenceSources</a></td><td>${root.occRawModel.georeferencesources?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>99</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/georeferenceVerificationStatus">georeferenceVerificationStatus</a></td><td>${root.occRawModel.georeferenceverificationstatus?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>100</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/georeferenceRemarks">georeferenceRemarks</a></td><td>${root.occRawModel.georeferenceremarks?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>101</td><td>identification</td><td><a href="http://rs.tdwg.org/dwc/terms/identificationID">identificationID</a></td><td>${root.occRawModel.identificationid?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>102</td><td>identification</td><td><a href="http://rs.tdwg.org/dwc/terms/identifiedBy">identifiedBy</a></td><td>${root.occRawModel.identifiedby?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>103</td><td>identification</td><td><a href="http://rs.tdwg.org/dwc/terms/dateIdentified">dateIdentified</a></td><td>${root.occRawModel.dateidentified?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>104</td><td>identification</td><td><a href="http://rs.tdwg.org/dwc/terms/identificationReferences">identificationReferences</a></td><td>${root.occRawModel.identificationreferences?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>105</td><td>identification</td><td><a href="http://rs.tdwg.org/dwc/terms/identificationVerificationStatus">identificationVerificationStatus</a></td><td>${root.occRawModel.identificationverificationstatus?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>106</td><td>identification</td><td><a href="http://rs.tdwg.org/dwc/terms/identificationRemarks">identificationRemarks</a></td><td>${root.occRawModel.identificationremarks?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>107</td><td>identification</td><td><a href="http://rs.tdwg.org/dwc/terms/identificationQualifier">identificationQualifier</a></td><td>${root.occRawModel.identificationqualifier?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>108</td><td>identification</td><td><a href="http://rs.tdwg.org/dwc/terms/typeStatus">typeStatus</a></td><td>${root.occRawModel.typestatus?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>109</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/taxonID">taxonID</a></td><td>${root.occRawModel.taxonid?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>110</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/scientificNameID">scientificNameID</a></td><td>${root.occRawModel.scientificnameid?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>111</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/acceptedNameUsageID">acceptedNameUsageID</a></td><td>${root.occRawModel.acceptednameusageid?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>112</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/parentNameUsageID">parentNameUsageID</a></td><td>${root.occRawModel.parentnameusageid?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>113</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/originalNameUsageID">originalNameUsageID</a></td><td>${root.occRawModel.originalnameusageid?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>114</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/nameAccordingToID">nameAccordingToID</a></td><td>${root.occRawModel.nameaccordingtoid?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>115</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/namePublishedInID">namePublishedInID</a></td><td>${root.occRawModel.namepublishedinid?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>116</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/namePublishedInYear">namePublishedInYear</a></td><td>${root.occRawModel.namepublishedinyear?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>117</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/taxonConceptID">taxonConceptID</a></td><td>${root.occRawModel.taxonconceptid?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>118</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/scientificName">scientificName</a></td><td>${root.occRawModel.scientificname?if_exists}</td><td>${root.occModel.scientificname?if_exists}</td></tr>
				<tr ><td>119</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/acceptedNameUsage">acceptedNameUsage</a></td><td>${root.occRawModel.acceptednameusage?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>120</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/parentNameUsage">parentNameUsage</a></td><td>${root.occRawModel.parentnameusage?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>121</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/originalNameUsage">originalNameUsage</a></td><td>${root.occRawModel.originalnameusage?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>122</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/nameAccordingTo">nameAccordingTo</a></td><td>${root.occRawModel.nameaccordingto?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>123</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/namePublishedIn">namePublishedIn</a></td><td>${root.occRawModel.namepublishedin?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>124</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/higherClassification">higherClassification</a></td><td>${root.occRawModel.higherclassification?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>125</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/kingdom">kingdom</a></td><td>${root.occRawModel.kingdom?if_exists}</td><td>${root.occModel.kingdom?if_exists}</td></tr>
				<tr ><td>126</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/phylum">phylum</a></td><td>${root.occRawModel.phylum?if_exists}</td><td>${root.occModel.phylum?if_exists}</td></tr>
				<tr ><td>127</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/class">class</a></td><td>${root.occRawModel._class?if_exists}</td><td>${root.occModel._class?if_exists}</td></tr>
				<tr ><td>128</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/order">order</a></td><td>${root.occRawModel._order?if_exists}</td><td>${root.occModel._order?if_exists}</td></tr>
				<tr ><td>129</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/family">family</a></td><td>${root.occRawModel.family?if_exists}</td><td>${root.occModel.family?if_exists}</td></tr>
				<tr ><td>130</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/genus">genus</a></td><td>${root.occRawModel.genus?if_exists}</td><td>${root.occModel.genus?if_exists}</td></tr>
				<tr class="unused"><td>131</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/subgenus">subgenus</a></td><td>${root.occRawModel.subgenus?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>132</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/specificEpithet">specificEpithet</a></td><td>${root.occRawModel.specificepithet?if_exists}</td><td>${root.occModel.specificepithet?if_exists}</td></tr>
				<tr ><td>133</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/infraspecificEpithet">infraspecificEpithet</a></td><td>${root.occRawModel.infraspecificepithet?if_exists}</td><td>${root.occModel.infraspecificepithet?if_exists}</td></tr>
				<tr ><td>134</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/taxonRank">taxonRank</a></td><td>${root.occRawModel.taxonrank?if_exists}</td><td>${root.occModel.taxonrank?if_exists}</td></tr>
				<tr class="unused"><td>135</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/verbatimTaxonRank">verbatimTaxonRank</a></td><td>${root.occRawModel.verbatimtaxonrank?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>136</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/scientificNameAuthorship">scientificNameAuthorship</a></td><td>${root.occRawModel.scientificnameauthorship?if_exists}</td><td>${root.occModel.scientificnameauthorship?if_exists}</td></tr>
				<tr class="unused"><td>137</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/vernacularName">vernacularName</a></td><td>${root.occRawModel.vernacularname?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>138</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/nomenclaturalCode">nomenclaturalCode</a></td><td>${root.occRawModel.nomenclaturalcode?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>139</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/taxonomicStatus">taxonomicStatus</a></td><td>${root.occRawModel.taxonomicstatus?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>140</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/nomenclaturalStatus">nomenclaturalStatus</a></td><td>${root.occRawModel.nomenclaturalstatus?if_exists}</td><td class="unused"></td></tr>
				<tr ><td>141</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/taxonRemarks">taxonRemarks</a></td><td>${root.occRawModel.taxonremarks?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>142</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/geologicalContextID">geologicalContextID</a></td><td>${root.occRawModel.geologicalcontextid?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>143</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/earliestEonOrLowestEonothem">earliestEonOrLowestEonothem</a></td><td>${root.occRawModel.earliesteonorlowesteonothem?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>144</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/latestEonOrHighestEonothem">latestEonOrHighestEonothem</a></td><td>${root.occRawModel.latesteonorhighesteonothem?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>145</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/earliestEraOrLowestErathem">earliestEraOrLowestErathem</a></td><td>${root.occRawModel.earliesteraorlowesterathem?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>146</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/latestEraOrHighestErathem">latestEraOrHighestErathem</a></td><td>${root.occRawModel.latesteraorhighesterathem?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>147</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/earliestPeriodOrLowestSystem">earliestPeriodOrLowestSystem</a></td><td>${root.occRawModel.earliestperiodorlowestsystem?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>148</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/latestPeriodOrHighestSystem">latestPeriodOrHighestSystem</a></td><td>${root.occRawModel.latestperiodorhighestsystem?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>149</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/earliestEpochOrLowestSeries">earliestEpochOrLowestSeries</a></td><td>${root.occRawModel.earliestepochorlowestseries?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>150</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/latestEpochOrHighestSeries">latestEpochOrHighestSeries</a></td><td>${root.occRawModel.latestepochorhighestseries?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>151</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/earliestAgeOrLowestStage">earliestAgeOrLowestStage</a></td><td>${root.occRawModel.earliestageorloweststage?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>152</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/latestAgeOrHighestStage">latestAgeOrHighestStage</a></td><td>${root.occRawModel.latestageorhigheststage?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>153</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/lowestBiostratigraphicZone">lowestBiostratigraphicZone</a></td><td>${root.occRawModel.lowestbiostratigraphiczone?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>154</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/highestBiostratigraphicZone">highestBiostratigraphicZone</a></td><td>${root.occRawModel.highestbiostratigraphiczone?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>155</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/lithostratigraphicTerms">lithostratigraphicTerms</a></td><td>${root.occRawModel.lithostratigraphicterms?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>156</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/group">group</a></td><td>${root.occRawModel.group?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>157</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/formation">formation</a></td><td>${root.occRawModel.formation?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>158</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/member">member</a></td><td>${root.occRawModel.member?if_exists}</td><td class="unused"></td></tr>
				<tr class="unused"><td>159</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/bed">bed</a></td><td>${root.occRawModel.bed?if_exists}</td><td class="unused"></td></tr>
			</tbody>
			</table>
		</div>
	</div><#-- body -->
<#include "inc/footer.ftl">
