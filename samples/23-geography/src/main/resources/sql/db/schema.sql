CREATE TABLE IF NOT EXISTS countries (
  id SERIAL,
  name VARCHAR(100) NOT NULL,
  iso_code CHAR(2) NOT NULL,
  CONSTRAINT pk_countries PRIMARY KEY (id),
  CONSTRAINT uq_countries_iso_code UNIQUE (iso_code)
);

CREATE TABLE IF NOT EXISTS states (
  id SERIAL,
  country_id INTEGER NOT NULL,
  name VARCHAR(100) NOT NULL,
  abbreviation VARCHAR(10) NOT NULL,
  CONSTRAINT pk_states PRIMARY KEY (id),
  CONSTRAINT fk_states_country_id FOREIGN KEY (country_id) REFERENCES countries(id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS cities (
  id SERIAL,
  state_id INTEGER NOT NULL,
  name VARCHAR(100) NOT NULL,
  CONSTRAINT pk_cities PRIMARY KEY (id),
  CONSTRAINT fk_cities_state_id FOREIGN KEY (state_id) REFERENCES states(id) ON DELETE RESTRICT
);

