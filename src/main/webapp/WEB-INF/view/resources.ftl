<#ftl strip_whitespace=true>
<#include "inc/functions.ftl">
<#include "inc/paging.ftl">
<#include "inc/global-functions.ftl">
<head>
<title>${rc.getMessage("resourcespage.title")}</title>
<@cssAsset fileName="occportal" version=page.currentVersion! useMinified=false/>
<@jsLibAsset libName="sorttable.js"/>

</head>
<div id="body">
	<a id="main-content"></a>
	<div class="boxcontent">
		<div id="table_wrapper">
			<div id="content" class="clear_fix no_side_bar">
				<h1>${rc.getMessage("resourcespage.resourcelist", [page.totalResources])}</h1>
				<table id="results" class="sortable">
					<thead>
						<tr>
							<th class="sorttable_alpha" scope="col">${rc.getMessage("resourcespage.resourcename")}</th>
							<th class="sorttable_alpha" scope="col">${rc.getMessage("resourcespage.publishername")}</th>
							<th class="sorttable_numerical" scope="col">${rc.getMessage("resourcespage.amountofrecords")}</th>
						</tr>
					</thead>
					<tbody>
						<#if page.resources?has_content>
							<#list page.resources as resource>
								<#if resource?has_content>
									<#if (resource.getRecord_count()>0)>
										<#assign publisher = resource.getPublisher()>
										<tr>
											<td><a href="${rc.getContextPath()}/${rc.getMessage("resourcepage.resource")}/${resource.getId()}" target"_self">${resource.getName()}</a></td>
											<td><a href="${rc.getContextPath()}/${rc.getMessage("publisherspage.publisherlink")}/${publisher.getAuto_id()}" target"_self">${publisher.getName()}</a></td>
											<td><a href="${rc.getContextPath()}/${rc.getMessage("resourcespage.occurrencelink")}${resource.getName()}" target="_blank">${resource.getRecord_count()}</a></td>
										</tr>
									</#if>
								</#if>			
							</#list>
						</#if>
					</tbody>
				</table>
			   	<#if ((page.totalResources!0) >= page.pageSize)>
			   		<#assign p = (page.currentPage!1)?number>
					<@pages 1..page.totalPages p />
				</#if>
		   	</div>
	   	</div>
	</div>
</div>