package EndDeno.service.impl;

import EndDeno.dao.DishFlavorDao;
import EndDeno.domain.DishFlavor;
import EndDeno.service.DishFlavorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorDao, DishFlavor> implements DishFlavorService {
}
