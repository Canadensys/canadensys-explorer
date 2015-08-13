<#include "inc/functions.ftl">
<#include "inc/global-functions.ftl">
<head>
<meta property="og:title" content="${rc.getMessage("page.search.title")}: <@defaultIfEmpty text=page.occModel.scientificname! defaulttext=rc.getMessage("occpage.scientificnamenotprovided")/>" />
<meta property="og:locale" content="${rc.getLocale().getLanguage()}" />
<meta property="og:type" content="website" />
<title>${rc.getMessage("page.search.title")}: <@defaultIfEmpty text=page.occModel.scientificname! defaulttext=rc.getMessage("occpage.scientificnamenotprovided")/></title>
<@cssAsset fileName="occportal" version=page.currentVersion! useMinified=false/>
</head>
<a id="main-content"></a>
<div id="body">
	<div id="content" class="clear_fix no_side_bar">
		<h1><@defaultIfEmpty text=page.occModel.scientificname! defaulttext=rc.getMessage("occpage.scientificnamenotprovided")/></h1>
		<p class="details">
		  <@printIfNotEmpty text=rc.getMessage("occpage.header.details")+": "+(page.occModel.collectioncode!)+ " " variable=page.occModel.catalognumber/>
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
		<table class="occpage_group" vocab="http://rs.tdwg.org/dwc/terms/" typeof="Taxon">
		<tbody>
			<tr>
				<th scope="row">${rc.getMessage("occ.kingdom")}</th>
				<td property="kingdom">${page.occModel.kingdom?if_exists}</td>
			</tr>
			<tr>
				<th scope="row">${rc.getMessage("occ.phylum")}</th>
				<td property="phylum">${page.occModel.phylum?if_exists}</td>
			</tr>
			<tr>
				<th scope="row">${rc.getMessage("occ._class")}</th>
				<td property="class">${page.occModel._class?if_exists}</td>
			</tr>
			<tr>
				<th scope="row">${rc.getMessage("occ._order")}</th>
				<td property="order">${page.occModel._order?if_exists}</td>
			</tr>
			<tr>
				<th scope="row">${rc.getMessage("occ.family")}</th>
				<td property="family">${page.occModel.family?if_exists}</td>
			</tr>
			<tr>
				<th scope="row">${rc.getMessage("occ.genus")}</th>
				<td property="genus">${page.occModel.genus?if_exists}</td>
			</tr>
			<tr>
				<th scope="row">${rc.getMessage("occ.scientificname")}</th>
				<td>${page.occModel.scientificname!} <#if page.occModel.scientificname?has_content><span class="remark">(${rc.getMessage("occpage.remark.interpretedfrom")}: <span property="scientificName">${page.occRawModel.scientificname?if_exists}</span>)</span></#if></td>
			</tr>
			<tr>
				<th scope="row">${rc.getMessage("occ.scientificnameauthorship")}</th>
				<td property="scientificNameAuthorship">${page.occModel.scientificnameauthorship?if_exists}</td>
			</tr>
		</tbody>
		</table>
		
		<#if page.occViewModel.imageViewModelList?has_content>
		<h2>${rc.getMessage("occpage.group.images")}</h2>
		<div id="occpage_image">
		<ul class="clear_fix">
		<#list page.occViewModel.imageViewModelList as currMultimediaViewModel>
			<li>
				<div vocab="http://schema.org/" typeof="ImageObject">
					<a href="${currMultimediaViewModel.references!}" class="media" property="isBasedOnUrl"><img src="${currMultimediaViewModel.identifier!}" title="${currMultimediaViewModel.title!}" alt="${currMultimediaViewModel.title!}" property="contentUrl"/></a>
					<@licenseDiv license=currMultimediaViewModel.license! licenseShortname=currMultimediaViewModel.licenseShortname! creator=currMultimediaViewModel.creator!/>
				</div>
			</li>
		</#list>
		</ul>
		</div>
		</#if>

		<#if page.occViewModel.otherMediaViewModelList?has_content>
		<h2>${rc.getMessage("occpage.group.multimedia")}</h2>
			<ul>
				<#list page.occViewModel.otherMediaViewModelList as currOm>
				<li>
					<a href="${currOm.references!}">${currOm.title!}</a>
					<@licenseDiv license=currOm.license! licenseShortname=currOm.licenseShortname! creator=currOm.creator!/>
				</li>
				</#list>
			</ul>
		</#if>

		<h2>${rc.getMessage("occpage.group.location")}</h2>
		<div id="occpage_location" class="clear_fix">
			<table class="occpage_group" vocab="http://rs.tdwg.org/dwc/terms/" typeof="Location">
			<tbody>
				<tr>
					<th scope="row">${rc.getMessage("occ.country")}</th>
					<td property="country">${page.occModel.country?if_exists}</td>
				</tr>
				<tr>
					<th scope="row">${rc.getMessage("occ.stateprovince")}</th>
					<td property="stateProvince">${page.occModel.stateprovince?if_exists}</td>
				</tr>
				<tr>
					<th scope="row">${rc.getMessage("occ.county")}</th>
					<td property="county">${page.occModel.county?if_exists}</td>
				</tr>
				<tr>
					<th scope="row">${rc.getMessage("occ.municipality")}</th>
					<td property="municipality">${page.occModel.municipality?if_exists}</td>
				</tr>
				<tr>
					<th scope="row">${rc.getMessage("occ.locality")}</th>
					<td property="locality">${page.occModel.locality?if_exists}</td>
				</tr>
			</tbody>
			<tbody>
				<tr>
					<th scope="row">${rc.getMessage("occ.decimallatitude")}</th>
					<td property="decimalLatitude">${safeNumber(page.occModel.decimallatitude!)}</td>
				</tr>
				<tr>
					<th scope="row">${rc.getMessage("occ.decimallongitude")}</th>
					<td property="decimalLongitude">${safeNumber(page.occModel.decimallongitude!)}</td>
				</tr>
				<tr>
					<th scope="row">${rc.getMessage("occ.coordinateuncertaintyinmeters")}</th>
					<td property="coordinateUncertaintyInMeters">${page.occRawModel.coordinateuncertaintyinmeters?if_exists}</td>
				</tr>
			</tbody>
			<tbody>
				<tr>
					<th scope="row">${rc.getMessage("occ.minimumelevationinmeters")}</th>
					<td property="minimumElevationInMeters">${page.occModel.minimumelevationinmeters?if_exists}</td>
				</tr>
				<tr>
					<th scope="row">${rc.getMessage("occ.maximumelevationinmeters")}</th>
					<td property="maximumElevationInMeters">${page.occModel.maximumelevationinmeters?if_exists}</td>
				</tr>
			<tbody>
			</tbody>
			<tbody>
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
		<table class="occpage_group" vocab="http://rs.tdwg.org/dwc/terms/" typeof="Occurrence">
		<tbody>
			<tr>
				<th scope="row">${rc.getMessage("occ.collectioncode")}</th>
				<td>${page.occModel.collectioncode!}</td>
			</tr>
			<tr>
				<th scope="row">${rc.getMessage("occ.catalognumber")}</th>
				<td property="catalogNumber">${page.occModel.catalognumber!}</td>
			</tr>
			<tr>
				<th scope="row">${rc.getMessage("occ.othercatalognumbers")}</th>
				<td property="catalogNumber">${page.occModel.othercatalognumbers!}</td>
			</tr>
			<tr>
				<th scope="row">${rc.getMessage("occ.recordedby")}</th>
				<td property="recordedBy">${page.occModel.recordedby!}</td>
			</tr>
			<tr>
				<th scope="row">${rc.getMessage("occ.recordnumber")}</th>
				<td property="recordNumber">${page.occModel.recordnumber!}</td>
			</tr>
		</tbody>
		</table>
		
		<#-- associatedSequences, optional section -->
		<#if page.occViewModel.associatedSequences?has_content>
		<h2>${rc.getMessage("occpage.group.associatedsequences")}</h2>
		
		<ul class="clear_fix">
		<#list page.occViewModel.associatedSequences as associatedSequence>
			<li><@hrefIfUrl text=associatedSequence /></li>
		</#list>
		</ul>
		</#if>

		<h2>${rc.getMessage("occpage.group.record")}</h2>
		<table class="occpage_group">
		<tbody>
			<tr>
				<th scope="row">${rc.getMessage("occ.dwcaid")}</th>
				<td>${page.occModel.dwcaid!}</td>
			</tr>
			<tr>
				<th scope="row">${rc.getMessage("occ.occurrenceid")}</th>
				<td>${page.occModel.occurrenceid!}</td>
			</tr>
			<tr>
				<th scope="row">${rc.getMessage("occ.basisofrecord")}</th>
				<td>${page.occRawModel.basisofrecord!}</td>
			</tr>
			<tr>
				<th scope="row">${rc.getMessage("occpage.menu.sourcerecord")}</th>
				<td><@hrefIfNotEmpty text=page.occRawModel._references! link=page.occRawModel._references!/></td>
			</tr>
			<tr>
				<th scope="row">${rc.getMessage("occ.modified")}</th>
				<td>${page.occRawModel.modified!}</td>
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
		
		<h2>${rc.getMessage("occpage.group.contact")}</h2>
		<#if page.contactModel?has_content>
		<table vocab="http://schema.org/" typeof="Person">
		<tbody>
			<tr>
				<th>${rc.getMessage("resourcecontact.name")}</th>
				<td property="name">${page.contactModel.name!}</td>
			</tr>
			<tr>
				<th>${rc.getMessage("resourcecontact.position")}</th>
				<td property="jobTitle">${page.contactModel.position_name!}</td>
			</tr>
			<tr>
				<th>${rc.getMessage("resourcecontact.organization")}</th>
				<td property="memberOf" typeof="http://schema.org/Organization"><span property="name">${page.contactModel.organization_name!}</span></td>
			</tr>
			<tr>
				<th>${rc.getMessage("resourcecontact.address")}</th>
				<td property="address">${page.contactModel.address!}, ${page.contactModel.city!}, ${page.contactModel.administrative_area!}, ${page.contactModel.postal_code!}, ${page.contactModel.country!}</td>
			</tr>
			<tr>
				<th>${rc.getMessage("resourcecontact.email")}</th>
				<td>
					<#if page.contactModel.email?has_content>
						<span property="email"><a href="mailto:${page.contactModel.email}">${page.contactModel.email}</a></span>
					</#if>
				</td>
			</tr>
			<tr>
				<th>${rc.getMessage("resourcecontact.telephone")}</th>
				<td property="telephone">${page.contactModel.phone!}</td>
			</tr>
		</tbody>
		</table>
		</#if>
		
		<#if page.occViewModel.recommendedCitation?has_content>
		<h2>${rc.getMessage("occpage.group.citation")}</h2>
		<p>${page.occViewModel.recommendedCitation!}</p>
		</#if>
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
