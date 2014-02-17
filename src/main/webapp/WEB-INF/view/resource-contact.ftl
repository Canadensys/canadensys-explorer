<#ftl strip_whitespace=true>
<#include "inc/global-functions.ftl">
<head>
<title>${rc.getMessage("resourcecontact.title",[root.data.resource_name])}</title>
<@cssAsset fileName="occportal" version=root.currentVersion! useMinified=false/>
<link rel="dns-prefetch" href="http://tiles.canadensys.net"/>
<link rel="prefetch" href="http://tiles.canadensys.net"/>
</head>

<div id="body">
	<a id="main-content"></a>
	<div id="content" class="clear_fix no_side_bar">
		<h1>${root.data.resource_name!}</h1>
		<h2>${rc.getMessage("resourcecontact.header")}</h2>
		<table>
		<tbody>
			<tr><th>${rc.getMessage("resourcecontact.name")}</th><td>${root.data.name!}</td></tr>
			<tr><th>${rc.getMessage("resourcecontact.position")}</th><td>${root.data.position_name!}</td></tr>
			<tr><th>${rc.getMessage("resourcecontact.organization")}</th><td>${root.data.organization_name!}</td></tr>
			<tr><th>${rc.getMessage("resourcecontact.address")}</th><td>${root.data.address!}, ${root.data.city!}, ${root.data.administrative_area!}, ${root.data.postal_code!}, ${root.data.country!}</td></tr>
			<tr><th>${rc.getMessage("resourcecontact.email")}</th><td>
			<#if root.data.email?has_content>
			<a href="mailto:${root.data.email}">${root.data.email}</a>
			</#if>
			</td></tr>
			<tr><th>${rc.getMessage("resourcecontact.telephone")}</th><td>${root.data.phone!}</td></tr>
		</tbody>
		</table>
	</div>
</div><#-- body -->
