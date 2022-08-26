--
-- PostgreSQL database dump
--

-- Dumped from database version 13.7 (Debian 13.7-0+deb11u1)
-- Dumped by pg_dump version 13.7 (Debian 13.7-0+deb11u1)

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

DROP DATABASE IF EXISTS taskmanager_db;
--
-- Name: taskmanager_db; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE taskmanager_db WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'ru_RU.UTF-8';


ALTER DATABASE taskmanager_db OWNER TO postgres;

\connect taskmanager_db

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
-- Name: next_task_id; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.next_task_id
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.next_task_id OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: owners_table; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.owners_table (
    id integer NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE public.owners_table OWNER TO postgres;

--
-- Name: owners_table_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.owners_table_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.owners_table_id_seq OWNER TO postgres;

--
-- Name: owners_table_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.owners_table_id_seq OWNED BY public.owners_table.id;


--
-- Name: tasks_table; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tasks_table (
    id integer DEFAULT nextval('public.next_task_id'::regclass) NOT NULL,
    task_name character varying(255),
    owner character varying(255) NOT NULL,
    priority smallint
);


ALTER TABLE public.tasks_table OWNER TO postgres;

--
-- Name: owners_table id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.owners_table ALTER COLUMN id SET DEFAULT nextval('public.owners_table_id_seq'::regclass);


--
-- Data for Name: owners_table; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.owners_table (id, name) VALUES (1, 'Charls Darvin');
INSERT INTO public.owners_table (id, name) VALUES (2, 'CoolGuy');


--
-- Data for Name: tasks_table; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.tasks_table (id, task_name, owner, priority) VALUES (1, 'test_task-1', 'Charls Darvin', 5);
INSERT INTO public.tasks_table (id, task_name, owner, priority) VALUES (2, 'another task', 'CoolGuy', 4);


--
-- Name: next_task_id; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.next_task_id', 2, true);


--
-- Name: owners_table_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.owners_table_id_seq', 2, true);


--
-- Name: owners_table owners_table_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.owners_table
    ADD CONSTRAINT owners_table_name_key UNIQUE (name);


--
-- Name: owners_table owners_table_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.owners_table
    ADD CONSTRAINT owners_table_pkey PRIMARY KEY (id);


--
-- Name: tasks_table tasks_table_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tasks_table
    ADD CONSTRAINT tasks_table_pkey PRIMARY KEY (id);


--
-- Name: tasks_table foreign_constraint; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tasks_table
    ADD CONSTRAINT foreign_constraint FOREIGN KEY (owner) REFERENCES public.owners_table(name) ON DELETE CASCADE;


--
-- Name: DATABASE taskmanager_db; Type: ACL; Schema: -; Owner: postgres
--

GRANT ALL ON DATABASE taskmanager_db TO alex;


--
-- Name: SEQUENCE next_task_id; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON SEQUENCE public.next_task_id TO alex;


--
-- Name: TABLE owners_table; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.owners_table TO alex;


--
-- Name: SEQUENCE owners_table_id_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON SEQUENCE public.owners_table_id_seq TO alex;


--
-- Name: TABLE tasks_table; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.tasks_table TO alex;


--
-- PostgreSQL database dump complete
--

