<link rel="stylesheet" href="http://data.canadensys.net/common/styles/common-1.0.min.css" media="screen,print"/>
<link rel="shortcut icon" href="http://data.canadensys.net/common/images/favicon.png"/>

<#if gaAccount?? && gaAccount?has_content>
	<meta name="google-site-verification" content="${gaSiteVerification}"/>
	<script>
	var _gaq = _gaq || [];
	_gaq.push(['_setAccount', '${gaAccount}']);
	_gaq.push(['_trackPageview']);
	(function() {
		var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
		ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
	var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
	})();
	</script>
</#if>