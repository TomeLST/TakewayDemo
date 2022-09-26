package EndDeno.controller;

import EndDeno.domain.Employee;
import EndDeno.domain.Result;
import EndDeno.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /*登录业务
     * 将员工的id存到session中
     * 通过request对象的getsession（）进行id获取
     *  */
    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        //1.将页面提交的密码进行md5加密处理
        String password = employee.getPassword();

        //2.根据用户名查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        //3.对查询结果进行判断
        if (emp == null) {
            return Result.error("没有该用户！");
        }
        //4.密码检查
        if (!emp.getPassword().equals(password)) {
            return Result.error("密码错误！");
        }
        //5.查看员工状态码 是否被禁用
        if (emp.getStatus() == 0) {
            return Result.error("账户被禁用！");
        }
        //6.登录成功 跳转页面并将用户id放入session中
        request.getSession().setAttribute("employeeId", emp.getId());
        return Result.success(emp);

    }

    /*
    退出业务
    * */
    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request) {
        //清理session中保存的员工id
        request.getSession().removeAttribute("employeeId");
        return Result.success("退出成功！");
    }

    /*
     * 新增员工
     * */
    @PostMapping
    public Result<String> save(@RequestBody Employee employee, HttpServletRequest request) {
        log.info("新增员工，员工信息:{}", employee.toString());
        //给员工设置默认密码
        employee.setPassword("123456");
        //设置当前系统时间
        //employee.setCreateTime(LocalDateTime.now());
        //设置更新时间
        //employee.setUpdateTime(LocalDateTime.now());
        //获得当前登录用户的id
        //Long empId = (Long) request.getSession().getAttribute("employeeId");
        //employee.setCreateUser(empId);
        //employee.setUpdateUser(empId);
         employeeService.save(employee);
        return Result.success("新增员工成功");
    }


    /*
     * 分页查询 前端返回的参数为 page页数和pageSize页面大小
     * 查询用户时额外需要一个name参数
     * await getMemberList(params)
     * */
    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize, String name) {
        //log.info("page={},pageSize={},n ame={}", page, pageSize, name);
        //构造分页构造器 page.total page.size
        Page pageinfo = new Page(page, pageSize, true);

        //构造条件构造器 当传入name参数时
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(name != null, Employee::getName, name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        //执行查询
        employeeService.page(pageinfo, queryWrapper);
        return Result.success(pageinfo);
    }


    /*更新员工状态码*/
    @PutMapping
    public Result<String> updateStatus(HttpServletRequest request, @RequestBody Employee employee) {
        //log.info(employee.toString());
        //Long id =Thread.currentThread().getId();
        //log.info("当前线程的id:{}",id);
//        js对long型数据处理会丢失精度  最多处理16位
        Long empId = (Long) request.getSession().getAttribute("employeeId");
        //employee.setUpdateTime(LocalDateTime.now());
       // employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        return Result.success("状态修改成功");
    }


    /*
    * 根据id查询员工信息
    * */
    @GetMapping("/{id}")
    public Result<Employee> geyById(@PathVariable Long id){
        log.info("根据id查询员工信息...");
        Employee employee = employeeService.getById(id);
        if(employee!=null){
            return Result.success(employee);
        }
       return Result.error("没有查询到员工信息...");
    }
}
