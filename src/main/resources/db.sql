-- Adminer 4.6.3 PostgreSQL dump

\connect "ear";

DROP TABLE IF EXISTS "account";
DROP SEQUENCE IF EXISTS account_id_seq;
CREATE SEQUENCE account_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1;

CREATE TABLE "public"."account" (
    "id" integer DEFAULT nextval('account_id_seq') NOT NULL,
    "username" character varying NOT NULL,
    "password" character varying NOT NULL,
    "role" integer,
    CONSTRAINT "account_id" PRIMARY KEY ("id")
) WITH (oids = false);


DROP TABLE IF EXISTS "account_file";
CREATE TABLE "public"."account_file" (
    "account_id" integer NOT NULL,
    "files_id" integer NOT NULL,
    CONSTRAINT "account_file_pkey" PRIMARY KEY ("account_id", "files_id"),
    CONSTRAINT "fk_account_file_account_id" FOREIGN KEY (account_id) REFERENCES account(id) NOT DEFERRABLE,
    CONSTRAINT "fk_account_file_files_id" FOREIGN KEY (files_id) REFERENCES file(id) NOT DEFERRABLE
) WITH (oids = false);


DROP TABLE IF EXISTS "account_payment";
CREATE TABLE "public"."account_payment" (
    "account_id" integer NOT NULL,
    "payments_id" integer NOT NULL,
    CONSTRAINT "account_payment_pkey" PRIMARY KEY ("account_id", "payments_id"),
    CONSTRAINT "fk_account_payment_account_id" FOREIGN KEY (account_id) REFERENCES account(id) NOT DEFERRABLE,
    CONSTRAINT "fk_account_payment_payments_id" FOREIGN KEY (payments_id) REFERENCES payment(id) NOT DEFERRABLE
) WITH (oids = false);


DROP TABLE IF EXISTS "batch";
DROP SEQUENCE IF EXISTS batch_id_seq;
CREATE SEQUENCE batch_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1;

CREATE TABLE "public"."batch" (
    "id" integer DEFAULT nextval('batch_id_seq') NOT NULL,
    "account_id" integer NOT NULL,
    CONSTRAINT "batch_account_id" UNIQUE ("account_id"),
    CONSTRAINT "batch_id" PRIMARY KEY ("id"),
    CONSTRAINT "batch_account_id_fkey" FOREIGN KEY (account_id) REFERENCES account(id) NOT DEFERRABLE
) WITH (oids = false);


DROP TABLE IF EXISTS "batch_file";
CREATE TABLE "public"."batch_file" (
    "batch_id" integer NOT NULL,
    "files_id" integer NOT NULL,
    CONSTRAINT "batch_file_pkey" PRIMARY KEY ("batch_id", "files_id"),
    CONSTRAINT "fk_batch_file_batch_id" FOREIGN KEY (batch_id) REFERENCES batch(id) NOT DEFERRABLE,
    CONSTRAINT "fk_batch_file_files_id" FOREIGN KEY (files_id) REFERENCES file(id) NOT DEFERRABLE
) WITH (oids = false);


DROP TABLE IF EXISTS "batch_file_association";
DROP SEQUENCE IF EXISTS batch_file_association_id_seq;
CREATE SEQUENCE batch_file_association_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1;

CREATE TABLE "public"."batch_file_association" (
    "id" integer DEFAULT nextval('batch_file_association_id_seq') NOT NULL,
    "batch_id" integer NOT NULL,
    "file_id" integer NOT NULL,
    CONSTRAINT "batch_file_association_batch_id_file_id" UNIQUE ("batch_id", "file_id"),
    CONSTRAINT "batch_file_association_id" PRIMARY KEY ("id"),
    CONSTRAINT "batch_file_association_batch_id_fkey" FOREIGN KEY (batch_id) REFERENCES batch(id) NOT DEFERRABLE,
    CONSTRAINT "batch_file_association_file_id_fkey" FOREIGN KEY (file_id) REFERENCES file(id) NOT DEFERRABLE
) WITH (oids = false);

CREATE INDEX "batch_file_association_batch_id" ON "public"."batch_file_association" USING btree ("batch_id");

CREATE INDEX "batch_file_association_file_id" ON "public"."batch_file_association" USING btree ("file_id");


DROP TABLE IF EXISTS "batch_url";
CREATE TABLE "public"."batch_url" (
    "batch_id" integer NOT NULL,
    "urls_id" integer NOT NULL,
    CONSTRAINT "batch_url_pkey" PRIMARY KEY ("batch_id", "urls_id"),
    CONSTRAINT "fk_batch_url_batch_id" FOREIGN KEY (batch_id) REFERENCES batch(id) NOT DEFERRABLE,
    CONSTRAINT "fk_batch_url_urls_id" FOREIGN KEY (urls_id) REFERENCES url(id) NOT DEFERRABLE
) WITH (oids = false);


DROP TABLE IF EXISTS "file";
DROP SEQUENCE IF EXISTS file_id_seq;
CREATE SEQUENCE file_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1;

CREATE TABLE "public"."file" (
    "id" integer DEFAULT nextval('file_id_seq') NOT NULL,
    "account_id" integer NOT NULL,
    "name" character varying NOT NULL,
    "location" character varying NOT NULL,
    "expiration" timestamp NOT NULL,
    CONSTRAINT "file_id" PRIMARY KEY ("id"),
    CONSTRAINT "file_account_id_fkey" FOREIGN KEY (account_id) REFERENCES account(id) NOT DEFERRABLE
) WITH (oids = false);

CREATE INDEX "file_account_id" ON "public"."file" USING btree ("account_id");


DROP TABLE IF EXISTS "file_batch";
CREATE TABLE "public"."file_batch" (
    "file_id" integer NOT NULL,
    "batches_id" integer NOT NULL,
    CONSTRAINT "file_batch_pkey" PRIMARY KEY ("file_id", "batches_id"),
    CONSTRAINT "fk_file_batch_batches_id" FOREIGN KEY (batches_id) REFERENCES batch(id) NOT DEFERRABLE,
    CONSTRAINT "fk_file_batch_file_id" FOREIGN KEY (file_id) REFERENCES file(id) NOT DEFERRABLE
) WITH (oids = false);


DROP TABLE IF EXISTS "file_url";
CREATE TABLE "public"."file_url" (
    "file_id" integer NOT NULL,
    "urls_id" integer NOT NULL,
    CONSTRAINT "file_url_pkey" PRIMARY KEY ("file_id", "urls_id"),
    CONSTRAINT "fk_file_url_file_id" FOREIGN KEY (file_id) REFERENCES file(id) NOT DEFERRABLE,
    CONSTRAINT "fk_file_url_urls_id" FOREIGN KEY (urls_id) REFERENCES url(id) NOT DEFERRABLE
) WITH (oids = false);


DROP TABLE IF EXISTS "payment";
DROP SEQUENCE IF EXISTS payment_id_seq;
CREATE SEQUENCE payment_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1;

CREATE TABLE "public"."payment" (
    "id" integer DEFAULT nextval('payment_id_seq') NOT NULL,
    "account_id" integer NOT NULL,
    "amount" double precision NOT NULL,
    "date" timestamp NOT NULL,
    "type" character varying NOT NULL,
    CONSTRAINT "payment_id" PRIMARY KEY ("id"),
    CONSTRAINT "payment_account_id_fkey" FOREIGN KEY (account_id) REFERENCES account(id) NOT DEFERRABLE
) WITH (oids = false);

CREATE INDEX "payment_account_id" ON "public"."payment" USING btree ("account_id");


DROP TABLE IF EXISTS "sequence";
CREATE TABLE "public"."sequence" (
    "seq_name" character varying(50) NOT NULL,
    "seq_count" numeric(38,0),
    CONSTRAINT "sequence_pkey" PRIMARY KEY ("seq_name")
) WITH (oids = false);

INSERT INTO "sequence" ("seq_name", "seq_count") VALUES
('SEQ_GEN',	0);

DROP TABLE IF EXISTS "url";
DROP SEQUENCE IF EXISTS url_id_seq;
CREATE SEQUENCE url_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1;

CREATE TABLE "public"."url" (
    "id" integer DEFAULT nextval('url_id_seq') NOT NULL,
    "file_id" integer,
    "batch_id" integer,
    "string" character varying NOT NULL,
    CONSTRAINT "url_id" PRIMARY KEY ("id"),
    CONSTRAINT "url_batch_id_fkey" FOREIGN KEY (batch_id) REFERENCES batch(id) NOT DEFERRABLE,
    CONSTRAINT "url_file_id_fkey" FOREIGN KEY (file_id) REFERENCES file(id) NOT DEFERRABLE
) WITH (oids = false);

CREATE INDEX "url_batch_id" ON "public"."url" USING btree ("batch_id");

CREATE INDEX "url_file_id" ON "public"."url" USING btree ("file_id");


-- 2018-12-12 17:20:37.426613+01