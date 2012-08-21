<!DOCTYPE html>
<html>
<body>
	<style type="text/css">
	a { text-decoration: none; }
	a:hover { text-decoration: underline; }
	a:visited { color: #6a0d00; }
	</style>
	<div style="color: #22241e; font-family: 'Lucida Grande', Arial, Sans-Serif; font-size: 14px; line-height: 1.5em; padding: 18px 36px">
		<p>The data you requested via the <a href="http://data.canadensys.net/explorer" style="color: #a82400;">Canadensys explorer</a> on ${requestTimestampText} are now available for download:</p>
		<a href="${dwcaLink}" style="background: #e7e7e7 url('http://data.canadensys.net/common/images/button-dwca.png') top right no-repeat; ; border: 1px solid #a8a7a5; border-radius: 5px; color: #a82400; display: block; height: 48px; font-size: 15px; line-height: 48px; margin: auto; padding: 0 63px 0 15px; text-decoration: none; width: 240px">Download data</a></p>
		<p>We will keep this data package on our servers for one week. To revisit your query, <a href="${requestURL}" style="color: #a82400;">click here</a>.</p>
		<p>The data are packaged as a <strong>Darwin Core archive</strong>, which is a standardized zip file containing the data as a tab-delimited text and the metadata as xml. To learn more about Darwin Core, visit <a href="http://www.canadensys.net/darwin-core" style="color: #a82400;">our introduction page</a> or any of <a href="http://www.canadensys.net/category/darwin-core" style="color: #a82400;">our related blog posts</a>.</p>
		<p>Legally, there are almost no restrictions in how you can use these data (recorded in the <em><a href="http://rs.tdwg.org/dwc/terms/index.htm#dcterms:rights" style="color: #a82400;">rights</a></em> field of every record), as most Canadensys participants have dedicated their data to the <a href="http://creativecommons.org/publicdomain/zero/1.0/" style="color: #a82400;">public domain (CC0)</a>. We do request, however, that you follow the <a href="http://www.canadensys.net/norms" style="color: #a82400;">Canadensys norms</a>, which includes giving credit where credit is due. A suggested citation for the dataset above is:</p>
		<p><code>${institutionCodeList}. <a href="http://data.canadensys.net/explorer" style="color: #a82400;">http://data.canadensys.net/explorer</a> (accessed on ${requestTimestamp?date?iso_utc})</code></p>
		<p>For the citation of more specific datasets (e.g. non-aggregated dataset, single specimen), see the <a href="http://www.canadensys.net/norms" style="color: #a82400;">Canadensys norms</a>.</p>
		<p>Thanks,</p>
		<p>The <a href="http://www.canadensys.net/about/people" style="color: #a82400;">Canadensys team</a></p>
		<img src="http://data.canadensys.net/common/images/canadensys-logo-60.png" alt="Canadensys logo">
	</div>
</body>
</html>