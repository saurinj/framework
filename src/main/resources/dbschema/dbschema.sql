CREATE TABLE VALIDATION_RUN 
(
	RUN_ID INT AUTO_INCREMENT, 
	VALIDATION_TYPE VARCHAR(100) NOT NULL, 
	COMPARISON_RUN_ID INT, 
	STARTED_BY VARCHAR(50),
	TIME_CREATED DATETIME DEFAULT CURRENT_TIMESTAMP, 
	TIME_UPDATED DATETIME DEFAULT CURRENT_TIMESTAMP, 
	PRIMARY KEY (RUN_ID), 
	INDEX `VALIDATION_RUN_STARTED_BY_FI_1` (`STARTED_BY`), 
	CONSTRAINT `VALIDATION_RUN_COMPARISON_RUN_ID_FK_1` FOREIGN KEY (`COMPARISON_RUN_ID`) REFERENCES `VALIDATION_RUN` (`RUN_ID`)
);

CREATE TABLE TEST_VALIDATION 
(
	VALIDATION_ID INT AUTO_INCREMENT,
	RUN_ID INT NOT NULL,
	UNIQUE_KEY VARCHAR(100) NOT NULL, 
	COMPARISON_VALIDATION_ID INT, 
	VALIDATION_STATUS VARCHAR(50),
	REPLAY_ID INT,
	CASSANDRA_TCID VARCHAR(100),
	TIME_CREATED DATETIME DEFAULT CURRENT_TIMESTAMP, 
	TIME_UPDATED DATETIME DEFAULT CURRENT_TIMESTAMP, 
	PRIMARY KEY (VALIDATION_ID), 
	INDEX `TEST_VALIDATION_REPLAY_ID_FI_1` (`REPLAY_ID`),
	INDEX `TEST_VALIDATION_RUN_ID_FI_1` (`RUN_ID`),
	INDEX `TEST_VALIDATION_UNIQUE_KEY_FI_1` (`UNIQUE_KEY`),
	CONSTRAINT `TEST_VALIDATION_COMPARISON_VALIDATION_ID_FK_1` FOREIGN KEY (`COMPARISON_VALIDATION_ID`) REFERENCES `TEST_VALIDATION` (`VALIDATION_ID`),
	CONSTRAINT `TEST_VALIDATION_REPLAY_ID_FK_1` FOREIGN KEY (`REPLAY_ID`) REFERENCES `TEST_VALIDATION` (`VALIDATION_ID`),
	CONSTRAINT `TEST_VALIDATION_RUN_ID_FK_1` FOREIGN KEY (`REPLAY_ID`) REFERENCES `VALIDATION_RUN` (`RUN_ID`)
);

CREATE TABLE TEST_VALIDATION_STEP 
(
	STEP_ID INT AUTO_INCREMENT,
	VALIDATION_ID INT NOT NULL,
	STEP_NAME VARCHAR(100) NOT NULL, 
	MESSAGE VARCHAR(100), 
	STEP_STATUS VARCHAR(50),
	TIME_CREATED DATETIME DEFAULT CURRENT_TIMESTAMP, 
	TIME_UPDATED DATETIME DEFAULT CURRENT_TIMESTAMP, 
	PRIMARY KEY (STEP_ID), 
	INDEX `TEST_VALIDATION_STEP_VALIDATION_ID_FI_1` (`VALIDATION_ID`), 
	CONSTRAINT `TEST_VALIDATION_STEP_VALIDATION_ID_FK_1` FOREIGN KEY (`VALIDATION_ID`) REFERENCES `TEST_VALIDATION` (`VALIDATION_ID`)
);