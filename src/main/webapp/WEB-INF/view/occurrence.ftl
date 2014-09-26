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
		  <@printIfNotEmpty text=rc.getMessage("occpage.header.details")+": "+page.occModel.collectioncode+ " " variable=page.occModel.catalognumber/>
		</p>

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
		
		<#if page.occViewModel.imageViewModelList?has_content>
		<h2>${rc.getMessage("occpage.group.multimedia")}</h2>
		<div id="occpage_image">
		<ul class="clear_fix">
		<#list page.occViewModel.imageViewModelList as currMultimediaViewModel>
			<li>
				<div>
					<a href="${currMultimediaViewModel.references!}" class="media"><img src="${currMultimediaViewModel.identifier!}"/></a>
					<div>
						<#if currMultimediaViewModel.licenseShortname?has_content>
							<a href="${currMultimediaViewModel.license}">
								<img src="${rc.getContextUrl("/assets/images/"+currMultimediaViewModel.licenseShortname+".png")}" title="${currMultimediaViewModel.title!}" alt="${currMultimediaViewModel.title!}" />
							</a>
						<#else>
							<@printIfNotEmpty text=rc.getMessage("occ.multimedia.license")+": " variable=currMultimediaViewModel.license/>
						</#if>
						<@printIfNotEmpty text=rc.getMessage("occ.multimedia.creator")+": " variable=currMultimediaViewModel.creator/>
					</div>
				</div>
			</li>
		</#list>
		</ul>
		</div>
		</#if>

<#-- TODO: adjust the styling here -->
		<#if page.occViewModel.otherMediaViewModelList?has_content>
		<h2>${rc.getMessage("occpage.group.associatedmultimedia")}</h2>
			<ul>
			    <#-- TODO: display the title here and let the controller fill it in case it's missing -->
				<#assign mediaNumber = 1>
				<#list page.occViewModel.otherMediaViewModelList as currOm>
				<li>
					<a href="${currOm.references}">${rc.getMessage("occpage.menu.associatedmedia")} ${mediaNumber}</a>
				</li>
				<#assign mediaNumber = mediaNumber + 1>
				</#list>
			</ul>
		</#if>

		<h2>${rc.getMessage("occpage.group.location")}</h2>
		<div id="occpage_location" class="clear_fix">
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

			<div id="occpage_map" class="round">
				<#-- Map injected, will remove span element -->
				<span>${rc.getMessage("occpage.nogeo")}</span>
			</div>
		</div>

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
				<th scope="row">${rc.getMessage("occpage.menu.sourcerecord")}</th>
				<td><@hrefIfNotEmpty text=page.occRawModel._references! link=page.occRawModel._references!/></td>
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
		
<#-- <h2>${rc.getMessage("occpage.group.contact")}</h2> -->
<#-- TODO: need to get content from ../contact and dump here -->
		
		<h2>${rc.getMessage("occpage.group.citation")}</h2>
		<p>${page.occViewModel.recommendedCitation!}</p>
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
