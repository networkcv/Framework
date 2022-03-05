package com.lagou.hot.po;

import java.io.Serializable;
import java.util.Date;

public class TJob implements Serializable {
    private int id;
    private String jname;//职位名称
    private String inc;//公司
    private Date jdate;//发布时间
    private String salary;//薪资
    private int degree;//学历 码表
    private int experience;//工作经验  码表

    public void setId(int id) {
        this.id = id;
    }

    public void setJname(String jname) {
        this.jname = jname;
    }

    public void setInc(String inc) {
        this.inc = inc;
    }

    public void setJdate(Date jdate) {
        this.jdate = jdate;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getId() {
        return id;
    }

    public String getJname() {
        return jname;
    }

    public String getInc() {
        return inc;
    }

    public Date getJdate() {
        return jdate;
    }

    public String getSalary() {
        return salary;
    }

    public int getDegree() {
        return degree;
    }


}
