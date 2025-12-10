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

\restrict jIAaqMAirqPWRvJLgwuAXTdxUtgvPxCa6FOTxSiBasEoDxaBmkrCL9l5nw3uNKu

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
225	33	34	2
\.


--
-- Data for Name: order_products; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.order_products (id, order_id, product_id, count) FROM stdin;
45	19	32	2
46	19	33	1
47	19	34	1
48	19	35	1
49	19	36	1
50	20	33	1
51	20	34	1
52	20	35	1
53	20	36	1
54	21	33	1
55	21	34	1
56	21	35	1
57	21	36	1
58	21	37	1
\.


--
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.orders (id, user_id, credit_card_number, verification_code, expiration_date, first_name, last_name, order_date) FROM stdin;
19	33	5843593920405030	531	03/40	Enes	Zor	2025-11-25 20:47:01.398461
20	33	5394999292001020	555	05/49	Enes	Zor	2025-11-25 20:49:43.831001
21	33	5034030104105030	555	05/50	Enes	Zor	2025-11-25 20:52:49.438734
\.


--
-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.products (id, name, description, photo, price, category) FROM stdin;
32	Aurora Smart Lamp	A sleek smart lamp that adjusts brightness automatically, offering warm ambience and energy efficiency for modern living spaces.	0.jpg	49.99	Electronics
33	BreezeFlow Mini Fan	Compact personal fan designed for powerful airflow with silent operation, perfect for office desks, bedside tables, and travel use.	1.jpg	19.5	Home
34	TerraBlend Coffee Grinder	Precision burr grinder delivering consistent grounds, ideal for coffee lovers seeking a richer flavor and smoother brewing experience.	2.jpg	64	Kitchen
35	LumaGlass Water Bottle	Crystal-clear insulated bottle that keeps drinks cold for hours, featuring a durable glass core wrapped in protective silicone.	3.jpg	22.9	Lifestyle
36	PulseFit Tracker	Lightweight fitness tracker that counts steps, monitors sleep, and provides daily wellness insights with an intuitive companion app.	4.jpg	39.99	Wearables
37	Nebula Bluetooth Speaker	Portable speaker producing immersive 360-degree sound with deep bass and long battery life, ideal for indoor and outdoor use.	5.jpg	29.99	Electronics
38	EverSoft Throw Blanket	Plush microfiber blanket offering exceptional warmth and comfort, perfect for cozy evenings on the couch or extra bedding layers.	6.jpg	24.99	Home
39	SwiftCharge Power Bank	High-capacity power bank capable of multiple device charges, built with fast-charging technology and a robust protective shell.	7.jpg	34.95	Electronics
40	PureMist Humidifier	Ultrasonic humidifier that increases room moisture with a clean, quiet mist, improving comfort in dry environments all year long.	8.jpg	42	Home
41	EcoWave Dish Set	Durable bamboo dish set designed with natural materials, perfect for sustainable households looking to reduce plastic waste.	9.jpg	27.8	Kitchen
42	ZenBrew Tea Maker	Elegant tea maker that infuses loose leaves evenly, delivering a fragrant and smooth brew for both beginners and tea enthusiasts.	10.jpg	38	Kitchen
43	SkyVenture Drone Mini	Compact drone with stable flight control, ideal for beginners who want to capture aerial moments with ease and reliability.	11.jpg	59.99	Electronics
44	CloudStep Slippers	Ultra-soft memory foam slippers providing excellent cushioning and warmth, designed for maximum comfort during daily wear.	12.jpg	18.75	Lifestyle
45	TrailGuard LED Flashlight	Rugged flashlight built for outdoor adventures, offering bright illumination, long battery life, and durable metal housing.	13.jpg	16.9	Outdoors
46	CrystalTone Earbuds	Wireless earbuds delivering crisp sound and deep bass with ergonomic fit, ensuring comfort during long listening sessions.	14.jpg	44.5	Electronics
47	AquaFlex Sports Bottle	Flexible, lightweight bottle ideal for workouts and hikes, featuring a leak-proof lid and a comfortable grip design.	15.jpg	12.99	Sports
48	GlowRoot Planter	Self-watering planter that uses moisture sensors to provide the right amount of hydration, perfect for busy plant owners.	16.jpg	26.4	Home
49	OrbitCharge Cable	Durable braided charging cable with reinforced connectors, engineered to resist bending and daily wear for long-term reliability.	17.jpg	9.99	Electronics
50	CaféCraft Milk Frother	Handheld frother that creates creamy foam instantly, enhancing homemade coffee drinks with café-style textures and flavors.	18.jpg	14.99	Kitchen
51	LuxeSkin Face Roller	Cooling facial roller made from polished stone, designed to reduce puffiness and improve circulation for refreshed skin.	19.jpg	21.5	Beauty
52	AirLite Yoga Mat	Lightweight, cushioned yoga mat offering strong grip and stability, ideal for home workouts, stretching, and fitness routines.	20.jpg	28	Sports
53	SonicClean Toothbrush	Electric toothbrush with powerful vibrations for deep cleaning, featuring multiple modes to promote better oral hygiene.	21.jpg	33.99	Health
54	FrostGuard Ice Pack	Reusable gel pack that stays colder longer, providing effective relief for muscle tension, injuries, and swelling treatments.	22.jpg	11.5	Health
55	MetroStyle Backpack	Modern backpack with organized compartments, padded laptop sleeve, and water-resistant exterior for everyday commuting.	23.jpg	54	Lifestyle
56	FreshPress Citrus Squeezer	Easy-to-use citrus squeezer that extracts maximum juice with minimal effort, perfect for fresh drinks and cooking recipes.	24.jpg	9.5	Kitchen
57	BlissAroma Diffuser	Aromatic diffuser dispersing calming essential oil mist to elevate mood, reduce stress, and create a relaxing atmosphere.	25.jpg	31.4	Home
58	IronWave Kettle	Stainless steel kettle built for fast heating and durability, ideal for preparing tea, coffee, and hot beverages quickly.	26.jpg	23.9	Kitchen
59	PeakPro Hiking Poles	Lightweight yet sturdy trekking poles that enhance stability on all terrains, suitable for both casual walkers and hikers.	27.jpg	48	Outdoors
60	HydraTune Shower Head	High-pressure shower head with adjustable spray modes, improving water flow and providing a refreshing daily experience.	28.jpg	19.99	Home
61	CalmSound Sleep Machine	White noise machine with multiple soothing sound options designed to help users sleep better and relax more deeply.	29.jpg	39	Health
62	SparkWrite Gel Pens	Smooth-writing gel pens offering vibrant colors and comfortable grip, ideal for journaling, office work, and creative tasks.	30.jpg	7.99	Office
63	Voyager Travel Pillow	Ergonomic travel pillow that supports proper neck alignment, offering comfort during long flights, drives, or train trips.	31.jpg	17.99	Travel
64	ChillBrew Ice Tray	Flexible silicone ice tray that releases cubes easily, perfect for cocktails, iced coffee, smoothies, and everyday drinks.	32.jpg	6.8	Kitchen
65	NightGlow Desk Lamp	Minimalist desk lamp featuring adjustable brightness and a soft light mode, making it ideal for studying or late-night work.	33.jpg	27.5	Home
66	FlexCharge Car Adapter	Dual-port car charger that powers devices quickly and safely, designed for long road trips and daily commutes.	34.jpg	13.4	Electronics
67	Serenity Bath Pillow	Soft, waterproof bath pillow offering neck and head support, helping users enjoy a more relaxing and comfortable bath.	35.jpg	18.2	Home
68	UltraGrip Oven Mitts	Heat-resistant silicone oven mitts that offer strong protection and non-slip grip, essential for safe cooking and baking.	36.jpg	15.99	Kitchen
69	SilverMist Facial Steamer	Compact steamer that hydrates skin with warm mist, improving clarity and prepping the face for deeper skincare treatments.	37.jpg	36.8	Beauty
70	EcoScribe Notebook	Recycled-paper notebook with durable binding and smooth pages, perfect for notes, sketches, planning, and daily journaling.	38.jpg	8.49	Office
71	TerraGrip Garden Gloves	Comfortable gardening gloves with reinforced palms, designed for durability and ease while planting, pruning, and digging.	39.jpg	10.99	Outdoors
72	ZenSound Portable Harp	Compact handheld harp producing soft, calming tones, ideal for meditation, relaxation, and creative musical exploration.	40.jpg	44	Music
73	BrightNest Desk Organizer	Multi-section organizer that keeps workspace clutter-free, offering compartments for pens, devices, notes, and accessories.	41.jpg	12.4	Office
74	GlidePro Hair Dryer	Efficient hair dryer with multiple heat settings and ionic technology, delivering smoother hair with reduced frizz.	42.jpg	49	Beauty
75	CleanWave Mop Set	Lightweight, adjustable mop with ultra-absorbent pads designed to clean floors thoroughly with minimal effort.	43.jpg	22	Home
76	TrailPack Lunch Box	Insulated lunch box that keeps meals fresh for hours, featuring multiple compartments for organized food storage.	44.jpg	16.5	Lifestyle
77	FusionGrip Phone Stand	Sturdy adjustable phone stand that holds devices securely, perfect for video calls, content viewing, and desk use.	45.jpg	8.99	Electronics
78	GlowForge Candle	Hand-poured aromatherapy candle with a long burn time and soothing fragrance, ideal for relaxation and cozy evenings.	46.jpg	14.99	Home
79	ProSlice Cutting Board	Durable cutting board made from non-slip materials, designed to stay steady during food prep while resisting stains.	47.jpg	13.8	Kitchen
80	VeloRide Bike Light	Bright, rechargeable bike light that enhances nighttime riding visibility with multiple modes for safety.	48.jpg	18.99	Outdoors
81	AuraTone Desk Fan	Quiet desk fan with adjustable angles and steady airflow, perfect for keeping cool during work or relaxation.	49.jpg	20	Home
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, name, email, phone, password, address, profile_picture) FROM stdin;
33	Enes	enes@gmail.com	5519710453	WZRHGrsBESr8wYFZ9sx0tPURuZgG2lmzyvWpwXPKz8U=	ankara/elvankent	rabbit.jpeg
\.


--
-- Name: cart_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.cart_id_seq', 225, true);


--
-- Name: order_products_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.order_products_id_seq', 58, true);


--
-- Name: orders_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.orders_id_seq', 21, true);


--
-- Name: products_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.products_id_seq', 81, true);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 33, true);


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

\unrestrict jIAaqMAirqPWRvJLgwuAXTdxUtgvPxCa6FOTxSiBasEoDxaBmkrCL9l5nw3uNKu

