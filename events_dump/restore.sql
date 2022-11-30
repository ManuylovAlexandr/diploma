--
-- NOTE:
--
-- File paths need to be edited. Search for $$PATH$$ and
-- replace it with the path to the directory containing
-- the extracted data files.
--
--
-- PostgreSQL database dump
--

-- Dumped from database version 14.5 (Debian 14.5-1.pgdg110+1)
-- Dumped by pg_dump version 14.5 (Ubuntu 14.5-0ubuntu0.22.04.1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE postgres;
--
-- Name: postgres; Type: DATABASE; Schema: -; Owner: -
--

CREATE DATABASE postgres WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'en_US.utf8';


\connect postgres

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: DATABASE postgres; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON DATABASE postgres IS 'default administrative connection database';


--
-- Name: therapy_analytics; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA therapy_analytics;


--
-- Name: pg_trgm; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS pg_trgm WITH SCHEMA public;


--
-- Name: EXTENSION pg_trgm; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION pg_trgm IS 'text similarity measurement and index searching based on trigrams';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: stored_events; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.stored_events (
    id bigint NOT NULL,
    occurred_on timestamp with time zone NOT NULL,
    event_type text NOT NULL,
    event_payload jsonb NOT NULL,
    event_metadata jsonb NOT NULL,
    stream_id character varying(64) NOT NULL
);


--
-- Name: TABLE stored_events; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.stored_events IS 'Domain events store';


--
-- Name: COLUMN stored_events.stream_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.stored_events.stream_id IS 'Indicates that current event is part of specified event stream. Each stream contains events for one particular instance of aggregate root';


--
-- Name: stored_events_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.stored_events_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: stored_events_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.stored_events_id_seq OWNED BY public.stored_events.id;


--
-- Name: stored_events id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.stored_events ALTER COLUMN id SET DEFAULT nextval('public.stored_events_id_seq'::regclass);


--
-- Data for Name: stored_events; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.stored_events (id, occurred_on, event_type, event_payload, event_metadata, stream_id) FROM stdin;
\.
COPY public.stored_events (id, occurred_on, event_type, event_payload, event_metadata, stream_id) FROM '$$PATH$$/3360.dat';

--
-- Name: stored_events_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.stored_events_id_seq', 103, true);


--
-- Name: stored_events stored_events_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.stored_events
    ADD CONSTRAINT stored_events_pkey PRIMARY KEY (id);


--
-- Name: stored_events_ix_event_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX stored_events_ix_event_type ON public.stored_events USING btree (event_type);


--
-- PostgreSQL database dump complete
--

