-- Users table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    last_login TIMESTAMP
);

-- File Jobs table
CREATE TABLE file_jobs (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id),
    filename VARCHAR(255) NOT NULL,
    original_filename VARCHAR(255) NOT NULL,
    file_type VARCHAR(20) NOT NULL,
    upload_date TIMESTAMP NOT NULL DEFAULT NOW(),
    status VARCHAR(20) NOT NULL,
    error_message TEXT,
    result_location VARCHAR(255),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Processing Steps table
CREATE TABLE processing_steps (
    id SERIAL PRIMARY KEY,
    job_id INT NOT NULL,
    step_type VARCHAR(50) NOT NULL,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    status VARCHAR(20) NOT NULL,
    details JSONB,
    CONSTRAINT fk_job FOREIGN KEY (job_id) REFERENCES file_jobs(id)
);

-- Validation Rules table
CREATE TABLE validation_rules (
    id SERIAL PRIMARY KEY,
    job_id INT,
    rule_name VARCHAR(100) NOT NULL,
    rule_type VARCHAR(50) NOT NULL,
    rule_config JSONB NOT NULL,
    severity VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_job FOREIGN KEY (job_id) REFERENCES file_jobs(id)
);

-- Transformations table
CREATE TABLE transformations (
    id SERIAL PRIMARY KEY,
    step_id INT,
    transform_type VARCHAR(50) NOT NULL,
    source_field VARCHAR(100),
    target_field VARCHAR(100),
    transform_config JSONB,
    transform_order INT NOT NULL,
    CONSTRAINT fk_step FOREIGN KEY (step_id) REFERENCES processing_steps(id)
);

-- Create indexes
CREATE INDEX idx_filejobs_userid ON file_jobs(user_id);
CREATE INDEX idx_processingsteps_jobid ON processing_steps(job_id);
CREATE INDEX idx_validationrules_jobid ON validation_rules(job_id);
CREATE INDEX idx_transformations_stepid ON transformations(step_id);