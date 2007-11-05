package com.fatwire.cs.profiling.ss.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import com.fatwire.cs.profiling.ss.dao.CrawlSessionDao;
import com.fatwire.cs.profiling.ss.dao.TableCreator;
import com.fatwire.cs.profiling.ss.domain.CrawlSession;

public class JdbcCrawlSessionDaoImpl extends AbstractJdbcCrud<CrawlSession,Long> implements
        CrawlSessionDao, TableCreator {

    private static final ParameterizedUpdateMapper<CrawlSession> parameterizedUpdateMapper = new ParameterizedUpdateMapper<CrawlSession>() {

        public Object[] toArray(final CrawlSession object) {
            return new Object[] { object.getHostConfigId(),
                    object.getStartTime(), object.getEndTime(), object.getId() };
        }

    };

    private static final ParameterizedUpdateMapper<CrawlSession> parameterizedCreateMapper = new ParameterizedUpdateMapper<CrawlSession>() {

        public Object[] toArray(final CrawlSession object) {
            return new Object[] { object.getId(), object.getHostConfigId(),
                    object.getStartTime(), object.getEndTime() };
        }

    };

    private static final ParameterizedRowMapper<CrawlSession> mapper = new ParameterizedRowMapper<CrawlSession>() {

        public CrawlSession mapRow(ResultSet rs, int rowNum)
                throws SQLException {

            CrawlSession crawlSession = new CrawlSession();
            crawlSession.setId(rs.getLong("id"));
            crawlSession.setHostConfigId(rs.getLong("host_config"));
            crawlSession.setStartTime(rs.getDate("start_time"));
            crawlSession.setEndTime(rs.getDate("end_time"));
            return crawlSession;
        }

    };


    private final static String[] cols = new String[] { "id", "host_config",
            "start_time", "end_time" };

    public JdbcCrawlSessionDaoImpl() {
        super("CrawlSession", cols);
    }

    public void create(final CrawlSession e) {
        e.setId(this.keyGenerator.getNextKey());
        simpleJdbcTemplate.updateForObject(getInsertSql(),
                getParameterizedCreateMapper(), e);

    }

    public void delete(final CrawlSession e) {
        deleteById(e.getId());
    }


    public void createTable() {
        simpleJdbcTemplate
                .getJdbcOperations()
                .execute(
                        "create table "
                                + getTableName()
                                + " (id integer UNIQUE NOT NULL, host_config integer, start_time timestamp,end_time timestamp");

    }

    @Override
    protected ParameterizedRowMapper<CrawlSession> getMapper() {
        return mapper;
    }

    @Override
    protected ParameterizedUpdateMapper<CrawlSession> getParameterizedCreateMapper() {
        return parameterizedCreateMapper;
    }

    @Override
    protected ParameterizedUpdateMapper<CrawlSession> getParameterizedUpdateMapper() {
        return parameterizedUpdateMapper;
    }

}
