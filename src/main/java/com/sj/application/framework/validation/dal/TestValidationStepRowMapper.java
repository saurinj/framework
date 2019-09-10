package com.sj.application.framework.validation.dal;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sj.application.framework.step.ReadOnlyStep;
import com.sj.application.framework.step.Step;
import com.sj.application.framework.step.StepStatus;
import com.sj.application.framework.validation.model.TestValidationData;

public class TestValidationStepRowMapper implements RowMapper<Step<TestValidationData>> {
	
	private static final String COLUMN_STEP_ID = "STEP_ID";
	private static final String COLUMN_STEP_NAME = "STEP_NAME";
	private static final String COLUMN_MESSAGE = "MESSAGE";
	private static final String COLUMN_STEP_STATUS = "STEP_STATUS";
	private static final String COLUMN_TIME_CREATED = "TIME_CREATED";
	private static final String COLUMN_TIME_UPDATED = "TIME_UPDATED";

	@Override
	public Step<TestValidationData> mapRow(ResultSet rs, int rowNum) throws SQLException {
		ReadOnlyStep<TestValidationData> step = new ReadOnlyStep<>(rs.getString(COLUMN_STEP_NAME));
		step.getStepData().setId(rs.getLong(COLUMN_STEP_ID));
		step.getStepData().setMessage(rs.getString(COLUMN_MESSAGE));
		step.getStepData().setStepStatus(StepStatus.getEnum(rs.getString(COLUMN_STEP_STATUS)));
		step.getStepData().setTimeCreated(rs.getDate(COLUMN_TIME_CREATED));
		step.getStepData().setTimeUpdated(rs.getDate(COLUMN_TIME_UPDATED));
		return step;
	}
}
