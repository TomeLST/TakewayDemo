package EndDeno.service.impl;

import EndDeno.dao.EmployeeDao;
import EndDeno.domain.Employee;
import EndDeno.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeDao, Employee> implements EmployeeService {
}
