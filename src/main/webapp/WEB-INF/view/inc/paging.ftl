<#-- This will come from canadensys-web-core library -->
<#-- URLHelper must be available -->
<#function max x y>
	<#if (x<y)><#return y><#else><#return x></#if>
</#function>

<#function min x y>
	<#if (x<y)><#return x><#else><#return y></#if>
</#function>

<#-- From http://stackoverflow.com/questions/6391668/freemarker-pagination-or-just-general-algorithm-for-clicking-through-pages -->
<#macro pages totalPages p>
	<div class="pagination">

	<#assign size = totalPages?size>
	<#if (p<=5)>
			<#assign interval = 1..(min(5,size))>
	<#elseif ((size-p)<5)>
			<#assign interval = (max(1,(size-4)))..size >
	<#else>
			<#assign interval = (p-2)..(p+2)>
	</#if>
	<#if (p <= 1)>
		<span class="previous_page disabled">«</span>
	<#else>
		<a class="previous_page" href="${URLHelper.getURL(Request,"page",(p-1)?c)}" rel="prev">«</a>
	</#if>
	<#if !(interval?seq_contains(1))>
	 <a href="${URLHelper.getURL(Request,"page","1")}">1</a><span class="gap">...</span><#rt>
	</#if>
	<#list interval as page>
			<#if page=p>
			 <em class="current">${page}</em> <#t>
			<#else>
			 <a href="${URLHelper.getURL(Request,"page",page?c)}">${page}</a> <#t>
			</#if>
	</#list>
	<#if !(interval?seq_contains(size))>
	 <span class="gap">...</span><a href="${URLHelper.getURL(Request,"page",size?c)}">${size}</a><#lt>
	</#if>
	<#if (p == size)>
		<span class="next_page disabled">»</span>
	<#else>
		<a class="next_page" href="${URLHelper.getURL(Request,"page",(p+1)?c)}" rel="next">»</a>
	</#if>

	</div>
</#macro>