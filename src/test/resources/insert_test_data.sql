/* Occurrence with extension */
INSERT INTO occurrence(
auto_id,datasetname,sourcefileid,dwcaid,scientificname,taxonrank,country,stateprovince,collectioncode,catalognumber,minimumelevationinmeters,maximumelevationinmeters,syear,habitat,_class,_order,decimallatitude,decimallongitude,hasmedia)
VALUES (
1,'E. C. Smith Herbarium (ACAD)','acad-specimens',
'ACAD-1',
'Acer pseudoplatanus','species','Canada','Nova Scotia','ACAD','ECS019597',100,200,1980,'Under rocks','Magnoliopsida','Sapindales',45.099220,-62.354647,true
);
INSERT INTO occurrence_raw(
auto_id,datasetname,sourcefileid,dwcaid,scientificname,taxonrank,country,stateprovince,collectioncode,catalognumber,year,habitat)
VALUES (
1,'E. C. Smith Herbarium (ACAD)','acad-specimens',
'ACAD-1','Acer pseudoplatanus','species','Canada','Nova Scotia','ACAD','ECS019597',1980,'Under rocks'
);

/* Add extension data */
INSERT INTO occurrence_extension(auto_id,dwcaid,sourcefileid,resource_uuid,ext_type,ext_data)
VALUES(
1,'ACAD-1','acad-specimens','ABDU-NSNS-2836','Multimedia',
toKeyValue('creator=>Chuck Norris','references=>http://procyon.acadiau.ca/ecsmith/cgi-bin/image.cgi?ECS019597,jpeg','identifier=>http://procyon.acadiau.ca/ecsmith/cgi-bin/image.cgi?ECS019597,jpeg',
'license=>http://creativecommons.org/licenses/by/4.0/','title=>Image title 1','format=>image/png')
),
(
2,'ACAD-1','acad-specimens','ABDU-NSNS-2836','Multimedia',
toKeyValue('creator=>JCVD','references=>http://procyon.acadiau.ca/ecsmith/cgi-bin/image.cgi?ECS019597,jpeg','identifier=>http://procyon.acadiau.ca/ecsmith/cgi-bin/image.cgi?ECS019597,jpeg',
'license=>http://creativecommons.org/licenses/unkown','title=>Image title 2')
)
;

/* Occurrence with associatedMedia */
INSERT INTO occurrence(
auto_id,datasetname,sourcefileid,dwcaid,scientificname,taxonrank,country,stateprovince,collectioncode,catalognumber,syear,habitat,associatedmedia)
VALUES (
2,'E. C. Smith Herbarium (ACAD)','acad-specimens',
'ACAD-2','Myosotis arvensis','species','Canada','Nova Scotia','ACAD','10440',2013,'On a lobster claw','http://procyon.acadiau.ca/ecsmith/cgi-bin/image.cgi?ECS019597,jpeg; http://procyon.acadiau.ca/ecsmith/cgi-bin/image.cgi?ECS019597.jpeg'
);
INSERT INTO occurrence_raw(
auto_id,datasetname,sourcefileid,dwcaid,scientificname,taxonrank,country,stateprovince,collectioncode,catalognumber,year,habitat,associatedmedia)
VALUES (
2,'E. C. Smith Herbarium (ACAD)','acad-specimens',
'ACAD-2','Myosotis arvensis','species','Canada','Nova Scotia','ACAD','10440',2013, 'On a lobster claw','http://procyon.acadiau.ca/ecsmith/cgi-bin/image.cgi?ECS019597,jpeg; http://procyon.acadiau.ca/ecsmith/cgi-bin/image.cgi?ECS019597.jpeg'
);

/* dwcaid containing a space character */
INSERT INTO occurrence(
auto_id,datasetname,sourcefileid,dwcaid,scientificname,taxonrank,country,stateprovince,collectioncode,catalognumber,syear,habitat)
VALUES (
3,'E. C. Smith Herbarium (ACAD)','acad-specimens',
'ACAD 3','Myosotis arvensis','species','Canada','Nova Scotia','ACAD','z10441',2013,'On a lobster claw'
);
INSERT INTO occurrence_raw(
auto_id,datasetname,sourcefileid,dwcaid,scientificname,taxonrank,country,stateprovince,collectioncode,catalognumber,year,habitat)
VALUES (
3,'E. C. Smith Herbarium (ACAD)','acad-specimens',
'ACAD 3','Myosotis arvensis','species','Canada','Nova Scotia','ACAD','z10441',2013, 'On a lobster claw'
);

INSERT INTO occurrence(
auto_id,datasetname,sourcefileid,dwcaid,scientificname,taxonrank,country,stateprovince,collectioncode,catalognumber,habitat,_class,_order,decimallatitude,decimallongitude,associatedSequences,hasassociatedsequences)
VALUES (
4,'TRT (TRT)','trt-specimens',
'TRT-1','Acer pseudoplatanus','species','Canada','Nova Scotia','TRT','ECS019597','Under rocks','Magnoliopsida','Sapindales',45.099220,-62.354647,
'GenBank:KC251652|BOLD:TADCR103-10.rbcLa|GenBank:KC25165x|TestBank:anId',true
);
INSERT INTO occurrence_raw(
auto_id,datasetname,sourcefileid,dwcaid,scientificname,taxonrank,country,stateprovince,collectioncode,catalognumber,habitat,_class,_order,decimallatitude,decimallongitude,associatedSequences)
VALUES (
4,'TRT (TRT)','trt-specimens',
'TRT-1','Acer pseudoplatanus','species','Canada','Nova Scotia','ACAD','ECS019597','Under rocks','Magnoliopsida','Sapindales',45.099220,-62.354647,
'GenBank:KC251652|BOLD:TADCR103-10.rbcLa|GenBank:KC25165x|TestBank:anId'
);

/* record with empty scientificname*/
INSERT INTO occurrence(
auto_id,datasetname,sourcefileid,dwcaid,occurrenceid,scientificname,taxonrank,country,stateprovince,collectioncode,catalognumber,habitat,_class,_order,decimallatitude,decimallongitude)
VALUES (
5,'TRT (TRT)','trt-specimens',
'TRT-5','5','','species','Canada','Nova Scotia','TRT','ECS019598','Under rocks','Magnoliopsida','Sapindales',45.099220,-62.354647
);
INSERT INTO occurrence_raw(
auto_id,datasetname,sourcefileid,dwcaid,occurrenceid,scientificname,taxonrank,country,stateprovince,collectioncode,catalognumber,habitat,_class,_order,decimallatitude,decimallongitude)
VALUES (
5,'TRT (TRT)','trt-specimens',
'TRT-5','5','','species','Canada','Nova Scotia','TRT','ECS019598','Under rocks','Magnoliopsida','Sapindales',45.099220,-62.354647
);

-- resource_contact
INSERT INTO resource_contact(
            id, sourcefileid, resource_name, name, position_name, organization_name, 
            address, city, administrative_area, country, postal_code, 
            email)
VALUES (1, 'acad-specimens', 'E. C. Smith Herbarium (ACAD)', 'Greedo', 'Manager', 'Sith', '1212 Death Star Road', 'Jawas County', 'Dunes', 'Tatooine', 'R1-D2', 'greedo@tatooine.org');

-- resource_management
INSERT INTO resource_management(sourcefileid,resource_uuid,name,archive_url)
  VALUES ('acad-specimens','ABDU-NSNS-2836','E. C. Smith Herbarium (ACAD)','http://data.canadensys.net/ipt/archive.do?r=acad-specimens');
INSERT INTO resource_management(sourcefileid,resource_uuid,name,archive_url)
  VALUES ('trt-specimens','ABDU-NSNS-2837','TRT','http://data.canadensys.net/ipt/archive.do?r=trt-specimens');

-- unique_values
INSERT INTO unique_values(id,key,occurrence_count,value,unaccented_value)
VALUES (1,'_class',1,'Magnoliopsida','magnoliopsida');

INSERT INTO unique_values(id,key,occurrence_count,value,unaccented_value)
VALUES (2,'_order',1,'Sapindales','sapindales');

INSERT INTO unique_values(id,key,occurrence_count,value,unaccented_value)
VALUES (3,'country',1,'Canada','canada');

