<#include "../inc/functions.ftl">
<div id="occ_preview_control"><span id="preview_close">${rc.getMessage("view.preview.close")} &times;</span></div>
<div id="occ_preview_content">

<h2>${root.occModel.scientificname!}</h2>

<dl class="occ_preview_classification clear_fix">
	<dt>${rc.getMessage("occ.kingdom")}: </dt><dd>${root.occModel.kingdom!}</dd>
	<dt>${rc.getMessage("occ.phylum")}: </dt><dd>${root.occModel.phylum!}</dd>
	<dt>${rc.getMessage("occ._class")}: </dt><dd>${root.occModel._class!}</dd>
	<dt>${rc.getMessage("occ._order")}: </dt><dd>${root.occModel._order!}</dd>
	<dt>${rc.getMessage("occ.family")}: </dt><dd>${root.occModel.family!}</dd>
	<dt>${rc.getMessage("occ.genus")}: </dt><dd>${root.occModel.genus!}</dd>
	<dt>${rc.getMessage("occ.rawscientificname")}: </dt><dd>${root.occModel.rawscientificname!}</dd>
</dl>
<p class="occ_preview_links"><a id="viewfullrecord" href="<@i18nResource resourceName="occurrence" params=[root.occModel.sourcefileid,root.occModel.dwcaid]/>" target="_blank">${rc.getMessage("view.preview.viewfullrecord")}</a>
<#if root.occModel._references??>
 | <a id="viewsourcerecord" href="${root.occModel._references}" target="_blank">${rc.getMessage("view.preview.viewsourcerecord")}</a></p>
</#if>
<dl class="occ_preview_data clear_fix">
	<dt>${rc.getMessage("occ.institutioncode")}</dt><dd>${root.occModel.institutioncode!}</dd>
	<dt>${rc.getMessage("occ.datasetname")}</dt><dd>${root.occModel.datasetname!}</dd>
</dl>
<dl class="occ_preview_data clear_fix">
	<dt>${rc.getMessage("occ.catalognumber")}</dt><dd>${root.occModel.catalognumber!}</dd>
	<dt>${rc.getMessage("occ.recordedby")}</dt><dd>${root.occModel.recordedby!}</dd>
	<dt>${rc.getMessage("occ.recordnumber")}</dt><dd>${root.occModel.recordnumber!}</dd>
</dl>
<dl class="occ_preview_data clear_fix">
<#assign formattedStartDate = formatdate(root.occModel.syear!0,root.occModel.smonth!0,root.occModel.sday!0)>
<#assign formattedEndDate = formatdate(root.occModel.eyear!0,root.occModel.emonth!0,root.occModel.eday!0)>
	<dt>${rc.getMessage("view.preview.daterange")}</dt><dd>${formattedStartDate}<#if formattedEndDate?has_content>/${formattedEndDate}</#if></dd>
</dl>
<dl class="occ_preview_data clear_fix">
	<dt>${rc.getMessage("occ.country")}</dt><dd>${root.occModel.country!}</dd>
	<dt>${rc.getMessage("occ.stateprovince")}</dt><dd>${root.occModel.stateprovince!}</dd>
	<dt>${rc.getMessage("occ.locality")}</dt><dd>${root.occModel.locality!}</dd>
	<dt>${rc.getMessage("view.preview.latlong")}</dt><dd>${root.occModel.decimallatitude!} ${root.occModel.decimallongitude!}</dd>
	<#assign altituteStr = "">
	<#if root.occModel.minimumelevationinmeters??>
		<#assign altituteStr = root.occModel.minimumelevationinmeters>
		<#if root.occModel.maximumelevationinmeters?? && (root.occModel.minimumelevationinmeters != root.occModel.maximumelevationinmeters)>
			<#assign altituteStr =  altituteStr + "-" + root.occModel.maximumelevationinmeters>
		</#if>
		<#assign altituteStr =  altituteStr + " m">
	</#if>	
	<dt>${rc.getMessage("view.preview.altituderange")}</dt><dd>${altituteStr}</dd>
</dl>
<dl class="occ_preview_data clear_fix">
	<dt>${rc.getMessage("occ.habitat")}</dt><dd>${root.occModel.habitat!}</dd>
</dl>
<dl class="occ_preview_data clear_fix">
	<dt>${rc.getMessage("occ.associatedmedia")}</dt>
	<dd>
		<#if root.occViewModel.imageList?has_content>
			<a href="${root.occViewModel.imageList[0]}"><img src="${root.occViewModel.imageList[0]}" alt="${root.occModel.scientificname!} (${root.occModel.collectioncode!} ${root.occModel.catalognumber!})" target="_blank"/></a>
		<#elseif root.occViewModel.otherMediaList?has_content>
			<a href="${root.occViewModel.otherMediaList[0]}">${rc.getMessage("occpage.menu.associatedmedia")}</a>
		</#if>
	</dd>
</dl>
</div>