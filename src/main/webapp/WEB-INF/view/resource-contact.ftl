<#assign page={"title":rc.getMessage("resourcecontact.title",[root.data.resource_name]),"cssList":[rc.getContextUrl("/styles/occportal.css")],"prefetchList":["http://tiles.canadensys.net"]}>
<#include "inc/header.ftl">
<div id="feedback_bar"><a href="http://code.google.com/p/canadensys/issues/entry?template=Explorer%20-%20Interface%20issue" target="_blank" title="${rc.getMessage("feedback.hover")}">&nbsp;</a></div>
	<#include "inc/canadensys-header.ftl">
	<div id="body">
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
<#include "inc/footer.ftl">