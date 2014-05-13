<#include "inc/functions.ftl">
<#include "inc/global-functions.ftl">
<head>
<title>${rc.getMessage("page.search.title")}</title>
<@cssAsset fileName="occportal" version=root.currentVersion! useMinified=false/>
</head>

<content tag="lang_switch">
<@i18nLanguageSwitch resourceName="occurrence" params=[root.occModel.sourcefileid,root.occModel.dwcaid]/>
</content>

<a id="main-content"></a>
<div id="body">
	<div id="side_bar">
		<p><a class="round big_button no_margin" href="../contact">${rc.getMessage("occpage.menu.datasetcontact")}</a></p>
		<#if root.occRawModel._references?has_content >
		<p><a class="round big_button" href="${root.occRawModel._references}">${rc.getMessage("occpage.menu.sourcerecord")}</a></p>
		</#if>
		
		<div id="occpage_map" class="round">
		<#-- Map injected, will remove span element -->
		<span>${rc.getMessage("occpage.nogeo")}</span>
		</div>
		
		<#if root.occViewModel.imageList?has_content>
			<div id="occpage_image">
				<#list root.occViewModel.imageList as currImg>
				<a class="round" href="${currImg}"><span><img src="${currImg}" alt="${root.occModel.scientificname?if_exists} (${root.occModel.collectioncode?if_exists} ${root.occModel.catalognumber?if_exists})"/></span></a>
				</#list>
			</div>
		</#if>
		<#if root.occViewModel.otherMediaList?has_content>
			<div id="occpage_media">
				<#assign mediaNumber = 1>
				<#list root.occViewModel.otherMediaList as currOm>
				<p><a class="round big_button" href="${currOm}" target="_blank">${rc.getMessage("occpage.menu.associatedmedia")} ${mediaNumber}</a></p>
				<#assign mediaNumber = mediaNumber + 1>
				</#list>
			</div>
		</#if>
	</div>
	<div id="content" class="clear_fix">
		<h1>${root.occModel.scientificname?if_exists} (${root.occModel.collectioncode?if_exists} ${root.occModel.catalognumber?if_exists})</h1>
		<p class="details">${rc.getMessage("occpage.header.details")}: ${root.occModel.sourcefileid?if_exists}/${root.occModel.dwcaid?if_exists}</p>
		<div class="nav_container" id="occpage_navigation">
		<ul class="buttons">
			<li><a href="?view=normal" class="selected">${rc.getMessage("occpage.header.button.normal")}</a></li>
			<li><a href="?view=dwc">${rc.getMessage("occpage.header.button.dwc")}</a></li>
		</ul>
		</div>

		<#if root.occModel.hastypestatus?? && root.occModel.hastypestatus>
			<h2>${rc.getMessage("occpage.group.typestatus")}</h2>
			<p>${root.occModel.typestatus!}</p>
		</#if>

		<h2>${rc.getMessage("occpage.group.classification")}</h2>
		<table class="occpage_group">
		<tbody>
			<tr><th scope="row">${rc.getMessage("occ.kingdom")}</th><td>${root.occModel.kingdom?if_exists}</td></tr>
			<tr><th scope="row">${rc.getMessage("occ.phylum")}</th><td>${root.occModel.phylum?if_exists}</td></tr>
			<tr><th scope="row">${rc.getMessage("occ._class")}</th><td>${root.occModel._class?if_exists}</td></tr>
			<tr><th scope="row">${rc.getMessage("occ._order")}</th><td>${root.occModel._order?if_exists}</td></tr>
			<tr><th scope="row">${rc.getMessage("occ.family")}</th><td>${root.occModel.family?if_exists}</td></tr>
			<tr><th scope="row">${rc.getMessage("occ.genus")}</th><td>${root.occModel.genus?if_exists}</td></tr>
			<tr><th scope="row">${rc.getMessage("occ.scientificname")}</th><td>${root.occModel.scientificname?if_exists} <span class="remark">(${rc.getMessage("occpage.remark.interpretedfrom")}: ${root.occRawModel.scientificname?if_exists})</span></td></tr>
			<tr><th scope="row">${rc.getMessage("occ.scientificnameauthorship")}</th><td>${root.occModel.scientificnameauthorship?if_exists}</td></tr>
		</tbody>
		</table>

		<h2>${rc.getMessage("occpage.group.location")}</h2>
		<table class="occpage_group">
		<tbody>
			<tr><th scope="row">${rc.getMessage("occ.country")}</th><td>${root.occModel.country?if_exists}</td></tr>
			<tr><th scope="row">${rc.getMessage("occ.stateprovince")}</th><td>${root.occModel.stateprovince?if_exists}</td></tr>
			<tr><th scope="row">${rc.getMessage("occ.county")}</th><td>${root.occModel.county?if_exists}</td></tr>
			<tr><th scope="row">${rc.getMessage("occ.municipality")}</th><td>${root.occModel.municipality?if_exists}</td></tr>
			<tr><th scope="row">${rc.getMessage("occ.locality")}</th><td>${root.occModel.locality?if_exists}</td></tr>
		</tbody>
		<tbody>
			<tr>
			  <th scope="row">${rc.getMessage("occ.decimallatitude")}</th>
			  <td>${safeNumber(root.occModel.decimallatitude!"","")}</td>
			</tr>
			<tr>
			  <th scope="row">${rc.getMessage("occ.decimallongitude")}</th>
			  <td>${safeNumber(root.occModel.decimallongitude!"","")}</td>
			</tr>
			<tr>
			  <th scope="row">${rc.getMessage("occ.coordinateuncertaintyinmeters")}</th>
			  <td>${root.occRawModel.coordinateuncertaintyinmeters?if_exists}</td>
			</tr>
		</tbody>
		<tbody>	
			<tr>
			  <th scope="row">${rc.getMessage("occ.minimumelevationinmeters")}</th>
			  <td>${root.occModel.minimumelevationinmeters?if_exists}</td>
			</tr>
			<tr>
			  <th scope="row">${rc.getMessage("occ.maximumelevationinmeters")}</th>
			  <td>${root.occModel.maximumelevationinmeters?if_exists}</td>
			</tr>
		<tbody>
		</tbody>
		<tboby>
			<tr>
			  <th scope="row">${rc.getMessage("occ.habitat")}</th>
			  <td>${root.occModel.habitat?if_exists}</td>
			</tr>
		</tbody>
		</table>
		
		<h2>${rc.getMessage("occpage.group.date")}</h2>
		<table class="occpage_group">
		<tbody>
			<tr>
			  <th scope="row">${rc.getMessage("view.preview.daterange")}</th>
			  <td>${formatdate(root.occModel.syear!-1,root.occModel.smonth!-1,root.occModel.sday!-1)}</td>
			</tr>
		</tbody>
		</table>
		
		<h2>${rc.getMessage("occpage.group.specimen")}</h2>
		<table class="occpage_group">
		<tbody>
			<tr>
			  <th scope="row">${rc.getMessage("occ.collectioncode")}</th>
			  <td>${root.occModel.collectioncode?if_exists}</td>
			</tr>
			<tr>
			  <th scope="row">${rc.getMessage("occ.catalognumber")}</th>
			  <td>${root.occModel.catalognumber?if_exists}</td>
			</tr>
			<tr>
			  <th scope="row">${rc.getMessage("occ.recordedby")}</th>
			  <td>${root.occModel.recordedby?if_exists}</td>
			</tr>
			<tr>
			  <th scope="row">${rc.getMessage("occ.recordnumber")}</th>
			  <td>${root.occModel.recordnumber?if_exists}</td>
			</tr>
		</tbody>
		</table>

		<h2>${rc.getMessage("occpage.group.dataset")}</h2>
		<table class="occpage_group">
		<tbody>
			<tr>
			  <th scope="row">${rc.getMessage("occ.institutioncode")}</th>
			  <td>${root.occModel.institutioncode?if_exists}</td>
			</tr>
			<tr>
			  <th scope="row">${rc.getMessage("occ.datasetname")}</th>
			  <td><a href="${root.occRawModel.datasetid?if_exists}">${root.occModel.datasetname?if_exists}</a></td>
			</tr>
			<tr>
			  <th scope="row">${rc.getMessage("occ.rights")}</th>
			  <td>${root.occRawModel.rights?if_exists}</td>
			</tr>
		</tbody>
		<tbody>
			<tr>
			  <th scope="row">${rc.getMessage("occ.sourcefileid")}</th>
			  <td>${root.occModel.sourcefileid?if_exists}</td>
			</tr>
			<tr>
			  <th scope="row">${rc.getMessage("occ.dwcaid")}</th>
			  <td>${root.occModel.dwcaid?if_exists}</td>
			</tr>
			<tr>
			  <th scope="row">${rc.getMessage("occ.basisofrecord")}</th>
			  <td>${root.occRawModel.basisofrecord?if_exists}</td>
			</tr>
			<tr>
			  <th scope="row">${rc.getMessage("occ.modified")}</th>
			  <td>${root.occRawModel.modified?if_exists}</td>
			</tr>
		</tbody>
		</table>
	</div>
</div><#-- body -->
<#assign coordinateuncertaintyinmeters=0>
<#if root.occRawModel.coordinateuncertaintyinmeters?? && (root.occRawModel.coordinateuncertaintyinmeters?length>0)>
<#assign coordinateuncertaintyinmeters=root.occRawModel.coordinateuncertaintyinmeters?number>
</#if>

<#-- JavaScript handling -->
<content tag="local_script">
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="//maps.googleapis.com/maps/api/js?sensor=false"></script>
<@jsLibAsset libName="underscore-min.js"/>
<@jsLibAsset libName="backbone-min.js"/>
<@jsAsset fileName="explorer" version=root.currentVersion! useMinified=root.useMinified/>
<@jsAsset fileName="explorer.portal" version=root.currentVersion! useMinified=root.useMinified/>
<script>
$(function() {
  EXPLORER.details.setupSingleOccurrenceMap('occpage_map',${safeNumber(root.occModel.decimallatitude!"","undefined")},${safeNumber(root.occModel.decimallongitude!"","undefined")},${coordinateuncertaintyinmeters?c});
});
</script>
</content>
