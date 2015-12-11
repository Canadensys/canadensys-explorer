<title>${rc.getMessage("resourcespage.title")}</title>
<div id="body">
  <div class="boxcontent">
	<div id="content" class="clear_fix no_side_bar">
		<#if page.resource?has_content>
			<h1>${page.resource.getName()!}  (<a href="${rc.getContextPath()}/${rc.getMessage("resourcespage.occurrencelink")}${page.resource.getName()}" target="_blank">${page.resource.getRecord_count()} ${rc.getMessage("publisherpage.records")}</a>)</h1>
			<#if page.information?has_content>
				<h2>${rc.getMessage("resourcepage.information")}</h2>
			    <#if page.information.getResource_logo_url()?has_content>
			    	<a href="${page.information.getResource_logo_url()}" target="_blank">
						<img src="${page.information.getResource_logo_url()}" style="float: right; height: 100px">
				    </a>
			    </#if>
			    <#if page.information.getTitle()?has_content>
				   <b>${rc.getMessage("resourcepage.title")}:</b> ${page.information.getTitle()!}</br>
			    </#if>   
			    <#if page.information.get_abstract()?has_content>
			   		<b>${rc.getMessage("resourcepage.abstract")}:</b> ${page.information.get_abstract()!}</br>
			    </#if>  
			    <#if page.information.getCollection_identifier()?has_content>
				   <b>${rc.getMessage("resourcepage.collectionidentifier")}:</b> ${page.information.getCollection_identifier()!}</br>
			    </#if>
			    <#if page.information.getParent_collection_identifier()?has_content>
			   		<b>${rc.getMessage("resourcepage.parentcollectionidentifier")}:</b> ${page.information.getParent_collection_identifier()!}</br>
				</#if>
			    <#if page.information.getCollection_name()?has_content>
					<b>${rc.getMessage("resourcepage.collectionname")}:</b> ${page.information.getCollection_name()!}</br>
			   	</#if>
			   	<#if page.information.getPublication_date()?has_content>
					<b>${rc.getMessage("resourcepage.publicationdate")}:</b> ${page.information.getPublication_date()!}</br>
				</#if>
				<#if page.information.getKeyword()?has_content>
					<b>${rc.getMessage("resourcepage.keywords")}:</b> ${page.information.getKeyword()}</br>
				</#if>
				<#if page.information.getKeyword_thesaurus()?has_content>
				   <b>${rc.getMessage("resourcepage.keywordthesaurus")}:</b> ${page.information.getKeyword_thesaurus()}</br>
			    </#if>
			    <#if page.information.getAlternate_identifier()?has_content>
			    <b>${rc.getMessage("resourcepage.alternateidentifier")}:</b>  ${page.information.getAlternate_identifier()}</br>	    	
			    </#if>
   			    <#if page.resource.getArchive_url()?has_content>
			    <b>${rc.getMessage("resourcespage.archiveurl")}:</b> <a href="${page.resource.getArchive_url()}" target="_blank">${rc.getMessage("resourcepage.dwca")}</a></br>
			    <#assign repository = page.resource.getArchive_url()?replace("archive","resource")>
			    <b>${rc.getMessage("resourcepage.repository")}:</b> <a href="${repository}" target="_blank">${repository}</a>
			    </#if>
		    
			    <h2>${rc.getMessage("resourcepage.licensingandrights")}</h2>
			    <div style=" border: 1px solid #a8a7a5; border-radius: 5px; background-color: #D9ECE0; width: 80%; padding: 10px; margin: auto; text-align: center">
			    	${rc.getMessage("resourcepage.license.description")}
			    </div>
			    </br>
				<b>${rc.getMessage("resourcepage.intellectualrights")}:</b>
			    <#if page.information.getIntellectual_rights()?has_content>
			    	 ${page.information.getIntellectual_rights()!}
				<#else>
					${rc.getMessage("resourcepage.defaultintellectualrights")}
			    </#if>
			    </br>
   			    <b>${rc.getMessage("resourcepage.citation")}:</b>
				<#if page.information.getCitation()?has_content>
		    		${page.information.getCitation()!}
			    <#else>
			    	<#assign link = rc.getMessage("resourcepage.resource")> 
			    	${rc.getMessage("resourcepage.defaultcitation")}:</br></br>
			    	<div style=" border: 1px solid #a8a7a5; border-radius: 5px; background-color: #D9ECE0; width: 80%; padding: 10px; margin: auto; text-align: center">
				    	${page.information.getTitle()!}, ${page.information.getPublication_date()!}.</br>
				    	${rc.getMessage("resourcepage.accessedon")} <a href="${rc.getContextPath()}/${link}/${page.resource.getId()}" target="_blank"> ${domainName}${rc.getContextPath()}/${link}/${page.resource.getId()} </a>, ${rc.getMessage("resourcepage.on")} ${page.currentTime}.
				    </div>	   	
				</#if>
				</br>
			<#else>
			<h2>Sorry, no information found about this dataset.</h2>
			</#if>
		</#if>	
	</div>
		</div>
</div>	