<title>${rc.getMessage("publisherspage.title")}</title>
<div id="body">
	<div id="content" class="clear_fix no_side_bar">
		<#if page.publisher?has_content>
			<h1>${page.publisher.getName()!}</h1>
			<#if page.publisher?has_content>
				<#assign info = page.publisher>
				<h2>${rc.getMessage("publisherpage.information")} (${info.getRecord_count()} ${rc.getMessage("publisherpage.records")})</h2>
			    <#if info.getLogo_url()?has_content>
   				    <#if info.getHomepage()?has_content>
   				    	<#assign link = info.getHomepage()>
   				    <#else>
	   				    <#assign link = info.getLogo_url()>
   				    </#if>
   				    <a href="${link}" target="_blank" >
						<img src="${info.getLogo_url()}" style="padding: 5px; float: right; height: 100px">
				    </a>
			    </#if>
			    <#if info.getDescription()?has_content>
				   <b>${rc.getMessage("publisherpage.description")}:</b> ${info.getDescription()}</br>
			    </#if>
			    <#if info.getHomepage()?has_content>
				   <b>${rc.getMessage("publisherpage.homepage")}:</b> <a href="${info.getHomepage()}" target:'_blank'>${info.getHomepage()}</a></br>
			    </#if>
			    <h2>${rc.getMessage("publisherpage.contactinformation")}</h2>
			    <#if info.getEmail()?has_content>
			   		<b>${rc.getMessage("publisherpage.email")}:</b> ${info.getEmail()}</br>
				</#if>
			    <#if info.getPhone()?has_content>
					<b>${rc.getMessage("publisherpage.phone")}:</b> ${info.getPhone()}</br>
			   	</#if>
			   	<#if info.getAddress()?has_content>
					<b>${rc.getMessage("publisherpage.address")}:</b> ${info.getAddress()}</br>
				</#if>
				<#if info.getCity()?has_content>
					<b>${rc.getMessage("publisherpage.city")}:</b> ${info.getCity()}</br>
				</#if>
				<#if info.getAdministrative_area()?has_content>
				   <b>${rc.getMessage("publisherpage.department")}:</b> ${info.getAdministrative_area()}</br>
			    </#if>
		    	<#if info.getPostal_code()?has_content>
			    	<b>${rc.getMessage("publisherpage.postalcode")}:</b> ${info.getPostal_code()}
			    </#if>
			<#else>
			<h2>Sorry, no information found about this dataset.</h2>
			</#if>
			<!-- Provide list of the available datasets published by this publisher-->
			<h2>${rc.getMessage("publisherpage.resources")}</h2>
			<#list page.publisher.getResources() as resource>
				<#assign count = resource.getRecord_count()?string>
			   	<li> <a href="${rc.getContextPath()}/${rc.getMessage("resourcepage.resource")}/${resource.getId()}" target"_self">${resource.getName()}</a> <a href="${rc.getContextPath()}/${rc.getMessage("resourcespage.occurrencelink")}${resource.getName()}" target="_blank">${rc.getMessage("publisherpage.recordcount", [count])}</a></li>
			</#list>
		</#if>	
	</div>
</div>	
