<!DOCTYPE html>
<html>
<#include "inc/functions.ftl">
<#assign javaScriptIncludeList = [
"https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js",
"http://maps.googleapis.com/maps/api/js?sensor=false",
"${root.rootURL?if_exists}js/${formatFileInclude(\"occurrence-portal\",root.currentVersion?if_exists,root.useMinified,\".js\")}"]>
<#include "inc/header.ftl">
<body>
<div id="feedback_bar"><a href="http://code.google.com/p/canadensys/issues/entry?template=Explorer%20-%20Interface%20issue" target="_blank" title="${ltext("feedback.hover")}">&nbsp;</a></div>
	<#include "inc/canadensys-header.ftl">
	<div id="body">
		<div id="side_bar">
			<p><a class="round big_button no_margin" href="${root.occRawModel.datasetid?if_exists}">${ltext("occpage.menu.datasetcontact")}</a></p>
			<#if root.occRawModel._references?has_content >
			<p><a class="round big_button" href="${root.occRawModel._references}">${ltext("occpage.menu.sourcerecord")}</a></p>
			</#if>
			
			<div id="occpage_map" class="round">
			<#-- Map injected, will remove span element -->
			<span>${ltext("occpage.nogeo")}</span>
			</div>
			
			<div id="occpage_media">
			<#if root.occModel.associatedmedia?has_content>
				<#list root.occModel.associatedmedia?split("; ") as am>
					<a class="round" href="${am}" target="_blank"><span><img src="${am}" alt="Image"/></span></a>
				</#list>
			</#if>
			</div>
		</div>
		<div id="content" class="clear_fix">
			<h1>${root.occModel.scientificname?if_exists} (${root.occModel.collectioncode?if_exists} ${root.occModel.catalognumber?if_exists})</h1>
			<p class="details">${ltext("occpage.header.details")}: ${root.occModel.sourcefileid?if_exists}/${root.occModel.dwcaid?if_exists}</p>
			<div class="nav_container" id="occpage_navigation">
			<ul class="buttons">
				<li><a href="?view=normal" class="selected">${ltext("occpage.header.button.normal")}</a></li>
				<li><a href="?view=dwc">${ltext("occpage.header.button.dwc")}</a></li>
			</ul>
			</div>
			
			<h2>${ltext("occpage.group.classification")}</h2>
			<table class="occpage_group">
			<tbody>
				<tr><th>${ltext("occ.kingdom")}</th><td>${root.occModel.kingdom?if_exists}</td></tr>
				<tr><th>${ltext("occ.phylum")}</th><td>${root.occModel.phylum?if_exists}</td></tr>
				<tr><th>${ltext("occ._class")}</th><td>${root.occModel._class?if_exists}</td></tr>
				<tr><th>${ltext("occ._order")}</th><td>${root.occModel._order?if_exists}</td></tr>
				<tr><th>${ltext("occ.family")}</th><td>${root.occModel.family?if_exists}</td></tr>
				<tr><th>${ltext("occ.genus")}</th><td>${root.occModel.genus?if_exists}</td></tr>
				<tr><th>${ltext("occ.scientificname")}</th><td>${root.occModel.scientificname?if_exists} <span class="remark">(${ltext("occpage.remark.interpretedfrom")}: ${root.occRawModel.scientificname?if_exists})</span></td></tr>
				<tr><th>${ltext("occ.scientificnameauthorship")}</th><td>${root.occModel.scientificnameauthorship?if_exists}</td></tr>
			</tbody>
			</table>
			
			<h2>${ltext("occpage.group.location")}</h2>
			<table class="occpage_group">
			<tbody>
				<tr><th>${ltext("occ.country")}</th><td>${root.occModel.country?if_exists}</td></tr>
				<tr><th>${ltext("occ.stateprovince")}</th><td>${root.occModel.stateprovince?if_exists}</td></tr>
				<tr><th>${ltext("occ.county")}</th><td>${root.occModel.county?if_exists}</td></tr>
				<tr><th>${ltext("occ.municipality")}</th><td>${root.occModel.municipality?if_exists}</td></tr>
				<tr><th>${ltext("occ.locality")}</th><td>${root.occModel.locality?if_exists}</td></tr>
			</tbody>
			<tbody>	
				<tr><th>${ltext("occ.decimallatitude")}</th><td>${safeNumber(root.occModel.decimallatitude!"","")}</td></tr>
				<tr><th>${ltext("occ.decimallongitude")}</th><td>${safeNumber(root.occModel.decimallongitude!"","")}</td></tr>
				<tr><th>${ltext("occ.coordinateuncertaintyinmeters")}</th><td>${root.occRawModel.coordinateuncertaintyinmeters?if_exists}</td></tr>
			</tbody>
			<tbody>	
				<tr><th>${ltext("occ.minimumelevationinmeters")}</th><td>${root.occModel.minimumelevationinmeters?if_exists}</td></tr>
				<tr><th>${ltext("occ.maximumelevationinmeters")}</th><td>${root.occModel.maximumelevationinmeters?if_exists}</td></tr>			
			<tbody>
			</tbody>	
				<tr><th>${ltext("occ.habitat")}</th><td>${root.occModel.habitat?if_exists}</td></tr>
			</tbody>
			</table>
			
			<h2>${ltext("occpage.group.date")}</h2>
			<table class="occpage_group">
			<tbody>	
				<tr><th>${ltext("view.preview.daterange")}</th><td>${formatdate(root.occModel.syear!-1,root.occModel.smonth!-1,root.occModel.sday!-1)}</td></tr>
			</tbody>
			</table>
			
			<h2>${ltext("occpage.group.specimen")}</h2>
			<table class="occpage_group">
			<tbody>
				<tr><th>${ltext("occ.collectioncode")}</th><td><a href="http://biocol.org/${root.occRawModel.collectionid?if_exists}">${root.occModel.collectioncode?if_exists}</a></td></tr>
				<tr><th>${ltext("occ.catalognumber")}</th><td>${root.occModel.catalognumber?if_exists}</td></tr>
				<tr><th>${ltext("occ.recordedby")}</th><td>${root.occModel.recordedby?if_exists}</td></tr>
				<tr><th>${ltext("occ.recordnumber")}</th><td>${root.occModel.recordnumber?if_exists}</td></tr>
			</tbody>
			</table>

			<h2>${ltext("occpage.group.dataset")}</h2>
			<table class="occpage_group">
			<tbody>
				<tr><th>${ltext("occ.institutioncode")}</th><td>${root.occModel.institutioncode?if_exists}</td></tr>
				<tr><th>${ltext("occ.datasetname")}</th><td><a href="${root.occRawModel.datasetid?if_exists}">${root.occModel.datasetname?if_exists}</a></td></tr>
				<tr><th>${ltext("occ.rights")}</th><td>${root.occRawModel.rights?if_exists}</td></tr>
			</tbody>
			<tbody>	
				<tr><th>${ltext("occ.sourcefileid")}</th><td>${root.occModel.sourcefileid?if_exists}</td></tr>
				<tr><th>${ltext("occ.dwcaid")}</th><td>${root.occModel.dwcaid?if_exists}</td></tr>
				<tr><th>${ltext("occ.basisofrecord")}</th><td>${root.occRawModel.basisofrecord?if_exists}</td></tr>
				<tr><th>${ltext("occ.modified")}</th><td>${root.occRawModel.modified?if_exists}</td></tr>	
			</tbody>
			</table>
		</div>
	</div><#-- body -->
<#assign coordinateuncertaintyinmeters=0>
<#if root.occRawModel.coordinateuncertaintyinmeters?? && (root.occRawModel.coordinateuncertaintyinmeters?length>0)>
<#assign coordinateuncertaintyinmeters=root.occRawModel.coordinateuncertaintyinmeters?number>
</#if>
<#assign javaScriptSetupCallList = ["occurrenceDetails.setupSingleOccurrenceMap('occpage_map',${safeNumber(root.occModel.decimallatitude!\"\",\"undefined\")},${safeNumber(root.occModel.decimallongitude!\"\",\"undefined\")},${coordinateuncertaintyinmeters?c})"]>
<#include "inc/footer.ftl">
</body>
</html>