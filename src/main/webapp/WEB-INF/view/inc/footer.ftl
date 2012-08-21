<div id="footer">
	<div id="footer_content">
		<div class="footer_section left">
			<a id="cc0" class="cc_logo" href="${ltext("footer.cc0.url")}" title="${ltext("footer.cc0.title")}">&nbsp;</a>
			<p>${ltext("footer.cc0.description")}</p>
			<ul>
				<li><a href="${ltext("footer.norms.url")}">${ltext("footer.norms")}</a></li>
				<li><a href="${ltext("footer.ipt.url")}">${ltext("footer.ipt")}</a></li>
			</ul>
		</div>
		<div class="footer_section right">
			<a rel="license" id="cc_by" class="cc_logo" href="${ltext("footer.ccby.url")}" title="${ltext("footer.ccby.title")}">&nbsp;</a>
			<p>${ltext("footer.ccby.description")}</p>
			<ul>
				<li><a href="${ltext("footer.about.url")}">${ltext("footer.about")}</a></li>
				<li><a href="${ltext("footer.contact.url")}">${ltext("footer.contact")}</a></li>
			</ul>
		</div>
	</div>
</div>

<!-- JavaScript handling -->
<#if javaScriptIncludeList??>
	<#list javaScriptIncludeList as jsFile>
	<script type="text/javascript" src="${jsFile}"></script>
	</#list>
</#if>

<#if javaScriptSetupCallList??>
	<script type="text/javascript">
		<#if nojQueryJSSetupCallList??>
			<#list nojQueryJSSetupCallList as jsCall>
			${jsCall};
			</#list>
		</#if>
		$(document).ready(function() {
			<#list javaScriptSetupCallList as jsCall>
			${jsCall};
			</#list>
		});
	</script>
</#if>