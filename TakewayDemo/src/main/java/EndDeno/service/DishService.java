package EndDeno.service;


import EndDeno.domain.Dish;
import EndDeno.dto.DishDto;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;


public interface DishService extends IService<Dish> {

    /*
    * 新增菜品数据至dish表
    * 新增口味数据到dish_flavor表
    * */
    @Transactional
    public void  saveWithFlavor(DishDto dishDto);

    /*
    * 根据id查找对应的dish表信息以及口味信息
    * */
    public DishDto getDishAndFlavorById(Long id);


    /*
    * 修改菜品信息
    * */
    @Transactional
    public void updateWithFlavor(DishDto dishDto);

}
