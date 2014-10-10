<#include "inc/functions.ftl">
<#include "inc/global-functions.ftl">
<head>
<title>${rc.getMessage("page.search.title")}</title>
<@cssAsset fileName="occportal" version=page.currentVersion! useMinified=false/>
</head>
<a id="main-content"></a>
<div id="body">
	<div id="content" class="clear_fix no_side_bar">
		<h1>${page.occModel.scientificname!rc.getMessage("occpage.scientificnamenotprovided")}</h1>
		<p class="details">
	  	<@printIfNotEmpty text=rc.getMessage("occpage.header.details")+": "+(page.occModel.collectioncode!)+ " " variable=page.occModel.catalognumber/>
		</p>

		<div class="nav_container" id="occpage_navigation">
			<ul class="buttons">
				<li><a href="?view=normal">${rc.getMessage("occpage.header.button.normal")}</a></li>
				<li><a href="?view=dwc" class="selected">${rc.getMessage("occpage.header.button.dwc")}</a></li>
			</ul>
			<a id="dwc_table_toggle" href="#">${rc.getMessage("occpage.header.toggle.unused")}</a>
		</div>
		
		<table id="dwc_table" class="sortable">
		<thead>
			<tr>
			  <th class="sorttable_numeric" scope="col">${rc.getMessage("occpage.dwctable.order")}</th>
			  <th class="sorttable_alpha" scope="col">${rc.getMessage("occpage.dwctable.category")}</th>
			  <th class="sorttable_alpha" scope="col">${rc.getMessage("occpage.dwctable.term")}</th>
			  <th class="sorttable_alpha" scope="col">${rc.getMessage("occpage.dwctable.raw")}</th>
			  <th class="sorttable_alpha" scope="col">${rc.getMessage("occpage.dwctable.interpreted")}</th>
			</tr>
		</thead>
		<tbody>
			<tr><td>1</td><td>root</td><td><a href="http://purl.org/dc/terms/type">type</a></td><td>${page.occRawModel.type?if_exists}</td><td class="unused"></td></tr>
			<tr><td>2</td><td>root</td><td><a href="http://purl.org/dc/terms/modified">modified</a></td><td> ${page.occRawModel.modified?if_exists}</td><td class="unused"></td></tr>
			<tr><td>3</td><td>root</td><td><a href="http://purl.org/dc/terms/language">language</a></td><td>${page.occRawModel.language?if_exists}</td><td class="unused"></td></tr>
			<tr><td>4</td><td>root</td><td><a href="http://purl.org/dc/terms/rights">rights</a></td><td>${page.occRawModel.rights?if_exists}</td><td class="unused"></td></tr>
			<tr><td>5</td><td>root</td><td><a href="http://purl.org/dc/terms/rightsHolder">rightsHolder</a></td><td>${page.occRawModel.rightsholder?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>6</td><td>root</td><td><a href="http://purl.org/dc/terms/accessRights">accessRights</a></td><td>${page.occRawModel.accessrights?if_exists}</td><td class="unused"></td></tr>
			<tr><td>7</td><td>root</td><td><a href="http://purl.org/dc/terms/bibliographicCitation">bibliographicCitation</a></td><td>${page.occRawModel.bibliographiccitation!}</td><td>${page.occRawModel.bibliographiccitation!}</td></tr>
			<tr><td>8</td><td>root</td><td><a href="http://purl.org/dc/terms/references">references</a></td><td>${page.occRawModel._references?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>9</td><td>root</td><td><a href="http://rs.tdwg.org/dwc/terms/institutionID">institutionID</a></td><td>${page.occRawModel.institutionid?if_exists}</td><td class="unused"></td></tr>
			<tr><td>10</td><td>root</td><td><a href="http://rs.tdwg.org/dwc/terms/collectionID">collectionID</a></td><td>${page.occRawModel.collectionid?if_exists}</td><td class="unused"></td></tr>
			<tr><td>11</td><td>root</td><td><a href="http://rs.tdwg.org/dwc/terms/datasetID">datasetID</a></td><td>${page.occRawModel.datasetid?if_exists}</td><td class="unused"></td></tr>
			<tr><td>12</td><td>root</td><td><a href="http://rs.tdwg.org/dwc/terms/institutionCode">institutionCode</a></td><td>${page.occRawModel.institutioncode?if_exists}</td><td>${page.occModel.institutioncode?if_exists}</td></tr>
			<tr><td>13</td><td>root</td><td><a href="http://rs.tdwg.org/dwc/terms/collectionCode">collectionCode</a></td><td>${page.occRawModel.collectioncode?if_exists}</td><td>${page.occModel.collectioncode?if_exists}</td></tr>
			<tr><td>14</td><td>root</td><td><a href="http://rs.tdwg.org/dwc/terms/datasetName">datasetName</a></td><td>${page.occRawModel.datasetname?if_exists}</td><td>${page.occModel.datasetname?if_exists}</td></tr>
			<tr><td>15</td><td>root</td><td><a href="http://rs.tdwg.org/dwc/terms/ownerInstitutionCode">ownerInstitutionCode</a></td><td>${page.occRawModel.ownerinstitutioncode?if_exists}</td><td class="unused"></td></tr>
			<tr><td>16</td><td>root</td><td><a href="http://rs.tdwg.org/dwc/terms/basisOfRecord">basisOfRecord</a></td><td>${page.occRawModel.basisofrecord?if_exists}</td><td>${page.occModel.basisofrecord!}</td></tr>
			<tr class="unused"><td>17</td><td>root</td><td><a href="http://rs.tdwg.org/dwc/terms/informationWithheld">informationWithheld</a></td><td>${page.occRawModel.informationwithheld?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>18</td><td>root</td><td><a href="http://rs.tdwg.org/dwc/terms/dataGeneralizations">dataGeneralizations</a></td><td>${page.occRawModel.datageneralizations?if_exists}</td><td class="unused"></td></tr>
			<tr><td>19</td><td>root</td><td><a href="http://rs.tdwg.org/dwc/terms/dynamicProperties">dynamicProperties</a></td><td>${page.occRawModel.dynamicproperties?if_exists}</td><td class="unused"></td></tr>
			<tr><td>20</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/occurrenceID">occurrenceID</a></td><td>${page.occRawModel.occurrenceid!}</td><td>${page.occModel.occurrenceid!}</td></tr>
			<tr><td>21</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/catalogNumber">catalogNumber</a></td><td>${page.occRawModel.catalognumber?if_exists}</td><td>${page.occModel.catalognumber?if_exists}</td></tr>
			<tr><td>22</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/occurrenceRemarks">occurrenceRemarks</a></td><td>${page.occRawModel.occurrenceremarks?if_exists}</td><td class="unused"></td></tr>
			<tr><td>23</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/recordNumber">recordNumber</a></td><td>${page.occRawModel.recordnumber?if_exists}</td><td>${page.occModel.recordnumber?if_exists}</td></tr>
			<tr><td>24</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/recordedBy">recordedBy</a></td><td>${page.occRawModel.recordedby?if_exists}</td><td>${page.occModel.recordedby?if_exists}</td></tr>
			<tr class="unused"><td>25</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/individualID">individualID</a></td><td>${page.occRawModel.individualid?if_exists}</td><td class="unused"></td></tr>
			<tr><td>26</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/individualCount">individualCount</a></td><td>${page.occRawModel.individualcount?if_exists}</td><td class="unused"></td></tr>
			<tr><td>27</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/sex">sex</a></td><td>${page.occRawModel.sex?if_exists}</td><td class="unused"></td></tr>
			<tr><td>28</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/lifeStage">lifeStage</a></td><td>${page.occRawModel.lifestage?if_exists}</td><td class="unused"></td></tr>
			<tr><td>29</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/reproductiveCondition">reproductiveCondition</a></td><td>${page.occRawModel.reproductivecondition?if_exists}</td><td class="unused"></td></tr>
			<tr><td>30</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/behavior">behavior</a></td><td>${page.occRawModel.behavior?if_exists}</td><td class="unused"></td></tr>
			<tr><td>31</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/establishmentMeans">establishmentMeans</a></td><td>${page.occRawModel.establishmentmeans?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>32</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/occurrenceStatus">occurrenceStatus</a></td><td>${page.occRawModel.occurrencestatus?if_exists}</td><td class="unused"></td></tr>
			<tr><td>33</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/preparations">preparations</a></td><td>${page.occRawModel.preparations?if_exists}</td><td class="unused"></td></tr>
			<tr><td>34</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/disposition">disposition</a></td><td>${page.occRawModel.disposition?if_exists}</td><td class="unused"></td></tr>
			<tr><td>35</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/otherCatalogNumbers">otherCatalogNumbers</a></td><td>${page.occRawModel.othercatalognumbers?if_exists}</td><td class="unused"></td></tr>
			<tr><td>36</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/previousIdentifications">previousIdentifications</a></td><td>${page.occRawModel.previousidentifications?if_exists}</td><td class="unused"></td></tr>
			<tr><td>37</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/associatedMedia">associatedMedia</a></td><td>${page.occRawModel.associatedmedia?if_exists}</td><td>${page.occModel.associatedmedia?if_exists}</td></tr>
			<tr><td>38</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/associatedReferences">associatedReferences</a></td><td>${page.occRawModel.associatedreferences?if_exists}</td><td class="unused"></td></tr>
			<tr><td>39</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/associatedOccurrences">associatedOccurrences</a></td><td>${page.occRawModel.associatedoccurrences?if_exists}</td><td class="unused"></td></tr>
			<tr><td>40</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/associatedSequences">associatedSequences</a></td><td>${page.occRawModel.associatedsequences!}</td><td>${page.occModel.associatedsequences!}</td></tr>
			<tr><td>41</td><td>occurrence</td><td><a href="http://rs.tdwg.org/dwc/terms/associatedTaxa">associatedTaxa</a></td><td>${page.occRawModel.associatedtaxa?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>42</td><td>event</td><td><a href="http://rs.tdwg.org/dwc/terms/eventID">eventID</a></td><td>${page.occRawModel.eventid?if_exists}</td><td class="unused"></td></tr>
			<tr><td>43</td><td>event</td><td><a href="http://rs.tdwg.org/dwc/terms/samplingProtocol">samplingProtocol</a></td><td>${page.occRawModel.samplingprotocol?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>44</td><td>event</td><td><a href="http://rs.tdwg.org/dwc/terms/samplingEffort">samplingEffort</a></td><td>${page.occRawModel.samplingeffort?if_exists}</td><td class="unused"></td></tr>
			<tr><td>45</td><td>event</td><td><a href="http://rs.tdwg.org/dwc/terms/eventDate">eventDate</a></td><td>${page.occRawModel.eventdate?if_exists}</td><td>${formatdate(page.occModel.syear!-1,page.occModel.smonth!-1,page.occModel.sday!-1)}</td></tr>
			<tr><td>46</td><td>event</td><td><a href="http://rs.tdwg.org/dwc/terms/eventTime">eventTime</a></td><td>${page.occRawModel.eventtime?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>47</td><td>event</td><td><a href="http://rs.tdwg.org/dwc/terms/startDayOfYear">startDayOfYear</a></td><td>${page.occRawModel.startdayofyear?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>48</td><td>event</td><td><a href="http://rs.tdwg.org/dwc/terms/endDayOfYear">endDayOfYear</a></td><td>${page.occRawModel.enddayofyear?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>49</td><td>event</td><td><a href="http://rs.tdwg.org/dwc/terms/year">year</a></td><td>${page.occRawModel.year?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>50</td><td>event</td><td><a href="http://rs.tdwg.org/dwc/terms/month">month</a></td><td>${page.occRawModel.month?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>51</td><td>event</td><td><a href="http://rs.tdwg.org/dwc/terms/day">day</a></td><td>${page.occRawModel.day?if_exists}</td><td class="unused"></td></tr>
			<tr><td>52</td><td>event</td><td><a href="http://rs.tdwg.org/dwc/terms/verbatimEventDate">verbatimEventDate</a></td><td>${page.occRawModel.verbatimeventdate?if_exists}</td><td class="unused"></td></tr>
			<tr><td>53</td><td>event</td><td><a href="http://rs.tdwg.org/dwc/terms/habitat">habitat</a></td><td>${page.occRawModel.habitat?if_exists}</td><td>${page.occModel.habitat?if_exists}</td></tr>
			<tr class="unused"><td>54</td><td>event</td><td><a href="http://rs.tdwg.org/dwc/terms/fieldNumber">fieldNumber</a></td><td>${page.occRawModel.fieldnumber?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>55</td><td>event</td><td><a href="http://rs.tdwg.org/dwc/terms/fieldNotes">fieldNotes</a></td><td>${page.occRawModel.fieldnotes?if_exists}</td><td class="unused"></td></tr>
			<tr><td>56</td><td>event</td><td><a href="http://rs.tdwg.org/dwc/terms/eventRemarks">eventRemarks</a></td><td>${page.occRawModel.eventremarks?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>57</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/locationID">locationID</a></td><td>${page.occRawModel.locationid?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>58</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/higherGeographyID">higherGeographyID</a></td><td>${page.occRawModel.highergeographyid?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>59</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/higherGeography">higherGeography</a></td><td>${page.occRawModel.highergeography?if_exists}</td><td class="unused"></td></tr>
			<tr><td>60</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/continent">continent</a></td><td>${page.occRawModel.continent?if_exists}</td><td>${page.occModel.continent?if_exists}</td></tr>
			<tr><td>61</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/waterBody">waterBody</a></td><td>${page.occRawModel.waterbody?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>62</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/islandGroup">islandGroup</a></td><td>${page.occRawModel.islandgroup?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>63</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/island">island</a></td><td>${page.occRawModel.island?if_exists}</td><td class="unused"></td></tr>
			<tr><td>64</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/country">country</a></td><td>${page.occRawModel.country?if_exists}</td><td>${page.occModel.country?if_exists}</td></tr>
			<tr><td>65</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/countryCode">countryCode</a></td><td>${page.occRawModel.countrycode?if_exists}</td><td class="unused"></td></tr>
			<tr><td>66</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/stateProvince">stateProvince</a></td><td>${page.occRawModel.stateprovince?if_exists}</td><td>${page.occModel.stateprovince?if_exists}</td></tr>
			<tr><td>67</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/county">county</a></td><td>${page.occRawModel.county?if_exists}</td><td>${page.occModel.county?if_exists}</td></tr>
			<tr><td>68</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/municipality">municipality</a></td><td>${page.occRawModel.municipality?if_exists}</td><td>${page.occModel.municipality?if_exists}</td></tr>
			<tr><td>69</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/locality">locality</a></td><td>${page.occRawModel.locality?if_exists}</td><td>${page.occModel.locality?if_exists}</td></tr>
			<tr class="unused"><td>70</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/verbatimLocality">verbatimLocality</a></td><td>${page.occRawModel.verbatimlocality?if_exists}</td><td class="unused"></td></tr>
			<tr><td>71</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/verbatimElevation">verbatimElevation</a></td><td>${page.occRawModel.verbatimelevation?if_exists}</td><td class="unused"></td></tr>
			<tr><td>72</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/minimumElevationInMeters">minimumElevationInMeters</a></td><td>${page.occRawModel.minimumelevationinmeters?if_exists}</td><td>${page.occModel.minimumelevationinmeters?if_exists}</td></tr>
			<tr><td>73</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/maximumElevationInMeters">maximumElevationInMeters</a></td><td>${page.occRawModel.maximumelevationinmeters?if_exists}</td><td>${page.occModel.maximumelevationinmeters?if_exists}</td></tr>
			<tr><td>74</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/verbatimDepth">verbatimDepth</a></td><td>${page.occRawModel.verbatimdepth?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>75</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/minimumDepthInMeters">minimumDepthInMeters</a></td><td>${page.occRawModel.minimumdepthinmeters?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>76</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/maximumDepthInMeters">maximumDepthInMeters</a></td><td>${page.occRawModel.maximumdepthinmeters?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>77</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/minimumDistanceAboveSurfaceInMeters">minimumDistanceAboveSurfaceInMeters</a></td><td>${page.occRawModel.minimumdistanceabovesurfaceinmeters?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>78</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/maximumDistanceAboveSurfaceInMeters">maximumDistanceAboveSurfaceInMeters</a></td><td>${page.occRawModel.maximumdistanceabovesurfaceinmeters?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>79</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/locationAccordingTo">locationAccordingTo</a></td><td>${page.occRawModel.locationaccordingto?if_exists}</td><td class="unused"></td></tr>
			<tr><td>80</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/locationRemarks">locationRemarks</a></td><td>${page.occRawModel.locationremarks?if_exists}</td><td class="unused"></td></tr>
			<tr><td>81</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/verbatimCoordinates">verbatimCoordinates</a></td><td>${page.occRawModel.verbatimcoordinates?if_exists}</td><td class="unused"></td></tr>
			<tr><td>82</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/verbatimLatitude">verbatimLatitude</a></td><td>${page.occRawModel.verbatimlatitude?if_exists}</td><td class="unused"></td></tr>
			<tr><td>83</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/verbatimLongitude">verbatimLongitude</a></td><td>${page.occRawModel.verbatimlongitude?if_exists}</td><td class="unused"></td></tr>
			<tr><td>84</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/verbatimCoordinateSystem">verbatimCoordinateSystem</a></td><td>${page.occRawModel.verbatimcoordinatesystem?if_exists}</td><td class="unused"></td></tr>
			<tr><td>85</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/verbatimSRS">verbatimSRS</a></td><td>${page.occRawModel.verbatimsrs?if_exists}</td><td class="unused"></td></tr>
			<tr><td>86</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/decimalLatitude">decimalLatitude</a></td><td>${page.occRawModel.decimallatitude?if_exists}</td><td>${safeNumber(page.occModel.decimallatitude!"","")}</td></tr>
			<tr><td>87</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/decimalLongitude">decimalLongitude</a></td><td>${page.occRawModel.decimallongitude?if_exists}</td><td>${safeNumber(page.occModel.decimallongitude!"","")}</td></tr>
			<tr><td>88</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/geodeticDatum">geodeticDatum</a></td><td>${page.occRawModel.geodeticdatum?if_exists}</td><td class="unused"></td></tr>
			<tr><td>89</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/coordinateUncertaintyInMeters">coordinateUncertaintyInMeters</a></td><td>${page.occRawModel.coordinateuncertaintyinmeters?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>90</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/coordinatePrecision">coordinatePrecision</a></td><td>${page.occRawModel.coordinateprecision?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>91</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/pointRadiusSpatialFit">pointRadiusSpatialFit</a></td><td>${page.occRawModel.pointradiusspatialfit?if_exists}</td><td class="unused"></td></tr>
			<tr><td>92</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/footprintWKT">footprintWKT</a></td><td>${page.occRawModel.footprintwkt?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>93</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/footprintSRS">footprintSRS</a></td><td>${page.occRawModel.footprintsrs?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>94</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/footprintSpatialFit">footprintSpatialFit</a></td><td>${page.occRawModel.footprintspatialfit?if_exists}</td><td class="unused"></td></tr>
			<tr><td>95</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/georeferencedBy">georeferencedBy</a></td><td>${page.occRawModel.georeferencedby?if_exists}</td><td class="unused"></td></tr>
			<tr><td>96</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/georeferencedDate">georeferencedDate</a></td><td>${page.occRawModel.georeferenceddate?if_exists}</td><td class="unused"></td></tr>
			<tr><td>97</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/georeferenceProtocol">georeferenceProtocol</a></td><td>${page.occRawModel.georeferenceprotocol?if_exists}</td><td class="unused"></td></tr>
			<tr><td>98</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/georeferenceSources">georeferenceSources</a></td><td>${page.occRawModel.georeferencesources?if_exists}</td><td class="unused"></td></tr>
			<tr><td>99</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/georeferenceVerificationStatus">georeferenceVerificationStatus</a></td><td>${page.occRawModel.georeferenceverificationstatus?if_exists}</td><td class="unused"></td></tr>
			<tr><td>100</td><td>location</td><td><a href="http://rs.tdwg.org/dwc/terms/georeferenceRemarks">georeferenceRemarks</a></td><td>${page.occRawModel.georeferenceremarks?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>101</td><td>identification</td><td><a href="http://rs.tdwg.org/dwc/terms/identificationID">identificationID</a></td><td>${page.occRawModel.identificationid?if_exists}</td><td class="unused"></td></tr>
			<tr><td>102</td><td>identification</td><td><a href="http://rs.tdwg.org/dwc/terms/identifiedBy">identifiedBy</a></td><td>${page.occRawModel.identifiedby?if_exists}</td><td class="unused"></td></tr>
			<tr><td>103</td><td>identification</td><td><a href="http://rs.tdwg.org/dwc/terms/dateIdentified">dateIdentified</a></td><td>${page.occRawModel.dateidentified?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>104</td><td>identification</td><td><a href="http://rs.tdwg.org/dwc/terms/identificationReferences">identificationReferences</a></td><td>${page.occRawModel.identificationreferences?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>105</td><td>identification</td><td><a href="http://rs.tdwg.org/dwc/terms/identificationVerificationStatus">identificationVerificationStatus</a></td><td>${page.occRawModel.identificationverificationstatus?if_exists}</td><td class="unused"></td></tr>
			<tr><td>106</td><td>identification</td><td><a href="http://rs.tdwg.org/dwc/terms/identificationRemarks">identificationRemarks</a></td><td>${page.occRawModel.identificationremarks?if_exists}</td><td class="unused"></td></tr>
			<tr><td>107</td><td>identification</td><td><a href="http://rs.tdwg.org/dwc/terms/identificationQualifier">identificationQualifier</a></td><td>${page.occRawModel.identificationqualifier?if_exists}</td><td class="unused"></td></tr>
			<tr><td>108</td><td>identification</td><td><a href="http://rs.tdwg.org/dwc/terms/typeStatus">typeStatus</a></td><td>${page.occRawModel.typestatus!}</td><td> ${page.occModel.typestatus!}</td></tr>
			<tr class="unused"><td>109</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/taxonID">taxonID</a></td><td>${page.occRawModel.taxonid?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>110</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/scientificNameID">scientificNameID</a></td><td>${page.occRawModel.scientificnameid?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>111</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/acceptedNameUsageID">acceptedNameUsageID</a></td><td>${page.occRawModel.acceptednameusageid?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>112</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/parentNameUsageID">parentNameUsageID</a></td><td>${page.occRawModel.parentnameusageid?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>113</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/originalNameUsageID">originalNameUsageID</a></td><td>${page.occRawModel.originalnameusageid?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>114</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/nameAccordingToID">nameAccordingToID</a></td><td>${page.occRawModel.nameaccordingtoid?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>115</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/namePublishedInID">namePublishedInID</a></td><td>${page.occRawModel.namepublishedinid?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>116</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/namePublishedInYear">namePublishedInYear</a></td><td>${page.occRawModel.namepublishedinyear?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>117</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/taxonConceptID">taxonConceptID</a></td><td>${page.occRawModel.taxonconceptid?if_exists}</td><td class="unused"></td></tr>
			<tr><td>118</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/scientificName">scientificName</a></td><td>${page.occRawModel.scientificname?if_exists}</td><td>${page.occModel.scientificname?if_exists}</td></tr>
			<tr><td>119</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/acceptedNameUsage">acceptedNameUsage</a></td><td>${page.occRawModel.acceptednameusage?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>120</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/parentNameUsage">parentNameUsage</a></td><td>${page.occRawModel.parentnameusage?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>121</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/originalNameUsage">originalNameUsage</a></td><td>${page.occRawModel.originalnameusage?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>122</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/nameAccordingTo">nameAccordingTo</a></td><td>${page.occRawModel.nameaccordingto?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>123</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/namePublishedIn">namePublishedIn</a></td><td>${page.occRawModel.namepublishedin?if_exists}</td><td class="unused"></td></tr>
			<tr><td>124</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/higherClassification">higherClassification</a></td><td>${page.occRawModel.higherclassification?if_exists}</td><td class="unused"></td></tr>
			<tr><td>125</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/kingdom">kingdom</a></td><td>${page.occRawModel.kingdom?if_exists}</td><td>${page.occModel.kingdom?if_exists}</td></tr>
			<tr><td>126</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/phylum">phylum</a></td><td>${page.occRawModel.phylum?if_exists}</td><td>${page.occModel.phylum?if_exists}</td></tr>
			<tr><td>127</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/class">class</a></td><td>${page.occRawModel._class?if_exists}</td><td>${page.occModel._class?if_exists}</td></tr>
			<tr><td>128</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/order">order</a></td><td>${page.occRawModel._order?if_exists}</td><td>${page.occModel._order?if_exists}</td></tr>
			<tr><td>129</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/family">family</a></td><td>${page.occRawModel.family?if_exists}</td><td>${page.occModel.family?if_exists}</td></tr>
			<tr><td>130</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/genus">genus</a></td><td>${page.occRawModel.genus?if_exists}</td><td>${page.occModel.genus?if_exists}</td></tr>
			<tr class="unused"><td>131</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/subgenus">subgenus</a></td><td>${page.occRawModel.subgenus?if_exists}</td><td class="unused"></td></tr>
			<tr><td>132</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/specificEpithet">specificEpithet</a></td><td>${page.occRawModel.specificepithet?if_exists}</td><td>${page.occModel.specificepithet?if_exists}</td></tr>
			<tr><td>133</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/infraspecificEpithet">infraspecificEpithet</a></td><td>${page.occRawModel.infraspecificepithet?if_exists}</td><td>${page.occModel.infraspecificepithet?if_exists}</td></tr>
			<tr><td>134</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/taxonRank">taxonRank</a></td><td>${page.occRawModel.taxonrank?if_exists}</td><td>${page.occModel.taxonrank?if_exists}</td></tr>
			<tr class="unused"><td>135</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/verbatimTaxonRank">verbatimTaxonRank</a></td><td>${page.occRawModel.verbatimtaxonrank?if_exists}</td><td class="unused"></td></tr>
			<tr><td>136</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/scientificNameAuthorship">scientificNameAuthorship</a></td><td>${page.occRawModel.scientificnameauthorship?if_exists}</td><td>${page.occModel.scientificnameauthorship?if_exists}</td></tr>
			<tr class="unused"><td>137</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/vernacularName">vernacularName</a></td><td>${page.occRawModel.vernacularname?if_exists}</td><td class="unused"></td></tr>
			<tr><td>138</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/nomenclaturalCode">nomenclaturalCode</a></td><td>${page.occRawModel.nomenclaturalcode?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>139</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/taxonomicStatus">taxonomicStatus</a></td><td>${page.occRawModel.taxonomicstatus?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>140</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/nomenclaturalStatus">nomenclaturalStatus</a></td><td>${page.occRawModel.nomenclaturalstatus?if_exists}</td><td class="unused"></td></tr>
			<tr><td>141</td><td>taxon</td><td><a href="http://rs.tdwg.org/dwc/terms/taxonRemarks">taxonRemarks</a></td><td>${page.occRawModel.taxonremarks?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>142</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/geologicalContextID">geologicalContextID</a></td><td>${page.occRawModel.geologicalcontextid?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>143</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/earliestEonOrLowestEonothem">earliestEonOrLowestEonothem</a></td><td>${page.occRawModel.earliesteonorlowesteonothem?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>144</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/latestEonOrHighestEonothem">latestEonOrHighestEonothem</a></td><td>${page.occRawModel.latesteonorhighesteonothem?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>145</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/earliestEraOrLowestErathem">earliestEraOrLowestErathem</a></td><td>${page.occRawModel.earliesteraorlowesterathem?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>146</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/latestEraOrHighestErathem">latestEraOrHighestErathem</a></td><td>${page.occRawModel.latesteraorhighesterathem?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>147</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/earliestPeriodOrLowestSystem">earliestPeriodOrLowestSystem</a></td><td>${page.occRawModel.earliestperiodorlowestsystem?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>148</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/latestPeriodOrHighestSystem">latestPeriodOrHighestSystem</a></td><td>${page.occRawModel.latestperiodorhighestsystem?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>149</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/earliestEpochOrLowestSeries">earliestEpochOrLowestSeries</a></td><td>${page.occRawModel.earliestepochorlowestseries?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>150</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/latestEpochOrHighestSeries">latestEpochOrHighestSeries</a></td><td>${page.occRawModel.latestepochorhighestseries?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>151</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/earliestAgeOrLowestStage">earliestAgeOrLowestStage</a></td><td>${page.occRawModel.earliestageorloweststage?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>152</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/latestAgeOrHighestStage">latestAgeOrHighestStage</a></td><td>${page.occRawModel.latestageorhigheststage?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>153</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/lowestBiostratigraphicZone">lowestBiostratigraphicZone</a></td><td>${page.occRawModel.lowestbiostratigraphiczone?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>154</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/highestBiostratigraphicZone">highestBiostratigraphicZone</a></td><td>${page.occRawModel.highestbiostratigraphiczone?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>155</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/lithostratigraphicTerms">lithostratigraphicTerms</a></td><td>${page.occRawModel.lithostratigraphicterms?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>156</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/group">group</a></td><td>${page.occRawModel.group?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>157</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/formation">formation</a></td><td>${page.occRawModel.formation?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>158</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/member">member</a></td><td>${page.occRawModel.member?if_exists}</td><td class="unused"></td></tr>
			<tr class="unused"><td>159</td><td>geological</td><td><a href="http://rs.tdwg.org/dwc/terms/bed">bed</a></td><td>${page.occRawModel.bed?if_exists}</td><td class="unused"></td></tr>
		</tbody>
		</table>
	</div>
</div><#-- body -->
<#-- JavaScript handling -->
<content tag="local_script">
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<@jsLibAsset libName="sorttable.js"/>
<@jsAsset fileName="explorer" version=page.currentVersion! useMinified=page.useMinified/>
<@jsAsset fileName="explorer.portal" version=page.currentVersion! useMinified=page.useMinified/>
</content>
