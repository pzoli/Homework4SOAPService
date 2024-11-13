-------------------------- Select example --------------------
select "fileName", "filePath", locationname from "filedesc" f
join "filePath" p on f.pathid = p.id
join "location" l on f.locationid = l.id
where "fileName" ilike '%ö%' and "filePath" ilike '%é%'
---------------------------------------------------------------
DROP TABLE if exists public.fileDesc;
DROP TABLE if exists public.location;
DROP TABLE if exists public."filePath";

DROP SEQUENCE if exists public.filedesc_id_seq;
DROP SEQUENCE if exists public.location_id_seq;
DROP SEQUENCE if exists public."filePath_id_seq";

CREATE SEQUENCE public.location_id_seq;

ALTER SEQUENCE public.location_id_seq OWNER TO pzoli;
    
CREATE TABLE public.location
(
    id integer NOT NULL DEFAULT nextval('location_id_seq'::regclass),
    locationName character varying(1024) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT location_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.location
    OWNER to pzoli;


CREATE SEQUENCE public."filePath_id_seq";

ALTER SEQUENCE public."filePath_id_seq"
    OWNER TO pzoli;

CREATE TABLE public."filePath"
(
    id bigint NOT NULL DEFAULT nextval('"filePath_id_seq"'::regclass),
    "filePath" character varying(8192) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT "filePath_pkey" PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public."filePath"
    OWNER to pzoli;
    
CREATE SEQUENCE public.filedesc_id_seq;

ALTER SEQUENCE public.filedesc_id_seq
    OWNER TO pzoli;
    
CREATE TABLE public.fileDesc
(
    id bigint NOT NULL DEFAULT nextval('filedesc_id_seq'::regclass),
    "fileName" character varying(4096) COLLATE pg_catalog."default" NOT NULL,
    pathid bigint NOT NULL,
    locationid integer NOT NULL,
    CONSTRAINT filedesc_pkey PRIMARY KEY (id),
    CONSTRAINT locationid FOREIGN KEY (locationid)
        REFERENCES public.location (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT pathid FOREIGN KEY (pathid)
        REFERENCES public."filePath" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.filedesc
    OWNER to pzoli;