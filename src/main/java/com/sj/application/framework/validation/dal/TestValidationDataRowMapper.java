package com.sj.application.framework.validation.dal;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sj.application.framework.validation.model.TestValidationData;
import com.sj.application.framework.validation.model.ValidationRunData;
import com.sj.application.framework.validation.model.ValidationStatus;

public class TestValidationDataRowMapper implements RowMapper<TestValidationData> {

	private static final String ALIAS = "TV.";
	private static final String VALIDATION_RUN_ALIAS = "VR.";
	private static final String VALIDATION_COMPARISON_ALIAS = "TVC.";
	private static final String VALIDATION_REPLAY_ALIAS = "TVR.";

	private static final String COLUMN_ID = "VALIDATION_ID";
	private static final String COLUMN_RUN_ID = "RUN_ID";
	private static final String COLUMN_UNIQUE_KEY = "UNIQUE_KEY";
	private static final String COLUMN_COMPARISON_VALIDATION_ID = "COMPARISON_VALIDATION_ID";
	private static final String COLUMN_VALIDATION_STATUS = "VALIDATION_STATUS";
	private static final String COLUMN_REPLAY_ID = "REPLAY_ID";
	private static final String COLUMN_CASSANDRA_TCID = "CASSANDRA_TCID";
	private static final String COLUMN_TIME_CREATED = "TIME_CREATED";
	private static final String COLUMN_TIME_UPDATED = "TIME_UPDATED";

	private String alias;
	private boolean populateValidationRunData;
	private String validationRunAlias;
	private boolean populateValidationComparisonData;
	private String validationComparisonAlias;
	private boolean populateReplayData;
	private String replayAlias;

	private TestValidationDataRowMapper(String alias, boolean populateValidationRunData, String validationRunAlias,
			boolean populateValidationComparisonData, String validationComparisonAlias, boolean populateReplayData,
			String replayAlias) {
		this.alias = alias;
		this.populateValidationRunData = populateValidationRunData;
		this.validationRunAlias = validationRunAlias;
		this.populateValidationComparisonData = populateValidationComparisonData;
		this.validationComparisonAlias = validationComparisonAlias;
		this.populateReplayData = populateReplayData;
		this.replayAlias = replayAlias;
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public static class Builder {
		private String alias = ALIAS;
		private boolean populateValidationRunData = false;
		private String validationRunAlias = VALIDATION_RUN_ALIAS;
		private boolean populateValidationComparisonData = false;
		private String validationComparisonAlias = VALIDATION_COMPARISON_ALIAS;
		private boolean populateReplayData = false;
		private String replayAlias = VALIDATION_REPLAY_ALIAS;

		public Builder withAlias(String alias) {
			this.alias = alias;
			return this;
		}

		public Builder withPopulateValidationRunData(boolean populateValidationRunData) {
			this.populateValidationRunData = populateValidationRunData;
			return this;
		}

		public Builder withValidationRunAlias(String validationRunAlias) {
			this.validationRunAlias = validationRunAlias;
			return this;
		}

		public Builder withPopulateValidationComparisonData(boolean populateValidationComparisonData) {
			this.populateValidationComparisonData = populateValidationComparisonData;
			return this;
		}

		public Builder withValidationComparisonAlias(String validationComparisonAlias) {
			this.validationComparisonAlias = validationComparisonAlias;
			return this;
		}

		public Builder withPopulateReplayData(boolean populateReplayData) {
			this.populateReplayData = populateReplayData;
			return this;
		}

		public Builder withReplayAlias(String replayAlias) {
			this.replayAlias = replayAlias;
			return this;
		}

		public TestValidationDataRowMapper build() {
			return new TestValidationDataRowMapper(alias, populateValidationRunData, validationRunAlias,
					populateValidationComparisonData, validationComparisonAlias, populateReplayData, replayAlias);
		}
	}

	@Override
	public TestValidationData mapRow(ResultSet rs, int rowNum) throws SQLException {
		TestValidationData data = getData(alias, rs);
		// populate run data only for this row
		ValidationRunData runData = null;
		if (populateValidationRunData) {
			runData = ValidationRunDataRowMapper.newBuilder().build().mapRow(rs, rowNum);
		} else {
			runData = new ValidationRunData();
			runData.setRunId(rs.getLong(alias + COLUMN_RUN_ID));
		}
		data.setRunData(runData);
		// populate comparison data
		Long comparisonValidationId = rs.getLong(COLUMN_COMPARISON_VALIDATION_ID);
		if (comparisonValidationId != null && comparisonValidationId != 0) {
			TestValidationData validationComparisonData = null;
			if (populateValidationComparisonData) {
				validationComparisonData = getData(validationComparisonAlias, rs);
			} else {
				validationComparisonData = new TestValidationData();
				validationComparisonData.setValidationId(comparisonValidationId);
			}
			data.setReplayData(validationComparisonData);
		}
		// populate replay data
		Long replayId = rs.getLong(COLUMN_REPLAY_ID);
		if (replayId != null && replayId != 0) {
			TestValidationData replayData = null;
			if (populateReplayData) {
				replayData = getData(replayAlias, rs);
			} else {
				replayData = new TestValidationData();
				replayData.setValidationId(replayId);
			}
			data.setReplayData(replayData);
		}
		return data;
	}

	private TestValidationData getData(String alias, ResultSet rs) throws SQLException {
		TestValidationData data = new TestValidationData();
		data.setValidationId(rs.getLong(alias + COLUMN_ID));
		data.setUniqueKey(rs.getString(alias + COLUMN_UNIQUE_KEY));
		data.setValidationStatus(ValidationStatus.getEnum(rs.getString(alias + COLUMN_VALIDATION_STATUS)));
		data.setCassandraTcId(rs.getString(alias + COLUMN_CASSANDRA_TCID));
		data.setTimeCreated(rs.getDate(alias + COLUMN_TIME_CREATED));
		data.setTimeUpdated(rs.getDate(alias + COLUMN_TIME_UPDATED));
		return data;
	}
}
