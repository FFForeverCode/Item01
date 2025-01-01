package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 增加员工
     * 配置信息已开启驼峰命名法
     * @param person
     */
    @AutoFill(OperationType.INSERT)
    @Insert("insert into employee(name,username,password,phone,sex,id_number,create_time,update_time,create_user,update_user,status)" +
            "VALUES (#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{createTime},#{updateTime},#{createUser},#{updateUser},#{status})")
    void InsertEmployee(Employee person);

    /**
     * 使用PageHelper实现员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    Page<Employee>selectEmployeePage(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 更新员工 使用动态sql
     * @param employee
     */
    @AutoFill(value=OperationType.UPDATE)
    void update(Employee employee);

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    @Select("select* from employee where id = #{id}")
    Employee getById(long id);

    @AutoFill(value=OperationType.UPDATE)
    @Update("update employee set password=#{newPassword} where id = #{empId}")
    void editPassword(PasswordEditDTO passwordDTO);
}
