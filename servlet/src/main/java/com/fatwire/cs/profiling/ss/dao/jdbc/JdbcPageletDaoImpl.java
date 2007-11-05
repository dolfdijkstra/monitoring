package com.fatwire.cs.profiling.ss.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import com.fatwire.cs.profiling.ss.dao.PageletDao;
import com.fatwire.cs.profiling.ss.dao.TableCreator;
import com.fatwire.cs.profiling.ss.domain.Pagelet;

public class JdbcPageletDaoImpl extends AbstractJdbcCrud<Pagelet, Long>
        implements PageletDao, TableCreator {

    private static final ParameterizedUpdateMapper<Pagelet> parameterizedUpdateMapper = new ParameterizedUpdateMapper<Pagelet>() {

        public Object[] toArray(final Pagelet object) {
            return new Object[] { object.getNestedPagelets(),
                    object.getListedLinks(), object.getParams(), object.getId() };
        }

    };

    private static final ParameterizedUpdateMapper<Pagelet> parameterizedCreateMapper = new ParameterizedUpdateMapper<Pagelet>() {

        public Object[] toArray(final Pagelet object) {
            return new Object[] { object.getId(), object.getParams() };
        }

    };

    private static final ParameterizedRowMapper<Pagelet> mapper = new ParameterizedRowMapper<Pagelet>() {

        public Pagelet mapRow(ResultSet rs, int rowNum) throws SQLException {

            Pagelet pagelet = new Pagelet();
            pagelet.setId(rs.getLong("id"));
            String p = rs.getString("params");
            pagelet.setParams(p);
            return pagelet;
        }

    };

    public JdbcPageletDaoImpl() {
        super("Pagelet", new String[] { "id", "params" });
    }

    public Collection<Pagelet> search(String param) {
        // TODO Auto-generated method stub
        return null;
    }

    public void create(Pagelet e) {
        // TODO Auto-generated method stub

    }

    public void delete(Pagelet e) {
        this.deleteById(e.getId());

    }

    public void createTable() {
        // TODO Auto-generated method stub

    }

    @Override
    protected ParameterizedRowMapper<Pagelet> getMapper() {
        return mapper;
    }

    @Override
    protected ParameterizedUpdateMapper<Pagelet> getParameterizedCreateMapper() {
        return parameterizedCreateMapper;
    }

    @Override
    protected ParameterizedUpdateMapper<Pagelet> getParameterizedUpdateMapper() {
        return parameterizedUpdateMapper;
    }

}
