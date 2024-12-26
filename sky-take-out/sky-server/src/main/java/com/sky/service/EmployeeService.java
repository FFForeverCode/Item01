package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

import java.util.List;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void addEmployee(EmployeeDTO employee);

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    PageResult selectEmployeePage(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 禁用获启用管理人员
     * @param status 状态
     * @param id id
     */
    void EnableOrBan(int status, long id);

    /**
     * 修改员工
     * @param employeeDTO
     */
    void modifyEmployee(EmployeeDTO employeeDTO);

    /**
     * 查找员工
     * @param id
     * @return
     */
    Employee getById(long id);

    /**
     * 修改密码
     * @param passwordDTO
     */
    void editPassword(PasswordEditDTO passwordDTO);
}
