
-- dwca_resource
INSERT INTO dwca_resource(id, sourcefileid,gbif_package_id,name,archive_url)
  VALUES (1,'acad-specimens','db4f9560-9cca-11e4-89d3-123b93f75cba','E. C. Smith Herbarium (ACAD)','http://data.canadensys.net/ipt/archive.do?r=acad-specimens');
INSERT INTO dwca_resource(id, sourcefileid,gbif_package_id,name,archive_url)
  VALUES (2,'trt-specimens','f8d222ba-9cca-11e4-89d3-123b93f75cba','TRT','http://data.canadensys.net/ipt/archive.do?r=trt-specimens');

-- resource_metadata
INSERT INTO resource_metadata (dwca_resource_id,gbif_package_id)
	VALUES (1,'db4f9560-9cca-11e4-89d3-123b93f75cba');
INSERT INTO resource_metadata (dwca_resource_id,gbif_package_id)
	VALUES (2,'f8d222ba-9cca-11e4-89d3-123b93f75cba');

/* Occurrence with extension */
INSERT INTO occurrence(
auto_id,datasetname,sourcefileid,resource_id,dwca_id,scientificname,taxonrank,country,stateprovince,collectioncode,catalognumber,minimumelevationinmeters,maximumelevationinmeters,syear,habitat,_class,_order,decimallatitude,decimallongitude,hasmedia)
VALUES (
1,'E. C. Smith Herbarium (ACAD)','acad-specimens',1,
'ACAD-1','Acer pseudoplatanus','species','Canada','Nova Scotia','ACAD','ECS019597',100,200,1980,'Under rocks','Magnoliopsida','Sapindales',45.099220,-62.354647,true
);
INSERT INTO occurrence_raw(
auto_id,datasetname,sourcefileid,resource_id,dwca_id,scientificname,taxonrank,country,stateprovince,collectioncode,catalognumber,year,habitat)
VALUES (
1,'E. C. Smith Herbarium (ACAD)','acad-specimens',1,
'ACAD-1','Acer pseudoplatanus','species','Canada','Nova Scotia','ACAD','ECS019597',1980,'Under rocks'
);

/* Add extension data */
INSERT INTO occurrence_extension(auto_id,dwca_id,sourcefileid,resource_id,ext_type,ext_data)
VALUES(
1,'ACAD-1','acad-specimens',1,'Multimedia',
toKeyValue('creator=>Chuck Norris','references=>http://procyon.acadiau.ca/ecsmith/cgi-bin/image.cgi?ECS019597,jpeg','identifier=>http://procyon.acadiau.ca/ecsmith/cgi-bin/image.cgi?ECS019597,jpeg',
'license=>http://creativecommons.org/licenses/by/4.0/','title=>Image title 1','format=>image/png')
),
(
2,'ACAD-1','acad-specimens',1,'Multimedia',
toKeyValue('creator=>JCVD','references=>http://procyon.acadiau.ca/ecsmith/cgi-bin/image.cgi?ECS019597,jpeg','identifier=>http://procyon.acadiau.ca/ecsmith/cgi-bin/image.cgi?ECS019597,jpeg',
'license=>http://creativecommons.org/licenses/unkown','title=>Image title 2')
);

/* Occurrence with associatedMedia */
INSERT INTO occurrence(
auto_id,datasetname,sourcefileid,resource_id,dwca_id,scientificname,taxonrank,country,stateprovince,collectioncode,catalognumber,syear,habitat,associatedmedia)
VALUES (
2,'E. C. Smith Herbarium (ACAD)','acad-specimens',1,
'ACAD-2','Myosotis arvensis','species','Canada','Nova Scotia','ACAD','10440',2013,'On a lobster claw','http://procyon.acadiau.ca/ecsmith/cgi-bin/image.cgi?ECS019597,jpeg; http://procyon.acadiau.ca/ecsmith/cgi-bin/image.cgi?ECS019597.jpeg'
);
INSERT INTO occurrence_raw(
auto_id,datasetname,sourcefileid,resource_id,dwca_id,scientificname,taxonrank,country,stateprovince,collectioncode,catalognumber,year,habitat,associatedmedia)
VALUES (
2,'E. C. Smith Herbarium (ACAD)','acad-specimens',1,
'ACAD-2','Myosotis arvensis','species','Canada','Nova Scotia','ACAD','10440',2013, 'On a lobster claw','http://procyon.acadiau.ca/ecsmith/cgi-bin/image.cgi?ECS019597,jpeg; http://procyon.acadiau.ca/ecsmith/cgi-bin/image.cgi?ECS019597.jpeg'
);

/* dwca_id containing a space character */
INSERT INTO occurrence(
auto_id,datasetname,sourcefileid,resource_id,dwca_id,scientificname,taxonrank,country,stateprovince,collectioncode,catalognumber,syear,habitat)
VALUES (
3,'E. C. Smith Herbarium (ACAD)','acad-specimens',1,
'ACAD 3','Myosotis arvensis','species','Canada','Nova Scotia','ACAD','z10441',2013,'On a lobster claw'
);
INSERT INTO occurrence_raw(
auto_id,datasetname,sourcefileid,resource_id,dwca_id,scientificname,taxonrank,country,stateprovince,collectioncode,catalognumber,year,habitat)
VALUES (
3,'E. C. Smith Herbarium (ACAD)','acad-specimens',1,
'ACAD 3','Myosotis arvensis','species','Canada','Nova Scotia','ACAD','z10441',2013, 'On a lobster claw'
);

INSERT INTO occurrence(
auto_id,datasetname,sourcefileid,resource_id,dwca_id,scientificname,taxonrank,country,stateprovince,collectioncode,catalognumber,habitat,_class,_order,decimallatitude,decimallongitude,associatedSequences,hasassociatedsequences)
VALUES (
4,'TRT (TRT)','trt-specimens',1,
'TRT-1','Acer pseudoplatanus','species','Canada','Nova Scotia','TRT','ECS019597','Under rocks','Magnoliopsida','Sapindales',45.099220,-62.354647,
'GenBank:KC251652|BOLD:TADCR103-10.rbcLa|GenBank:KC25165x|TestBank:anId',true
);
INSERT INTO occurrence_raw(
auto_id,datasetname,sourcefileid,resource_id,dwca_id,scientificname,taxonrank,country,stateprovince,collectioncode,catalognumber,habitat,_class,_order,decimallatitude,decimallongitude,associatedSequences)
VALUES (
4,'TRT (TRT)','trt-specimens',1,
'TRT-1','Acer pseudoplatanus','species','Canada','Nova Scotia','ACAD','ECS019597','Under rocks','Magnoliopsida','Sapindales',45.099220,-62.354647,
'GenBank:KC251652|BOLD:TADCR103-10.rbcLa|GenBank:KC25165x|TestBank:anId'
);

/* record with empty scientificname*/
INSERT INTO occurrence(
auto_id,datasetname,sourcefileid,resource_id,dwca_id,occurrenceid,scientificname,taxonrank,country,stateprovince,collectioncode,catalognumber,habitat,_class,_order,decimallatitude,decimallongitude)
VALUES (
5,'TRT (TRT)','trt-specimens',1,
'TRT-5','5','','species','Canada','Nova Scotia','TRT','ECS019598','Under rocks','Magnoliopsida','Sapindales',45.099220,-62.354647
);
INSERT INTO occurrence_raw(
auto_id,datasetname,sourcefileid,resource_id,dwca_id,occurrenceid,scientificname,taxonrank,country,stateprovince,collectioncode,catalognumber,habitat,_class,_order,decimallatitude,decimallongitude)
VALUES (
5,'TRT (TRT)','trt-specimens',1,
'TRT-5','5','','species','Canada','Nova Scotia','TRT','ECS019598','Under rocks','Magnoliopsida','Sapindales',45.099220,-62.354647
);

/* record with dwca_id with a dot(.) */
INSERT INTO occurrence(
auto_id,datasetname,sourcefileid,resource_id,dwca_id,occurrenceid,scientificname,taxonrank,country,stateprovince,collectioncode,catalognumber,habitat,_class,_order,decimallatitude,decimallongitude)
VALUES (
6,'TRT (TRT)','trt-specimens',1,
'TRT.6','6','','species','Canada','Nova Scotia','TRT','ECS019598','Under rocks','Magnoliopsida','Sapindales',45.099220,-62.354647
);
INSERT INTO occurrence_raw(
auto_id,datasetname,sourcefileid,resource_id,dwca_id,occurrenceid,scientificname,taxonrank,country,stateprovince,collectioncode,catalognumber,habitat,_class,_order,decimallatitude,decimallongitude)
VALUES (
6,'TRT (TRT)','trt-specimens',1,
'TRT.6','6','','species','Canada','Nova Scotia','TRT','ECS019598','Under rocks','Magnoliopsida','Sapindales',45.099220,-62.354647
);
	
-- resource_contact
INSERT INTO contact(
            auto_id, resource_metadata_fkey, name, position_name, organization_name, 
            address, city, administrative_area, country, postal_code, email, role)
VALUES (1, 1, 'Greedo', 'Manager', 'Sith', '1212 Death Star Road', 
'Jawas County', 'Dunes', 'Tatooine', 'R1-D2', 'greedo@tatooine.org', 'contact');

-- unique_values
INSERT INTO unique_values(id,key,occurrence_count,value,unaccented_value) VALUES (1,'_class',1,'Magnoliopsida','magnoliopsida');
INSERT INTO unique_values(id,key,occurrence_count,value,unaccented_value) VALUES (2,'_order',1,'Sapindales','sapindales');
INSERT INTO unique_values(id,key,occurrence_count,value,unaccented_value) VALUES (3,'country',125,'Canada','canada');
INSERT INTO unique_values(id,key,occurrence_count,value,unaccented_value) VALUES (4,'country',17,'China','china');
INSERT INTO unique_values(id,key,occurrence_count,value,unaccented_value) VALUES (5,'country',1,'Réunion','reunion');
		
INSERT INTO unique_values (id,key,occurrence_count,value,unaccented_value) VALUES (6,'datasetname',3,'My-Specimens','my-speciens');
INSERT INTO unique_values (id,key,occurrence_count,value,unaccented_value) VALUES (7,'datasetname',40,'Cool-Specimens','cool-speciens');
INSERT INTO unique_values (id,key,occurrence_count,value,unaccented_value) VALUES (8,'datasetname',55,'Cooler-Specimens','cooler-specimens');
