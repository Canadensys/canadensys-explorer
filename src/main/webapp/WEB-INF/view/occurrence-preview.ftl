<#include "inc/functions.ftl">
<div id="occ_preview_control"><span id="preview_close">${rc.getMessage("view.preview.close")} &times;</span></div>
<div id="occ_preview_content">

<h2><span id="scientificname">${root.occModel.scientificname!}</span></h2>

<dl class="occ_preview_classification clear_fix">
	<dt>${rc.getMessage("occ.kingdom")}: </dt><dd><span id="kingdom">${root.occModel.kingdom!}</span></dd>
	<dt>${rc.getMessage("occ.phylum")}: </dt><dd><span id="phylum">${root.occModel.phylum!}</span></dd>
	<dt>${rc.getMessage("occ._class")}: </dt><dd><span id="_class">${root.occModel._class!}</span></dd>
	<dt>${rc.getMessage("occ._order")}: </dt><dd><span id="_order">${root.occModel._order!}</span></dd>
	<dt>${rc.getMessage("occ.family")}: </dt><dd><span id="family">${root.occModel.family!}</span></dd>
	<dt>${rc.getMessage("occ.genus")}: </dt><dd><span id="genus">${root.occModel.genus!}</span></dd>
	<dt>${rc.getMessage("occ.rawscientificname")}: </dt><dd><span id="rawscientificname">${root.occModel.rawscientificname!}</span></dd>
</dl>
<p class="occ_preview_links"><a id="viewfullrecord" href="<@i18nResource resourceName="occurrence" param1=root.occModel.sourcefileid param2=root.occModel.dwcaid/>" target="_blank">${rc.getMessage("view.preview.viewfullrecord")}</a><span> | <a id="viewsourcerecord" href="#" target="_blank">${rc.getMessage("view.preview.viewsourcerecord")}</a></span></p>
<dl class="occ_preview_data clear_fix">
	<dt>${rc.getMessage("occ.institutioncode")}</dt><dd id="institutioncode"></dd>
	<dt>${rc.getMessage("occ.datasetname")}</dt><dd id="datasetname"></dd>
</dl>
<dl class="occ_preview_data clear_fix">
	<dt>${rc.getMessage("occ.catalognumber")}</dt><dd id="catalognumber">${root.occModel.catalognumber!}</dd>
	<dt>${rc.getMessage("occ.recordedby")}</dt><dd id="recordedby">${root.occModel.recordedby!}</dd>
	<dt>${rc.getMessage("occ.recordnumber")}</dt><dd id="recordnumber">${root.occModel.recordnumber!}</dd>
</dl>
<dl class="occ_preview_data clear_fix">
<#assign formattedStartDate = formatdate(root.occModel.syear!0,root.occModel.smonth!0,root.occModel.sday!0)>
<#assign formattedEndDate = formatdate(root.occModel.eyear!0,root.occModel.emonth!0,root.occModel.eday!0)>
	<dt>${rc.getMessage("view.preview.daterange")}</dt><dd><span id="daterange">${formattedStartDate}<#if formattedEndDate?has_content>/${formattedEndDate}</#if></span></dd>
</dl>
<dl class="occ_preview_data clear_fix">
	<dt>${rc.getMessage("occ.country")}</dt><dd id="country">${root.occModel.country!}</dd>
	<dt>${rc.getMessage("occ.stateprovince")}</dt><dd id="stateprovince">${root.occModel.stateprovince!}</dd>
	<dt>${rc.getMessage("occ.locality")}</dt><dd id="locality">${root.occModel.locality!}</dd>
	<dt>${rc.getMessage("view.preview.latlong")}</dt><dd><span id="decimallatitude"></span> <span id="decimallongitude"></span></dd>
	<#assign altituteStr = "">
	<#if root.occModel.minimumelevationinmeters??>
		<#assign altituteStr = root.occModel.minimumelevationinmeters>
		<#if root.occModel.maximumelevationinmeters?? && (root.occModel.minimumelevationinmeters != root.occModel.maximumelevationinmeters)>
			<#assign altituteStr =  altituteStr + "-" + root.occModel.maximumelevationinmeters>
		</#if>
		<#assign altituteStr =  altituteStr + " m">
	</#if>	
	<dt>${rc.getMessage("view.preview.altituderange")}</dt><dd id="altituderange">${altituteStr}</dd>
</dl>
<dl class="occ_preview_data clear_fix">
	<dt>${rc.getMessage("occ.habitat")}</dt><dd id="habitat">${root.occModel.habitat!}</dd>
</dl>
<dl class="occ_preview_data clear_fix">
	<dt>${rc.getMessage("occ.associatedmedia")}</dt><dd id="occ_preview_media"></dd>
</dl>
</div>