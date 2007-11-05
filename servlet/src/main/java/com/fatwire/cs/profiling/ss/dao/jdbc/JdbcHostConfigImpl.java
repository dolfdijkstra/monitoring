package com.fatwire.cs.profiling.ss.dao.jdbc;

import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import com.fatwire.cs.profiling.ss.dao.HostConfigDao;
import com.fatwire.cs.profiling.ss.dao.TableCreator;
import com.fatwire.cs.profiling.ss.domain.HostConfig;

public class JdbcHostConfigImpl extends AbstractJdbcCrud<HostConfig, Long>
        implements HostConfigDao, TableCreator {

    private static final String[] cols = new String[] { "id", "host_name",
            "port", "domain" };

    private static final ParameterizedUpdateMapper<HostConfig> parameterizedCreateMapper = new ParameterizedUpdateMapper<HostConfig>() {

        public Object[] toArray(HostConfig e) {
            return new Object[] { e.getId(), e.getHostname(), e.getPort(),
                    e.getDomain() };
        }
    };

    private static final ParameterizedUpdateMapper<HostConfig> parameterizedUpdateMapper = new ParameterizedUpdateMapper<HostConfig>() {

        public Object[] toArray(HostConfig e) {

            return new Object[] { e.getHostname(), e.getPort(), e.getDomain(),
                    e.getId() };
        }
    };

    private final ParameterizedRowMapper<HostConfig> mapper = new ParameterizedRowMapper<HostConfig>() {

        public HostConfig mapRow(ResultSet rs, int rowNum) throws SQLException {
            HostConfig hostConfig = new HostConfig();
            hostConfig.setId(rs.getLong("id"));
            hostConfig.setHostname(rs.getString("host_name"));
            hostConfig.setPort(rs.getInt("port"));
            hostConfig.setDomain(rs.getString("domain"));
            return hostConfig;
        }
    };

    public JdbcHostConfigImpl() {
        super("HostConfig", cols);
    }

    public Collection<HostConfig> search(final URI uri) {
        final String readSql = "select id, host_name, port,domain from HostConfig where host_name = ? and port=? and domain=?";
        return simpleJdbcTemplate.query(readSql, mapper, uri.getHost(), uri
                .getPort(), uri.getPath());
    }

    public void create(final HostConfig e) {
        e.setId(this.keyGenerator.getNextKey());
        simpleJdbcTemplate.update(getInsertSql(),
                getParameterizedCreateMapper(), e);

    }

    public void delete(final HostConfig e) {
        deleteById(e.getId());

    }

    public void createTable() {
        simpleJdbcTemplate
                .getJdbcOperations()
                .execute(
                        "create table "
                                + getTableName()
                                + " (id integer UNIQUE NOT NULL, host_name varchar(255), port integer, domain varchar(255))");
    }

    @Override
    protected ParameterizedRowMapper<HostConfig> getMapper() {
        return mapper;
    }

    @Override
    protected ParameterizedUpdateMapper<HostConfig> getParameterizedCreateMapper() {
        return parameterizedCreateMapper;
    }

    @Override
    protected ParameterizedUpdateMapper<HostConfig> getParameterizedUpdateMapper() {
        return parameterizedUpdateMapper;
    }

}
