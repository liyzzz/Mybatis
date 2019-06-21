package com.liyueze.crud.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

//此注解是类注解，作用是json序列化时将java bean中的一些属性忽略掉，序列化和反序列化都受影响
@JsonIgnoreProperties(value = {"handler"})
public class Department implements Serializable{
    private Integer deptId;
    private String deptName;

    public Department(Integer deptId, String deptName) {
        this.deptId = deptId;
        this.deptName = deptName;
    }

    public Department() {
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName == null ? null : deptName.trim();
    }
}