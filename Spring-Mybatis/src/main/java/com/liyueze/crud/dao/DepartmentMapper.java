package com.liyueze.crud.dao;

import com.liyueze.crud.bean.Department;
import java.util.List;

public interface DepartmentMapper {

    int deleteByPrimaryKey(Integer deptId);

    int insert(Department record);

    int insertSelective(Department record);

    List<Department> selectByMap(Object o);
}