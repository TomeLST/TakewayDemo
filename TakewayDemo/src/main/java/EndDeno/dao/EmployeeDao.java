package EndDeno.dao;

import EndDeno.domain.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeDao extends BaseMapper<Employee> {
}
