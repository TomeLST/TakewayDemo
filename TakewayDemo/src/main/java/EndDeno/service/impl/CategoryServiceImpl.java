package EndDeno.service.impl;

import EndDeno.dao.CategoryDao;
import EndDeno.domain.Category;
import EndDeno.domain.Dish;
import EndDeno.domain.Setmeal;
import EndDeno.exception.RemoveCategotyException;
import EndDeno.service.CategoryService;
import EndDeno.service.DishService;
import EndDeno.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;
    /*
    * 根据删除菜品或者套餐的分类
    * */
    @Override
    public void remove(Long ids) {
        /*
        查询当前分类是否关联菜品 若关联则抛出业务异常
        */
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper =new LambdaQueryWrapper<>();
        //根据分类id进行查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,ids);
        int count = dishService.count(dishLambdaQueryWrapper);
        //count>0 说明当前分类关联了菜品
        if(count>0){
            //抛出异常
            throw new RemoveCategotyException("当前分类关联了发布的菜品，无法删除");
        }
        /*
        查询当前分类是否关联套餐 若关联则抛出业务异常
        */
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper =new LambdaQueryWrapper<>();
        //根据分类id进行查询
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,ids);
        int countmeal = setmealService.count(setmealLambdaQueryWrapper);
        //countmeal>0 说明当前分类关联了套餐
        if(countmeal>0){
            //抛出异常
            throw new RemoveCategotyException("当前分类关联了发布的套餐，无法删除");
        }
        super.removeById(ids);
        //若都没关联 则正常删除分类
    }
}
