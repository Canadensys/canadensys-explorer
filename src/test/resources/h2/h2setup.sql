-- support hstore as type
CREATE DOMAIN hstore AS OTHER;
CREATE ALIAS IF NOT EXISTS toKeyValue FOR "net.canadensys.databaseutils.H2Decode.toKeyValue";

-- text is mapped to CLOB but, not indices can be created on CLOB so change the domain to VARCHAR
CREATE DOMAIN text AS VARCHAR;