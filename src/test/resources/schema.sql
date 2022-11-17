CREATE TABLE department (
  id SERIAL PRIMARY KEY,
  title text NOT NULL UNIQUE
);

CREATE TABLE users (
   id SERIAL PRIMARY KEY,
   first_name text NOT NULL,
   last_name text NOT NULL,
   email text NOT NULL UNIQUE,
   password text NOT NULL,
   job_title text NOT NULL,
   department_id integer REFERENCES department
);

CREATE TABLE project (
  id SERIAL PRIMARY KEY,
  title text NOT NULL UNIQUE,
  start_date DATE NOT NULL,
  end_date DATE
);

CREATE TABLE project_position (
  id SERIAL PRIMARY KEY,
  user_id integer REFERENCES users,
  project_id integer REFERENCES project,
  position_start_date DATE NOT NULL,
  position_end_date DATE,
  position_title text NOT NULL,
  occupation text NOT NULL
);