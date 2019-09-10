package com.sj.application.framework.validation.dal;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.sj.application.framework.validation.model.ValidationRunData;

/**
 * VALIDATION_RUN Table Schema: RUN_ID, VALIDATION_TYPE, COMPARISON_RUN_ID,
 * STARTED_BY, TIME_CREATED, TIME_UPDATED
 *
 */
@Repository
public class ValidationRunDAO {

	private static final String GET_RUN_BY_RUN_ID = "SELECT VR.*, CVR.* FROM VALIDATION_RUN VR LEFT JOIN VALIDATION_RUN CVR ON VR.COMPARISON_RUN_ID=CVR.RUN_ID AND VR.RUN_ID=?";
	private static final String GET_RUN_BY_STARTED_BY = "SELECT * FROM VALIDATION_RUN VR WHERE STARTED_BY=?";
	private static final String INSERT_RUN = "INSERT INTO VALIDATION_RUN(VALIDATION_TYPE, COMPARISON_RUN_ID, STARTED_BY) VALUES (?, ?, ?)";

	private JdbcTemplate jdbcTemplate;

	@Inject
	public ValidationRunDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public ValidationRunData createRun(ValidationRunData request) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(INSERT_RUN, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, request.getValidationType().getText());
			if (request.getComparisonRunData() != null) {
				ps.setLong(2, request.getComparisonRunData().getRunId());
			} else {
				ps.setNull(2, java.sql.Types.INTEGER);
			}
			ps.setString(3, request.getStartedBy());
			return ps;
		}, keyHolder);
		request.setTimeCreated(new Date());
		request.setTimeUpdated(new Date());
		request.setRunId((long) keyHolder.getKey());
		return request;
	}

	public ValidationRunData getRunData(Long runId) {
		List<ValidationRunData> validationRunData = jdbcTemplate.query(GET_RUN_BY_RUN_ID, new Object[] { runId },
				ValidationRunDataRowMapper.newBuilder().withPopulateComparisonRun(true).build());
		return validationRunData.isEmpty() ? null : validationRunData.get(0);
	}

	public List<ValidationRunData> getRunDataByStartedBy(String startedBy) {
		return jdbcTemplate.query(GET_RUN_BY_STARTED_BY, new Object[] { startedBy }, ValidationRunDataRowMapper.newBuilder().build());
	}
}
