package EndDeno.service;

import EndDeno.domain.Setmeal;
import EndDeno.dto.DishDto;
import EndDeno.dto.SetmealDto;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    @Transactional
    /*
    * 新增套餐保存套餐信息
    * */
    public void saveMealWithDish(SetmealDto setmealDto);



    /*
    * 删除套餐
    * */
    @Transactional
    public void deleteMealWithDish(List<Long> ids);

    /*
     * 根据id查找对应的Setmeal表信息以及dish信息
     * */
    public SetmealDto getSetmealAndDishById(Long id);

    /*
     * 修改菜品信息
     * */
    @Transactional
    public void updateWithSetmealDish(SetmealDto setmealDto);

}
