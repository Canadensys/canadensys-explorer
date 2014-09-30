<#include "../inc/functions.ftl">

<div id="occ_preview_control"><span id="preview_close">${rc.getMessage("view.preview.close")} &times;</span></div>
<div id="occ_preview_content">

<h2>${page.occModel.scientificname!}</h2>

<#if page.occModel.typestatus?has_content>
	<p><strong>${rc.getMessage("occ.typestatus")}</strong>: ${page.occModel.typestatus!}</p>
</#if>

<dl class="occ_preview_classification clear_fix">
	<dt>${rc.getMessage("occ.kingdom")}: </dt><dd>${page.occModel.kingdom!}</dd>
	<dt>${rc.getMessage("occ.phylum")}: </dt><dd>${page.occModel.phylum!}</dd>
	<dt>${rc.getMessage("occ._class")}: </dt><dd>${page.occModel._class!}</dd>
	<dt>${rc.getMessage("occ._order")}: </dt><dd>${page.occModel._order!}</dd>
	<dt>${rc.getMessage("occ.family")}: </dt><dd>${page.occModel.family!}</dd>
	<dt>${rc.getMessage("occ.genus")}: </dt><dd>${page.occModel.genus!}</dd>
	<dt>${rc.getMessage("occ.rawscientificname")}: </dt><dd>${page.occModel.rawscientificname!}</dd>
</dl>
<p class="occ_preview_links"><a id="viewfullrecord" href="<@i18nResource resourceName="occurrence" params=[page.occModel.sourcefileid,page.occModel.dwcaid]/>" target="_blank">${rc.getMessage("view.preview.viewfullrecord")}</a>
<#if page.occModel._references??>
 | <a id="viewsourcerecord" href="${page.occModel._references}" target="_blank">${rc.getMessage("view.preview.viewsourcerecord")}</a></p>
</#if>
<dl class="occ_preview_data clear_fix">
	<dt>${rc.getMessage("occ.institutioncode")}</dt><dd>${page.occModel.institutioncode!}</dd>
	<dt>${rc.getMessage("occ.datasetname")}</dt><dd>${page.occModel.datasetname!}</dd>
</dl>
<dl class="occ_preview_data clear_fix">
	<dt>${rc.getMessage("occ.catalognumber")}</dt><dd>${page.occModel.catalognumber!}</dd>
	<dt>${rc.getMessage("occ.recordedby")}</dt><dd>${page.occModel.recordedby!}</dd>
	<dt>${rc.getMessage("occ.recordnumber")}</dt><dd>${page.occModel.recordnumber!}</dd>
</dl>
<dl class="occ_preview_data clear_fix">
<#assign formattedStartDate = formatdate(page.occModel.syear!0,page.occModel.smonth!0,page.occModel.sday!0)>
<#assign formattedEndDate = formatdate(page.occModel.eyear!0,page.occModel.emonth!0,page.occModel.eday!0)>
	<dt>${rc.getMessage("view.preview.daterange")}</dt><dd>${formattedStartDate}<#if formattedEndDate?has_content>/${formattedEndDate}</#if></dd>
</dl>
<dl class="occ_preview_data clear_fix">
	<dt>${rc.getMessage("occ.country")}</dt><dd>${page.occModel.country!}</dd>
	<dt>${rc.getMessage("occ.stateprovince")}</dt><dd>${page.occModel.stateprovince!}</dd>
	<dt>${rc.getMessage("occ.locality")}</dt><dd>${page.occModel.locality!}</dd>
	<dt>${rc.getMessage("view.preview.latlong")}</dt><dd>${page.occModel.decimallatitude!} ${page.occModel.decimallongitude!}</dd>
	<#assign altituteStr = "">
	<#if page.occModel.minimumelevationinmeters??>
		<#assign altituteStr = page.occModel.minimumelevationinmeters>
		<#if page.occModel.maximumelevationinmeters?? && (page.occModel.minimumelevationinmeters != page.occModel.maximumelevationinmeters)>
			<#assign altituteStr =  altituteStr + "-" + page.occModel.maximumelevationinmeters>
		</#if>
		<#assign altituteStr =  altituteStr + " m">
	</#if>	
	<dt>${rc.getMessage("view.preview.altituderange")}</dt><dd>${altituteStr}</dd>
</dl>
<dl class="occ_preview_data clear_fix">
	<dt>${rc.getMessage("occ.habitat")}</dt><dd>${page.occModel.habitat!}</dd>
</dl>
<dl class="occ_preview_data clear_fix">
	<dt>${rc.getMessage("occ.associatedmedia")}</dt>
	<dd>
	<#if page.occViewModel.imageViewModelList?has_content>
		${page.occViewModel.imageViewModelList?size} ${rc.getMessage("occpreview.image")}
	<#else/>
		0 ${rc.getMessage("occpreview.image")}
	</#if>
	</dd>
	<#if page.occViewModel.otherMediaViewModelList?has_content>
		<dd>
			${page.occViewModel.otherMediaViewModelList?size} ${rc.getMessage("occpreview.othermultimedia")}
		</dd>
	</#if>
	</dd>
</dl>
</div>