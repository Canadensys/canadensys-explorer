INSERT INTO occurrence(
auto_id,datasetname,sourcefileid,dwcaid,associatedmedia,scientificname,taxonrank,country,stateprovince,collectioncode,catalognumber,minimumelevationinmeters,maximumelevationinmeters,syear,habitat,_class,_order,decimallatitude,decimallongitude)
VALUES (
1,'E. C. Smith Herbarium (ACAD)','acad-specimens',
'ACAD-1','http://procyon.acadiau.ca/ecsmith/cgi-bin/image.cgi?ECS019597,jpeg; http://procyon.acadiau.ca/ecsmith/cgi-bin/image.cgi?ECS019597.jpeg',
'Acer pseudoplatanus','species','Canada','Nova Scotia','ACAD','ECS019597',100,200,1980,'Under rocks','Magnoliopsida','Sapindales',45.099220,-62.354647
);
INSERT INTO occurrence_raw(
auto_id,datasetname,sourcefileid,dwcaid,associatedmedia,scientificname,taxonrank,country,stateprovince,collectioncode,catalognumber,year,habitat)
VALUES (
1,'E. C. Smith Herbarium (ACAD)','acad-specimens',
'ACAD-1','http://procyon.acadiau.ca/ecsmith/cgi-bin/image.cgi?ECS019597,jpeg; http://procyon.acadiau.ca/ecsmith/cgi-bin/image.cgi?ECS019597.jpeg',
'Acer pseudoplatanus','species','Canada','Nova Scotia','ACAD','ECS019597',1980,'Under rocks'
);

INSERT INTO occurrence(
auto_id,datasetname,sourcefileid,dwcaid,scientificname,taxonrank,country,stateprovince,collectioncode,catalognumber,syear,habitat)
VALUES (
2,'E. C. Smith Herbarium (ACAD)','acad-specimens',
'ACAD-2','Myosotis arvensis','species','Canada','Nova Scotia','ACAD','10440',2013,'On a lobster claw'
);
INSERT INTO occurrence_raw(
auto_id,datasetname,sourcefileid,dwcaid,scientificname,taxonrank,country,stateprovince,collectioncode,catalognumber,year,habitat)
VALUES (
2,'E. C. Smith Herbarium (ACAD)','acad-specimens',
'ACAD-2','Myosotis arvensis','species','Canada','Nova Scotia','ACAD','10440',2013, 'On a lobster claw'
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
auto_id,datasetname,sourcefileid,dwcaid,scientificname,taxonrank,country,stateprovince,collectioncode,catalognumber,habitat,_class,_order,decimallatitude,decimallongitude,associatedSequences)
VALUES (
4,'TRT (TRT)','trt-specimens',
'TRT-1','Acer pseudoplatanus','species','Canada','Nova Scotia','TRT','ECS019597','Under rocks','Magnoliopsida','Sapindales',45.099220,-62.354647,
'GenBank:KC251652|BOLD:TADCR103-10.rbcLa|GenBank:KC25165x|TestBank:anId'
);
INSERT INTO occurrence_raw(
auto_id,datasetname,sourcefileid,dwcaid,scientificname,taxonrank,country,stateprovince,collectioncode,catalognumber,habitat,_class,_order,decimallatitude,decimallongitude,associatedSequences)
VALUES (
4,'TRT (TRT)','trt-specimens',
'ACAD-1','Acer pseudoplatanus','species','Canada','Nova Scotia','ACAD','ECS019597','Under rocks','Magnoliopsida','Sapindales',45.099220,-62.354647,
'GenBank:KC251652|BOLD:TADCR103-10.rbcLa|GenBank:KC25165x|TestBank:anId'
);

-- resource_contact
INSERT INTO resource_contact(
            id, sourcefileid, resource_name, name, position_name, organization_name, 
            address, city, administrative_area, country, postal_code, 
            email)
VALUES (1, 'acad-specimens', 'E. C. Smith Herbarium (ACAD)', 'Greedo', 'Manager', 'Sith', '1212 Death Star Road', 'Jawas County', 'Dunes', 'Tatooine', 'R1-D2', 'greedo@tatooine.org');

-- resource_management
INSERT INTO resource_management(sourcefileid,name,archive_url)
  VALUES ('acad-specimens','E. C. Smith Herbarium (ACAD)','http://data.canadensys.net/ipt/archive.do?r=acad-specimens');

-- unique_values
INSERT INTO unique_values(id,key,occurrence_count,value,unaccented_value)
VALUES (1,'_class',1,'Magnoliopsida','magnoliopsida');

INSERT INTO unique_values(id,key,occurrence_count,value,unaccented_value)
VALUES (2,'_order',1,'Sapindales','sapindales');

INSERT INTO unique_values(id,key,occurrence_count,value,unaccented_value)
VALUES (3,'country',1,'Canada','canada');

