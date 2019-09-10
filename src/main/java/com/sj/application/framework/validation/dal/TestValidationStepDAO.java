package com.sj.application.framework.validation.dal;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.sj.application.framework.step.Step;
import com.sj.application.framework.step.StepStatus;
import com.sj.application.framework.validation.model.TestValidationData;

/**
 * TEST_VALIDATION_STEP Table Schema: STEP_ID, VALIDATION_ID, STEP_NAME, STEP_STATUS,
 * MESSAGE, TIME_CREATED, TIME_UPDATED
 *
 */
@Repository
public class TestValidationStepDAO {

	private static final String GET_STEPS_BY_VALIDATION_ID = "SELECT * FROM TEST_VALIDATION_STEP WHERE VALIDATION_ID=?";
	private static final String INSERT_STEP = "INSERT INTO TEST_VALIDATION_STEP(VALIDATION_ID, STEP_NAME, STEP_STATUS) VALUES (?, ?, ?)";
	private static final String UPDATE_STEP = "UPDATE TEST_VALIDATION_STEP SET STEP_STATUS=?, MESSAGE=? WHERE STEP_ID=?";

	private JdbcTemplate jdbcTemplate;

	@Inject
	public TestValidationStepDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public int insertValidationStep(Long validationId, Step<TestValidationData> step, StepStatus stepStatus) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int insertCount = jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(INSERT_STEP, Statement.RETURN_GENERATED_KEYS);
			ps.setLong(1, validationId);
			ps.setString(2, step.getStepData().getStepName());
			ps.setString(3, stepStatus.getText());
			return ps;
		}, keyHolder);
		step.getStepData().setId((long) keyHolder.getKey());
		return insertCount;
	}

	public int updateValidationStep(Step<TestValidationData> step) {
		return jdbcTemplate.update(UPDATE_STEP, step.getStepData().getStepStatus().getText(),
				step.getStepData().getMessage(), step.getStepData().getId());
	}

	public List<Step<TestValidationData>> getSteps(Long validationId) {
		return jdbcTemplate.query(GET_STEPS_BY_VALIDATION_ID, new Object[] { validationId }, new TestValidationStepRowMapper());
	}

}
