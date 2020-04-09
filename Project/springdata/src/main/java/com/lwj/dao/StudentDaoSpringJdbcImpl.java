package com.lwj.dao;

import com.lwj.DO.Student;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * create by lwj on 2020/4/8
 */
public class StudentDaoSpringJdbcImpl implements StudentDao {
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Student> query() {
        List<Student> list = new ArrayList<>();
        String sql = "select * from student";
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    Student student = new Student(resultSet.getInt(1), resultSet.getString(2),
                            resultSet.getInt(3));
                    list.add(student);
                }
            }
        });
        return list;
    }

    @Override
    public void save(Student student) {
        String sql = "insert into student(name,age) values(?,?)";
        jdbcTemplate.update(sql, student.getName(), student.getAge());
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
