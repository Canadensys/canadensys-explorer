<#ftl strip_whitespace=true>
<#include "inc/functions.ftl">
<#include "inc/global-functions.ftl">
<head>
<title>${rc.getMessage("resourcecontact.title",[page.data.resource_name])}</title>
<@cssAsset fileName="occportal" version=page.currentVersion! useMinified=false/>
</head>
<div id="body">
	<a id="main-content"></a>
	<div id="content" class="clear_fix no_side_bar">
		<h1>${page.data.resource_name!}</h1>
		<h2>${rc.getMessage("resourcecontact.header")}</h2>
		<table>
		<tbody>
			<tr><th>${rc.getMessage("resourcecontact.name")}</th><td>${page.data.name!}</td></tr>
			<tr><th>${rc.getMessage("resourcecontact.position")}</th><td>${page.data.position_name!}</td></tr>
			<tr><th>${rc.getMessage("resourcecontact.organization")}</th><td>${page.data.organization_name!}</td></tr>
			<tr><th>${rc.getMessage("resourcecontact.address")}</th><td>${page.data.address!}, ${page.data.city!}, ${page.data.administrative_area!}, ${page.data.postal_code!}, ${page.data.country!}</td></tr>
			<tr><th>${rc.getMessage("resourcecontact.email")}</th><td>
			<#if page.data.email?has_content>
			<a href="mailto:${page.data.email}">${page.data.email}</a>
			</#if>
			</td></tr>
			<tr><th>${rc.getMessage("resourcecontact.telephone")}</th><td>${page.data.phone!}</td></tr>
		</tbody>
		</table>
	</div>
</div><#-- body -->
