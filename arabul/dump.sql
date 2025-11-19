--
-- PostgreSQL database dump
--

DO
$$
BEGIN
   IF EXISTS (SELECT FROM pg_roles WHERE rolname = 'postgres') THEN
      ALTER ROLE postgres WITH PASSWORD 'root';
   ELSE
      CREATE ROLE postgres LOGIN SUPERUSER PASSWORD 'root';
   END IF;
END
$$;

-- Dumped from database version 14.19 (Ubuntu 14.19-0ubuntu0.22.04.1)
-- Dumped by pg_dump version 14.19 (Ubuntu 14.19-0ubuntu0.22.04.1)

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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: cart; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cart (
    id integer NOT NULL,
    user_id bigint,
    product_id bigint,
    count integer
);


ALTER TABLE public.cart OWNER TO postgres;

--
-- Name: cart_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.cart_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.cart_id_seq OWNER TO postgres;

--
-- Name: cart_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.cart_id_seq OWNED BY public.cart.id;


--
-- Name: order_products; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.order_products (
    id integer NOT NULL,
    order_id integer,
    product_id integer,
    count integer
);


ALTER TABLE public.order_products OWNER TO postgres;

--
-- Name: order_products_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.order_products_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.order_products_id_seq OWNER TO postgres;

--
-- Name: order_products_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.order_products_id_seq OWNED BY public.order_products.id;


--
-- Name: orders; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.orders (
    id integer NOT NULL,
    user_id integer,
    credit_card_number character varying(16),
    verification_code integer,
    expiration_date character varying(5),
    first_name character varying(30),
    last_name character varying(30),
    order_date timestamp without time zone DEFAULT now()
);


ALTER TABLE public.orders OWNER TO postgres;

--
-- Name: orders_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.orders_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.orders_id_seq OWNER TO postgres;

--
-- Name: orders_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.orders_id_seq OWNED BY public.orders.id;


--
-- Name: products; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.products (
    id integer NOT NULL,
    name character varying(255) NOT NULL,
    description character varying(1000) NOT NULL,
    photo character varying(255) NOT NULL,
    price double precision NOT NULL,
    category character varying(255) NOT NULL
);


ALTER TABLE public.products OWNER TO postgres;

--
-- Name: products_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.products_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.products_id_seq OWNER TO postgres;

--
-- Name: products_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.products_id_seq OWNED BY public.products.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id integer NOT NULL,
    name character varying(100) NOT NULL,
    email character varying(150) NOT NULL,
    phone character varying(20),
    password character varying(255) NOT NULL,
    address character varying(255) NOT NULL,
    profile_picture character varying(255)
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.users_id_seq OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- Name: cart id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cart ALTER COLUMN id SET DEFAULT nextval('public.cart_id_seq'::regclass);


--
-- Name: order_products id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.order_products ALTER COLUMN id SET DEFAULT nextval('public.order_products_id_seq'::regclass);


--
-- Name: orders id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders ALTER COLUMN id SET DEFAULT nextval('public.orders_id_seq'::regclass);


--
-- Name: products id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.products ALTER COLUMN id SET DEFAULT nextval('public.products_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- Data for Name: cart; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.cart (id, user_id, product_id, count) FROM stdin;
205     31      22      1
206     31      21      1
\.


--
-- Data for Name: order_products; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.order_products (id, order_id, product_id, count) FROM stdin;
17      8       12      2
18      8       11      1
19      9       12      2
20      9       11      1
21      10      19      1
22      11      19      1
23      12      20      1
24      12      19      1
25      12      18      1
26      12      17      1
27      12      21      1
28      12      22      1
29      13      19      4
30      13      18      3
31      13      17      2
32      13      21      1
33      14      19      4
34      14      20      3
35      14      21      2
36      15      18      1
37      15      17      1
38      15      19      2
39      16      22      1
40      16      21      1
41      16      20      3
42      17      19      3
43      17      18      2
44      18      17      2
\.


--
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.orders (id, user_id, credit_card_number, verification_code, expiration_date, first_name, last_name, order_date) FROM stdin;
1       25      5555555555555554        554     12/28   Enes    Zor     2025-11-12 20:55:24.390335
2       25      5555555555555554        554     12/28   Enes    Zor     2025-11-12 20:55:24.390335
3       25      5555555555555554        554     12/28   Enes    Zor     2025-11-12 20:55:24.390335
4       25      5555555555555554        554     12/28   Enes    Zor     2025-11-12 20:55:24.390335
5       25      5555555555555554        554     12/28   Enes    Zor     2025-11-12 20:55:24.390335
6       25      5555555555555554        554     12/28   Enes    Zor     2025-11-12 20:55:24.390335
7       25      5555555555555554        554     12/28   Enes    Zor     2025-11-12 20:55:24.390335
8       25      5555555555555554        554     12/28   Enes    Zor     2025-11-12 20:55:24.390335
9       25      5555555555555554        554     12/28   Enes    Zor     2025-11-12 20:55:24.390335
10      15      5555555555555555        555     05/30   Enes    Zor     2025-11-12 20:55:24.390335
11      15      5555555555555555        555     02/30   Enes    Zor     2025-11-12 20:55:24.390335
12      15      5555555555555555        555     05/30   Enes    Zor     2025-11-12 20:55:24.390335
13      15      5555555555555942        555     05/40   Enes Talha      Zor     2025-11-12 20:55:24.390335
14      15      5555555555557894        555     01/40   Enes    Zor     2025-11-12 20:55:24.390335
15      31      5444544454449234        549     02/25   Eno     Zor     2025-11-12 20:55:24.390335
16      31      5444544454442933        543     02/30   Enes    Zor     2025-11-12 20:55:24.390335
17      31      5555555555553040        552     04/30   Enoş    Zor     2025-11-12 21:02:24.91158
18      31      5554555455540503        555     02/40   Enes    Zor     2025-11-14 19:54:21.444379
\.


--
-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.products (id, name, description, photo, price, category) FROM stdin;
17      Haydo   for davşans     download.jpeg   10.640000343322754      book
18      Sehmistanın kocbasi     Haydonun kabusu download.jpeg   10.640000343322754      book
19      Enes    Enesin hikayeleri       download.jpeg   34.41999816894531       book
20      Talha   Hikaye  download.jpeg   34.41999816894531       book
21      Zor     Hikaye  1345614.jpeg    34.41999816894531       book
22      Zor     Hikaye  1345614.jpeg    34.41999816894531       book
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, name, email, phone, password, address, profile_picture) FROM stdin;
16      talha   talha@admin.com 5000000000      WZRHGrsBESr8wYFZ9sx0tPURuZgG2lmzyvWpwXPKz8U=    ankara/etimesgut
17              mesude@admin.com        5000000001      WZRHGrsBESr8wYFZ9sx0tPURuZgG2lmzyvWpwXPKz8U=    ankara/etimesgut
18      asdsd   asadasd@admin.com       5050505050      A6xnQhbz4Vx2HuGl4lXwZ5U2I8iziLRFnhP5eNfIRvQ=    ankara/etimesgut
19      asd     ASASd@admin.com 5050505052      A6xnQhbz4Vx2HuGl4lXwZ5U2I8iziLRFnhP5eNfIRvQ=    ankara/etimesgut
20      ease    ease@gmail.com  5050505056      A6xnQhbz4Vx2HuGl4lXwZ5U2I8iziLRFnhP5eNfIRvQ=    ankara/etimesgut
21              ase@gmail.com   5050505059      A6xnQhbz4Vx2HuGl4lXwZ5U2I8iziLRFnhP5eNfIRvQ=    ankara/etimesgut
22              adasdasdasdsd@gmail.com 5050505057      WZRHGrsBESr8wYFZ9sx0tPURuZgG2lmzyvWpwXPKz8U=    ankara/etimesgut
23      asdsdafgh       fatih@gmail.c   5071525360      WZRHGrsBESr8wYFZ9sx0tPURuZgG2lmzyvWpwXPKz8U=    ankara/etimesgut
25      enes    enes@gmail.com  5071525361      0QKtcVQzF1jYEiriTWLGQThAwdmFNbj5AUqMa9RM0Ls=    ankara/etimesgut
26      zor     zor@gmail.com   5529720554      jZae727K08KaOmKSgOaGzww/XVqGr/PKEgIMkjrcbJI=    etimesgut/ankara
27      qwe     qwe@gmail.com   5540304300      A6xnQhbz4Vx2HuGl4lXwZ5U2I8iziLRFnhP5eNfIRvQ=    ankara/etimesgut
28      qwer    qwer@gmail.com  5312234433      A6xnQhbz4Vx2HuGl4lXwZ5U2I8iziLRFnhP5eNfIRvQ=    ankara/etimesgut        rabbit.jpeg
15      Enes    Enes@admin.com  5943042030      0QKtcVQzF1jYEiriTWLGQThAwdmFNbj5AUqMa9RM0Ls=    ankara/etimesgut        1345614.jpeg
29      haydo   haydo@gmail.com 5348348428      WZRHGrsBESr8wYFZ9sx0tPURuZgG2lmzyvWpwXPKz8U=    ankara/etimesgut        rabbit.jpeg
30      davsan  davsan@gmail.com        5389599294      WZRHGrsBESr8wYFZ9sx0tPURuZgG2lmzyvWpwXPKz8U=    etimesgut/ankara        rabbit.jpeg
31      Enes    eno@gmail.com   5594994939      WZRHGrsBESr8wYFZ9sx0tPURuZgG2lmzyvWpwXPKz8U=    ankara/elvankent        rabbit.jpeg
\.


--
-- Name: cart_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.cart_id_seq', 206, true);


--
-- Name: order_products_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.order_products_id_seq', 44, true);


--
-- Name: orders_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.orders_id_seq', 18, true);


--
-- Name: products_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.products_id_seq', 22, true);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 31, true);


--
-- Name: cart cart_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cart
    ADD CONSTRAINT cart_pkey PRIMARY KEY (id);


--
-- Name: order_products order_products_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.order_products
    ADD CONSTRAINT order_products_pkey PRIMARY KEY (id);


--
-- Name: orders orders_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (id);


--
-- Name: products products_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_pkey PRIMARY KEY (id);


--
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users users_phone_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_phone_key UNIQUE (phone);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- PostgreSQL database dump complete
--

