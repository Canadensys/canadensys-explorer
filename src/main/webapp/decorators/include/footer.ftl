<div id="footer">
	<div id="footer_content">
		<div class="footer_section left">
			<a id="cc0" class="cc_logo" href="${rc.getMessage("viewinclude.footer.cc0.url")}" title="${rc.getMessage("viewinclude.footer.cc0.title")}">&nbsp;</a>
			<p>${rc.getMessage("viewinclude.footer.cc0.description")}</p>
			<ul>
				<li><a href="${rc.getMessage("viewinclude.footer.norms.url")}">${rc.getMessage("viewinclude.footer.norms")}</a></li>
				<li><a href="${rc.getMessage("viewinclude.footer.ipt.url")}">${rc.getMessage("viewinclude.footer.ipt")}</a></li>
				<#if footerAdditionalInfoKey??>
					<#if footerAdditionalInfoParamKey??>
					<#attempt>
				<li>${rc.getMessage(footerAdditionalInfoKey,[footerAdditionalInfoParamKey?eval])}</li>
					<#recover>
					</#attempt>
					<#else>
				<li>${rc.getMessage(footerAdditionalInfoKey)}</li>
					</#if>
				</#if>
			</ul>
		</div>
		<div class="footer_section right">
			<a rel="license" id="cc_by" class="cc_logo" href="${rc.getMessage("viewinclude.footer.ccby.url")}" title="${rc.getMessage("viewinclude.footer.ccby.title")}">&nbsp;</a>
			<p>${rc.getMessage("viewinclude.footer.ccby.description")}</p>
			<ul>
				<li><a href="${rc.getMessage("viewinclude.footer.about.url")}">${rc.getMessage("viewinclude.footer.about")}</a></li>
				<li><a href="${rc.getMessage("viewinclude.footer.contact.url")}">${rc.getMessage("viewinclude.footer.contact")}</a></li>
			</ul>
		</div>
	</div>
</div>