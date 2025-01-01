package com.sky.service.impl;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordEditFailedException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.catalina.mbeans.ServiceMBean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对
        //MD5加密得到的哈希密码与数据库中的哈希密码进行比较
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     * @param employeeDTO 员工信息
     */
    @Override
    public void addEmployee(EmployeeDTO employeeDTO) {
        //操作持久层,放到实体类中更合适
        Employee employee = new Employee();
        //对象属性拷贝
        BeanUtils.copyProperties(employeeDTO,employee);
        //设置没被拷贝的属性
        //设置状态
        employee.setStatus(StatusConstant.ENABLE);//常量类

        //设置密码,加密md5加密
        String password = DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes());
        employee.setPassword(password);
        //设置时间
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());

        //设置当前记录创建人的id和修改人的id
        //TODO:后期需要改为当前登录用户的ID
        //TODO:利用ThreadLocal线程 在拦截器阶段 创建token时保存ID,在同一线程获取保存的ID
        //填充更新时间，更新人代码重复冗余,可以采用AOP面向切面的技术将冗余代码与业务代码分离.
        //TODO:注解+AOP 类似日志Demo
//        Long ID = BaseContext.getCurrentId();
//        employee.setCreateUser(ID);
//        employee.setUpdateUser(ID);

        //执行DAO(Mapper)层操作数据库
        employeeMapper.InsertEmployee(employee);
    }


    /**
     * 员工页面查询
     * @param employeePageQueryDTO 页面查询对象
     * @return
     */
    @Override
    public PageResult selectEmployeePage(EmployeePageQueryDTO employeePageQueryDTO) {
        //TODO:要先引入PageHelper依赖
        //TODO:page分页工具类,相当于拦截器,拦截sql语句,然后自动动态拼接sql语句,自己就不要写limit后面的限制了
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
        //注意:(模板)该工具返回结果为Page
        Page<Employee> page = employeeMapper.selectEmployeePage(employeePageQueryDTO);
        long total = page.getTotal();//获取查询总数
        List<Employee>record = page.getResult();//获取查询结果
        return new PageResult(total,record);//封装成PageResult返回
        //PageResult类中 为 总数+查询结果
    }

    /**
     * 管理账号权限
     * @param status 状态
     * @param id id
     */
    @Override
    public void EnableOrBan(int status, long id) {
        Employee employee = Employee.builder()
                        .id(id)
                        .status(status)
                        .build();

        employeeMapper.update(employee);
    }

    /**
     * 修改员工信息
     * @param employeeDTO dto
     */
    @Override
    public void modifyEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
        //TODO:AOP实现公共字段填充
//        employee.setUpdateUser(BaseContext.getCurrentId());
//        employee.setUpdateTime(LocalDateTime.now());
        employeeMapper.update(employee);
    }

    /**
     * 查找员工
     * @param id
     * @return
     */
    @Override
    public Employee getById(long id) {
        return employeeMapper.getById(id);
    }

    /**
     * 修改密码
     * @param passwordDTO 修改密码业务实体
     */
    @Override
    public void editPassword(PasswordEditDTO passwordDTO) {
        //注意
        //首先验证用户密码是否正确
        //TODO:利用ThreadLocal存储的线程信息,来获取登陆用户的id
        //TODO:在拦截器验证Token时,设置的线程变量
        Long id = BaseContext.getCurrentId();
        passwordDTO.setEmpId(id);
        Employee employee = employeeMapper.getById(id);
        String password = employee.getPassword();
        String oldPassword = passwordDTO.getOldPassword();
        //密码错误则抛出异常,全局异常处理类会捕捉异常
        //注意:全局处理器处理的为controller层,但是得益于方法调用栈
        //当该层异常未被捕获,java会根据方法调用栈,逐层向上抛出异常,直至被捕获.
        //方法调用链:Controller -> Service -> DAO
        String newPassword = DigestUtils.md5DigestAsHex(passwordDTO.getNewPassword().getBytes());
        passwordDTO.setNewPassword(newPassword);
        if(!DigestUtils.md5DigestAsHex(oldPassword.getBytes()).equals(password)){
            throw new PasswordEditFailedException(MessageConstant.PASSWORD_ERROR);
        }
        employeeMapper.editPassword(passwordDTO);
    }

}
