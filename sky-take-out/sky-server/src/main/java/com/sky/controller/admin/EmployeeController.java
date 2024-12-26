package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.iterators.EmptyOrderedIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工管理控制层")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;//DAO层
    @Autowired
    private JwtProperties jwtProperties;//JWT-token令牌
    @Autowired
    private HttpMessageConverters messageConverters;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("登录方法")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();//载荷
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());//ID作为信息放置到载荷中
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),//配置文件中配置好的密钥
                jwtProperties.getAdminTtl(),//配置文件配置好的令牌生效时间
                claims);//载荷

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }


    @ResponseBody
    @PostMapping
    @ApiOperation("新增员工")
    public Result addEmployee(@RequestBody EmployeeDTO employee){
        log.info("新增员工:{}", employee);//动态地将employee填充到{}中
        employeeService.addEmployee(employee);
        return Result.success();//只返回标记字即可,前端无需接收其他数据
    }

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return 返回Result,内部封装code、msg、查询结果列表
     */

    @ResponseBody
    @GetMapping("/page")
    @ApiOperation("员工分页查询")
    public Result<PageResult> selectEmployeePage(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("员工分页查询,参数位{}",employeePageQueryDTO);
        PageResult pageResult =  employeeService.selectEmployeePage(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("禁用或开启管理员权限")
    public Result<String> EnableOrBan(@PathVariable int status, long id){
        log.info("禁用或者启动账号{},{}",status,id);
        employeeService.EnableOrBan(status,id);
        return Result.success();

    }

    /**
     * 修改员工信息
     * @param employeeDTO 员工信息
     * @return
     */
    @PutMapping
    @ApiOperation("修改员工信息")
    public Result<String>modifyEmployee(@RequestBody  EmployeeDTO employeeDTO){
        log.info("编辑员工:{}",employeeDTO);
        employeeService.modifyEmployee(employeeDTO);
        return Result.success();
    }

    /**
     * 根据ID查找员工
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID查找员工")
    public Result<Employee>getById(@PathVariable Long id){
        log.info("根据ID查询员工:{}",id);
        return Result.success(employeeService.getById(id));
    }

    /**
     * 修改密码
     * @param passwordDTO
     * @return
     */
    @PutMapping("/editPassword")
    @ResponseBody
    @ApiOperation("修改密码")
    public Result<String> EditPassword(@RequestBody PasswordEditDTO passwordDTO){
        log.info("修改密码,id:{}",passwordDTO.getEmpId());
        employeeService.editPassword(passwordDTO);
        return Result.success();
    }



    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("退出方法")
    public Result<String> logout() {
        return Result.success();
    }

}
