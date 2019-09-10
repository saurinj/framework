package com.sj.application.framework.validation.dal;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sj.application.framework.validation.model.ValidationRunData;
import com.sj.application.framework.validation.model.ValidationType;

public class ValidationRunDataRowMapper implements RowMapper<ValidationRunData> {

	private static final String ALIAS = "VR.";
	private static final String COMPARISON_RUN_ALIAS = "CVR.";

	private static final String COLUMN_RUN_ID = "RUN_ID";
	private static final String COLUMN_VALIDATION_TYPE = "VALIDATION_TYPE";
	private static final String COLUMN_COMPARISON_RUN_ID = "COMPARISON_RUN_ID";
	private static final String COLUMN_STARTED_BY = "STARTED_BY";
	private static final String COLUMN_TIME_CREATED = "TIME_CREATED";
	private static final String COLUMN_TIME_UPDATED = "TIME_UPDATED";
	
	private String alias;
	private boolean populateComparisonRun;
	private String comparisonRunAlias;
	
	private ValidationRunDataRowMapper(String alias, boolean populateComparisonRun, String comparisonRunAlias) {
		this.alias = alias;
		this.populateComparisonRun = populateComparisonRun;
		this.comparisonRunAlias = comparisonRunAlias;
	}
	
	public static Builder newBuilder() {
		return new Builder();
	}
	
	public static class Builder {
		private String alias = ALIAS;
		private boolean populateComparisonRun = false;
		private String comparisonRunAlias = COMPARISON_RUN_ALIAS;
		
		public Builder() {}
		
		public Builder withAlias(String alias) {
			this.alias = alias;
			return this;
		}
		
		public Builder withPopulateComparisonRun(boolean populateComparisonRun) {
			this.populateComparisonRun = populateComparisonRun;
			return this;
		}
		
		public Builder withComparisonRunAlias(String comparisonRunAlias) {
			this.comparisonRunAlias = comparisonRunAlias;
			return this;
		}
		
		public ValidationRunDataRowMapper build() {
			return new ValidationRunDataRowMapper(alias, populateComparisonRun, comparisonRunAlias);
		}
	}

	@Override
	public ValidationRunData mapRow(ResultSet rs, int rowNum) throws SQLException {
		ValidationRunData data = getData(alias, rs);
		Long comparisonRunId = rs.getLong(alias + COLUMN_COMPARISON_RUN_ID);
		if (comparisonRunId != null && comparisonRunId != 0) {
			ValidationRunData comparisonRunData = null;
			if (populateComparisonRun) {
				comparisonRunData = getData(comparisonRunAlias, rs);
			} else {
				comparisonRunData = new ValidationRunData();
				comparisonRunData.setRunId(comparisonRunId);
				data.setComparisonRunData(comparisonRunData);
			}
			data.setComparisonRunData(comparisonRunData);
		}
		return data;
	}
	
	private ValidationRunData getData(String alias, ResultSet rs) throws SQLException {
		ValidationRunData data = new ValidationRunData();
		data.setRunId(rs.getLong(alias + COLUMN_RUN_ID));
		data.setValidationType(ValidationType.getEnum(rs.getString(alias + COLUMN_VALIDATION_TYPE)));
		data.setStartedBy(rs.getString(alias + COLUMN_STARTED_BY));
		data.setTimeCreated(rs.getDate(alias + COLUMN_TIME_CREATED));
		data.setTimeUpdated(rs.getDate(alias + COLUMN_TIME_UPDATED));
		return data;
	}
}
