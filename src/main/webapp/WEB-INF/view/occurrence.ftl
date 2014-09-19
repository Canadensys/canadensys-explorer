<#include "inc/functions.ftl">
<#include "inc/global-functions.ftl">
<head>
<title>${rc.getMessage("page.search.title")}</title>
<@cssAsset fileName="occportal" version=page.currentVersion! useMinified=false/>
</head>
<a id="main-content"></a>
<div id="body">
	<div id="side_bar">
		<p><a class="round big_button no_margin" href="../contact">${rc.getMessage("occpage.menu.datasetcontact")}</a></p>
		<#if page.occRawModel._references?has_content >
		<p><a class="round big_button" href="${page.occRawModel._references}">${rc.getMessage("occpage.menu.sourcerecord")}</a></p>
		</#if>
		
		<div id="occpage_map" class="round">
		<#-- Map injected, will remove span element -->
		<span>${rc.getMessage("occpage.nogeo")}</span>
		</div>
		
		<#if page.occViewModel.imageList?has_content>
			<div id="occpage_image">
				<#list page.occViewModel.imageList as currImg>
				<a class="round" href="${currImg}"><span><img src="${currImg}" alt="${page.occModel.scientificname?if_exists} (${page.occModel.collectioncode?if_exists} ${page.occModel.catalognumber?if_exists})"/></span></a>
				</#list>
			</div>
		</#if>
		<#if page.occViewModel.otherMediaList?has_content>
			<div id="occpage_media">
				<#assign mediaNumber = 1>
				<#list page.occViewModel.otherMediaList as currOm>
				<p><a class="round big_button" href="${currOm}" target="_blank">${rc.getMessage("occpage.menu.associatedmedia")} ${mediaNumber}</a></p>
				<#assign mediaNumber = mediaNumber + 1>
				</#list>
			</div>
		</#if>
	</div>
	
	<div id="content" class="clear_fix">
		<h1>${page.occModel.scientificname?if_exists} (${page.occModel.collectioncode?if_exists} ${page.occModel.catalognumber?if_exists})</h1>
		<p class="details">${rc.getMessage("occpage.header.details")}: ${page.occModel.sourcefileid?if_exists}/${page.occModel.dwcaid?if_exists}</p>
		<div class="nav_container" id="occpage_navigation">
		<ul class="buttons">
			<li><a href="?view=normal" class="selected">${rc.getMessage("occpage.header.button.normal")}</a></li>
			<li><a href="?view=dwc">${rc.getMessage("occpage.header.button.dwc")}</a></li>
		</ul>
		</div>

		<#if page.occModel.hastypestatus?? && page.occModel.hastypestatus>
			<h2>${rc.getMessage("occpage.group.typestatus")}</h2>
			<p>${page.occModel.typestatus!}</p>
		</#if>

		<h2>${rc.getMessage("occpage.group.classification")}</h2>
		<table class="occpage_group">
		<tbody>
			<tr><th scope="row">${rc.getMessage("occ.kingdom")}</th><td>${page.occModel.kingdom?if_exists}</td></tr>
			<tr><th scope="row">${rc.getMessage("occ.phylum")}</th><td>${page.occModel.phylum?if_exists}</td></tr>
			<tr><th scope="row">${rc.getMessage("occ._class")}</th><td>${page.occModel._class?if_exists}</td></tr>
			<tr><th scope="row">${rc.getMessage("occ._order")}</th><td>${page.occModel._order?if_exists}</td></tr>
			<tr><th scope="row">${rc.getMessage("occ.family")}</th><td>${page.occModel.family?if_exists}</td></tr>
			<tr><th scope="row">${rc.getMessage("occ.genus")}</th><td>${page.occModel.genus?if_exists}</td></tr>
			<tr><th scope="row">${rc.getMessage("occ.scientificname")}</th><td>${page.occModel.scientificname?if_exists} <span class="remark">(${rc.getMessage("occpage.remark.interpretedfrom")}: ${page.occRawModel.scientificname?if_exists})</span></td></tr>
			<tr><th scope="row">${rc.getMessage("occ.scientificnameauthorship")}</th><td>${page.occModel.scientificnameauthorship?if_exists}</td></tr>
		</tbody>
		</table>
		
		<#if page.occViewModel.multimediaViewModelList?has_content>
		<h2>${rc.getMessage("occpage.group.multimedia")}</h2>
		<div id="occpage_image">
		<ul>
		<#list page.occViewModel.multimediaViewModelList as currMultimediaViewModel>
			<li><a href="${currMultimediaViewModel.references!}"><img src="${currMultimediaViewModel.identifier!}"/></a>
			<div>
			<#if currMultimediaViewModel.licenseShortname?has_content>
				<a href="${currMultimediaViewModel.license}"><img src="${rc.getContextUrl("/assets/images/"+currMultimediaViewModel.licenseShortname+".png")}"/></a>
			<#else>
				<@printIfNotEmpty text=rc.getMessage("occ.multimedia.license")+": " variable=currMultimediaViewModel.license/>
			</#if>
			<@printIfNotEmpty text=rc.getMessage("occ.multimedia.creator")+": " variable=currMultimediaViewModel.creator/>
			</div>
			</li>
		</#list>
		</ul>
		</div>
		</#if>

		<h2>${rc.getMessage("occpage.group.location")}</h2>
		<table class="occpage_group">
		<tbody>
			<tr><th scope="row">${rc.getMessage("occ.country")}</th><td>${page.occModel.country?if_exists}</td></tr>
			<tr><th scope="row">${rc.getMessage("occ.stateprovince")}</th><td>${page.occModel.stateprovince?if_exists}</td></tr>
			<tr><th scope="row">${rc.getMessage("occ.county")}</th><td>${page.occModel.county?if_exists}</td></tr>
			<tr><th scope="row">${rc.getMessage("occ.municipality")}</th><td>${page.occModel.municipality?if_exists}</td></tr>
			<tr><th scope="row">${rc.getMessage("occ.locality")}</th><td>${page.occModel.locality?if_exists}</td></tr>
		</tbody>
		<tbody>
			<tr>
			  <th scope="row">${rc.getMessage("occ.decimallatitude")}</th>
			  <td>${safeNumber(page.occModel.decimallatitude!"","")}</td>
			</tr>
			<tr>
			  <th scope="row">${rc.getMessage("occ.decimallongitude")}</th>
			  <td>${safeNumber(page.occModel.decimallongitude!"","")}</td>
			</tr>
			<tr>
			  <th scope="row">${rc.getMessage("occ.coordinateuncertaintyinmeters")}</th>
			  <td>${page.occRawModel.coordinateuncertaintyinmeters?if_exists}</td>
			</tr>
		</tbody>
		<tbody>	
			<tr>
			  <th scope="row">${rc.getMessage("occ.minimumelevationinmeters")}</th>
			  <td>${page.occModel.minimumelevationinmeters?if_exists}</td>
			</tr>
			<tr>
			  <th scope="row">${rc.getMessage("occ.maximumelevationinmeters")}</th>
			  <td>${page.occModel.maximumelevationinmeters?if_exists}</td>
			</tr>
		<tbody>
		</tbody>
		<tboby>
			<tr>
			  <th scope="row">${rc.getMessage("occ.habitat")}</th>
			  <td>${page.occModel.habitat?if_exists}</td>
			</tr>
		</tbody>
		</table>
		
		<h2>${rc.getMessage("occpage.group.date")}</h2>
		<table class="occpage_group">
		<tbody>
			<tr>
			  <th scope="row">${rc.getMessage("view.preview.daterange")}</th>
			  <td>${formatdate(page.occModel.syear!-1,page.occModel.smonth!-1,page.occModel.sday!-1)}</td>
			</tr>
		</tbody>
		</table>
		
		<h2>${rc.getMessage("occpage.group.specimen")}</h2>
		<table class="occpage_group">
		<tbody>
			<tr>
			  <th scope="row">${rc.getMessage("occ.collectioncode")}</th>
			  <td>${page.occModel.collectioncode!}</td>
			</tr>
			<tr>
			  <th scope="row">${rc.getMessage("occ.catalognumber")}</th>
			  <td>${page.occModel.catalognumber!}</td>
			</tr>
			<tr>
			  <th scope="row">${rc.getMessage("occ.recordedby")}</th>
			  <td>${page.occModel.recordedby!}</td>
			</tr>
			<tr>
			  <th scope="row">${rc.getMessage("occ.recordnumber")}</th>
			  <td>${page.occModel.recordnumber!}</td>
			</tr>
		</tbody>
		</table>
		
		<#-- associatedSequences, optional section -->
		<#if page.occViewModel.associatedSequencesPerProviderMap?has_content>
		<h2>${rc.getMessage("occpage.group.associatedsequences")}</h2>
		<table class="occpage_group">
		<tbody>
			<#list page.occViewModel.associatedSequencesPerProviderMap?keys as sequenceProvider>
			  <#list page.occViewModel.associatedSequencesPerProviderMap[sequenceProvider] as associatedSequence>
			  	<tr>
			  		<#-- dont't repeat header text -->
			  		<th scope="row"><#if associatedSequence_index == 0>${sequenceProvider}</#if></th>
					<td><@hrefIfNotEmpty text=associatedSequence.getLeft() link=associatedSequence.getRight()/></td>
				</tr>
			  </#list>
			</#list>
		</tbody>
		</table>
		</#if>
		<h2>${rc.getMessage("occpage.group.record")}</h2>
		<table class="occpage_group">
		<tbody>
			<tr>
			  <th scope="row">${rc.getMessage("occ.dwcaid")}</th>
			  <td>${page.occModel.dwcaid?if_exists}</td>
			</tr>
			<tr>
			  <th scope="row">${rc.getMessage("occ.basisofrecord")}</th>
			  <td>${page.occRawModel.basisofrecord?if_exists}</td>
			</tr>
			<tr>
			  <th scope="row">${rc.getMessage("occ.modified")}</th>
			  <td>${page.occRawModel.modified?if_exists}</td>
			</tr>
		</tbody>
		</table>

		<h2>${rc.getMessage("occpage.group.dataset")}</h2>
		<table class="occpage_group">
		<tbody>
			<tr>
			  <th scope="row">${rc.getMessage("occ.institutioncode")}</th>
			  <td>${page.occModel.institutioncode?if_exists}</td>
			</tr>
			<tr>
			  <th scope="row">${rc.getMessage("occ.datasetname")}</th>
			  <td>${page.occModel.datasetname!}</td>
			</tr>
			<tr>
			  <th scope="row">${rc.getMessage("occ.rights")}</th>
			  <td>${page.occRawModel.rights?if_exists}</td>
			</tr>
		</tbody>
		<tbody>
			<tr>
			  <th scope="row">${rc.getMessage("occpage.sourcefile")}</th>
			  <td><@hrefIfNotEmpty text=page.occModel.sourcefileid! link=page.occViewModel.dataSourcePageURL!/></td>
			</tr>
		</tbody>
		</table>
	</div>
</div><#-- body -->
<#assign coordinateuncertaintyinmeters=0>
<#if page.occRawModel.coordinateuncertaintyinmeters?? && (page.occRawModel.coordinateuncertaintyinmeters?length>0)>
<#assign coordinateuncertaintyinmeters=page.occRawModel.coordinateuncertaintyinmeters?number>
</#if>

<#-- JavaScript handling -->
<content tag="local_script">
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="//maps.googleapis.com/maps/api/js?sensor=false"></script>
<@jsAsset fileName="explorer" version=page.currentVersion! useMinified=page.useMinified/>
<@jsAsset fileName="explorer.portal" version=page.currentVersion! useMinified=page.useMinified/>
<script>
$(function() {
  EXPLORER.details.setupSingleOccurrenceMap('occpage_map',${safeNumber(page.occModel.decimallatitude!"","undefined")},${safeNumber(page.occModel.decimallongitude!"","undefined")},${coordinateuncertaintyinmeters?c});
});
</script>
</content>
