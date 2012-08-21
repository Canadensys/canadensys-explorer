<!DOCTYPE html>
<html>
<body>
	<style type="text/css">
	a { text-decoration: none; }
	a:hover { text-decoration: underline; }
	a:visited { color: #6a0d00; }
	</style>
	<div style="color: #22241e; font-family: 'Lucida Grande', Arial, Sans-Serif; font-size: 14px; line-height: 1.5em; padding: 18px 36px">
		<p>Les donn&eacute;es que vous avez demand&eacute;es via l'<a href="http://data.canadensys.net/explorer" style="color: #a82400;">explorateur Canadensys</a> le ${requestTimestampText} sont maintenant disponibles pour t&eacute;l&eacute;chargement:</p>
		<a href="${dwcaLink}" style="background: #e7e7e7 url('http://data.canadensys.net/common/images/button-dwca.png') top right no-repeat; ; border: 1px solid #a8a7a5; border-radius: 5px; color: #a82400; display: block; height: 48px; font-size: 15px; line-height: 48px; margin: auto; padding: 0 63px 0 15px; text-decoration: none; width: 240px">T&eacute;l&eacute;charger les donn&eacute;es</a></p>
		<p>Vos donn&eacute;es seront conserv&eacute;es sur notre serveur pendant une semaine. Afin de visualiser votre recherche &agrave; nouveau, <a href="${requestURL}" style="color: #a82400;">cliquez ici</a>.</p>
		<p>Les donn&eacute;es sont fournies sous la forme d'une <strong>archive Darwin Core</strong>, un fichier compress&eacute; et standardis&eacute; comprenant les donn&eacute;es en format texte, d&eacute;limit&eacute; par des tabulations, et les m&eacute;tadonn&eacute;es en format xml. Pour de plus amples informations sur Darwin Core, consultez <a href="http://www.canadensys.net/publication/darwin-core?lang=fr" style="color: #a82400;">notre page d'introduction</a> ou <a href="http://www.canadensys.net/category/darwin-core?lang=fr" style="color: #a82400;">nos articles de blog relatifs &agrave; ce sujet</a>.</p>
		<p>Il n'existe l&eacute;galement pas de restriction sur l'utilisation que vous ferez de ces donn&eacute;es (tel que mentionn&eacute; dans le champ <em><a href="http://rs.tdwg.org/dwc/terms/index.htm#dcterms:rights" style="color: #a82400;">droits</a></em> de tous les enregistrements), puisque la majorit&eacute; des participants de Canadensys ont transf&eacute;r&eacute; leur donn&eacute;es au <a href="http://creativecommons.org/publicdomain/zero/1.0/deed.fr" style="color: #a82400;">domaine public (CC0)</a>. Nous vous demandons n&eacute;anmoins de vous conformer aux <a href="http://www.canadensys.net/norms" style="color: #a82400;">normes de Canadensys</a> et en particulier de citer la source appropri&eacute;e lorsque cela est n&eacute;cessaire. Pour citer de tels jeux de donn&eacute;es combin&eacute;es, nous vous sugg&eacute;rons la formulation suivante : </p>
		<p><code>${institutionCodeList}. <a href="http://data.canadensys.net/explorer" style="color: #a82400;">http://data.canadensys.net/explorer</a> (visit&eacute; le  ${requestTimestamp?date?iso_utc})</code></p>
		<p>Si vous voulez citer un sp&eacute;cimen particulier ou un jeu de donn&eacute;es non combin&eacute;, veuillez vous r&eacute;f&eacute;rer aux <a href="http://www.canadensys.net/norms" style="color: #a82400;">normes de Canadensys</a>.</p>
		<p>Merci,</p>
		<p>l'<a href="http://www.canadensys.net/about/people" style="color: #a82400;">&eacute;quipe Canadensys</a></p>
		<img src="http://data.canadensys.net/common/images/canadensys-logo-60.png" alt="Canadensys logo">
	</div>
</body>
</html>