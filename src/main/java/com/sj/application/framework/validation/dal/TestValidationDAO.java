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

import com.sj.application.framework.validation.model.TestValidationData;

/**
 * TEST_VALIDATION Table Schema: VALIDATION_ID, RUN_ID, UNIQUE_KEY,
 * COMPARISON_VALIDATION_ID, VALIDATION_STATUS, REPLAY_ID, CASSANDRA_TCID,
 * TIME_CREATED, TIME_UPDATED
 *
 */
@Repository
public class TestValidationDAO {

	private static final String GET_VALIDATION_BY_VALIDATION_ID = "SELECT TV.*, VR.* FROM TEST_VALIDATION TV, VALIDATION_RUN VR WHERE TV.RUN_ID=VR.RUN_ID AND TV.VALIDATION_ID=? ";
	private static final String GET_VALIDATION_BY_RUN_ID = "SELECT TV.*, VR.* FROM TEST_VALIDATION TV, VALIDATION_RUN VR WHERE TV.RUN_ID=VR.RUN_ID AND TV.RUN_ID=? ";
	private static final String GET_LATEST_VALIDATION_BY_RUN_ID_AND_UNIQUE_KEY = "SELECT * FROM TEST_VALIDATION TV WHERE RUN_ID=? AND UNIQUE_KEY=? AND REPLAY_ID IS NULL";
	private static final String INSERT_VALIDATION = "INSERT INTO TEST_VALIDATION(RUN_ID, UNIQUE_KEY, COMPARISON_VALIDATION_ID, VALIDATION_STATUS, CASSANDRA_TCID) VALUES (?, ?, ?, ?, ?)";
	private static final String UPDATE_REPLAY_ID = "UPDATE TEST_VALIDATION SET REPLAY_ID=?, TIME_UPDATED=? WHERE VALIDATION_ID=?";
	private static final String UPDATE_STATUS = "UPDATE TEST_VALIDATION SET VALIDATION_STATUS=?, TIME_UPDATED=? WHERE VALIDATION_ID=?";
	private static final String DELETE_VALIDATION = "DELETE FROM TEST_VALIDATION WHERE VALIDATION_ID=?";

	private JdbcTemplate jdbcTemplate;

	@Inject
	public TestValidationDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * This method populates TestValidationData object with fully populated runData,
	 * validationComparisonData and replayData
	 * 
	 * @param validationId
	 * @return TestValidationData
	 */
	public TestValidationData getValidationById(Long validationId) {
		List<TestValidationData> validationList = jdbcTemplate.query(GET_VALIDATION_BY_VALIDATION_ID,
				new Object[] { validationId }, TestValidationDataRowMapper.newBuilder()
						.withPopulateValidationRunData(true).withPopulateValidationComparisonData(true).withPopulateReplayData(true).build());
		return validationList.isEmpty() ? null : validationList.get(0);
	}

	/**
	 * This method populates TestValidationData object with fully populated runData.
	 * But it does not fully populate validationComparisonData and replayData, just
	 * their ids are populated
	 * 
	 * @param validationId
	 * @return TestValidationData
	 */
	public List<TestValidationData> getValidationsByRunId(Long runId) {
		return jdbcTemplate.query(GET_VALIDATION_BY_RUN_ID, new Object[] { runId },
				TestValidationDataRowMapper.newBuilder().build());
	}

	public TestValidationData getLatestValidationByRunIdAndUniqueKey(Long runId, String uniqueKey) {
		List<TestValidationData> validationList = jdbcTemplate.query(GET_LATEST_VALIDATION_BY_RUN_ID_AND_UNIQUE_KEY,
				new Object[] { runId, uniqueKey }, TestValidationDataRowMapper.newBuilder().build());
		return validationList.isEmpty() ? null : validationList.get(0);
	}

	public TestValidationData createTestValidation(TestValidationData createRequest) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(INSERT_VALIDATION, Statement.RETURN_GENERATED_KEYS);
			ps.setLong(1, createRequest.getRunData().getRunId());
			ps.setString(2, createRequest.getUniqueKey());
			if (createRequest.getValidationComparisonData() != null) {
				ps.setLong(3, createRequest.getValidationComparisonData().getValidationId());
			} else {
				ps.setNull(3, java.sql.Types.INTEGER);
			}
			ps.setString(4, createRequest.getValidationStatus().getText());
			ps.setString(5, createRequest.getCassandraTcId());
			return ps;
		}, keyHolder);
		createRequest.setTimeCreated(new Date());
		createRequest.setTimeUpdated(new Date());
		createRequest.setValidationId((long) keyHolder.getKey());
		return createRequest;
	}

	public int updateReplayId(Long originalValidationId, Long replayValidationId) {
		return jdbcTemplate.update(UPDATE_REPLAY_ID, replayValidationId, new Date(), originalValidationId);
	}

	public int updateStatus(TestValidationData data) {
		return jdbcTemplate.update(UPDATE_STATUS, data.getValidationStatus().getText(), new Date(),
				data.getValidationId());
	}

	public int deleteById(Long validationId) {
		return jdbcTemplate.update(DELETE_VALIDATION, validationId);
	}
}
