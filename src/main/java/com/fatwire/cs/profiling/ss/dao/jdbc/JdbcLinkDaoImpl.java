package com.fatwire.cs.profiling.ss.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import com.fatwire.cs.profiling.ss.dao.LinkDao;
import com.fatwire.cs.profiling.ss.dao.TableCreator;
import com.fatwire.cs.profiling.ss.domain.Link;

public class JdbcLinkDaoImpl extends AbstractJdbcCrud<Link, Long> implements
        LinkDao, TableCreator {
    private static final ParameterizedUpdateMapper<Link> parameterizedUpdateMapper = new ParameterizedUpdateMapper<Link>() {

        public Object[] toArray(final Link object) {
            return new Object[] { object.getUrl(), object.getId() };
        }

    };

    private static final ParameterizedUpdateMapper<Link> parameterizedCreateMapper = new ParameterizedUpdateMapper<Link>() {

        public Object[] toArray(final Link object) {
            return new Object[] { object.getId(), object.getUrl() };
        }

    };

    private static final ParameterizedRowMapper<Link> mapper = new ParameterizedRowMapper<Link>() {

        public Link mapRow(ResultSet rs, int rowNum) throws SQLException {

            Link link = new Link();
            link.setId(rs.getLong("id"));
            link.setUrl(rs.getString("url"));
            return link;
        }

    };

    public JdbcLinkDaoImpl() {
        super("Link", new String[] { "id", "uri" });
    }

    public void create(Link e) {
        e.setId(this.keyGenerator.getNextKey());
        simpleJdbcTemplate.updateForObject(getInsertSql(),
                getParameterizedCreateMapper(), e);
    }

    public void delete(Link e) {
        deleteById(e.getId());
    }

    public void createTable() {
        // TODO Auto-generated method stub

    }

    @Override
    protected ParameterizedRowMapper<Link> getMapper() {
        return mapper;
    }

    @Override
    protected ParameterizedUpdateMapper<Link> getParameterizedCreateMapper() {
        return parameterizedCreateMapper;
    }

    @Override
    protected ParameterizedUpdateMapper<Link> getParameterizedUpdateMapper() {
        return parameterizedUpdateMapper;
    }

}
