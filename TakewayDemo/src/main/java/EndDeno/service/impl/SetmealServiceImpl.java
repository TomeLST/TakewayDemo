package EndDeno.service.impl;

import EndDeno.dao.SetmealDao;
import EndDeno.domain.*;
import EndDeno.dto.DishDto;
import EndDeno.dto.SetmealDto;
import EndDeno.exception.RemoveDishException;
import EndDeno.exception.RemoveSetmealException;
import EndDeno.service.SetmealDishService;
import EndDeno.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealDao, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    public void saveMealWithDish(SetmealDto setmealDto) {
        // 保存套餐的基本信息 setmeal表
        this.save(setmealDto);
        //保存套餐和菜品的关联信息 setmeal_dish表
        Long setmealId = setmealDto.getId();
        List<SetmealDish> setmealDishList = setmealDto.getSetmealDishes();
       setmealDishList= setmealDishList.stream().map((item)->{
            item.setSetmealId(setmealId);
            return item;
        }).collect(Collectors.toList());
       setmealDishService.saveBatch(setmealDishList);

    }

    @Override
    public void deleteMealWithDish(List<Long> ids) {
        //查询套餐状态是否可以删除
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.in(Setmeal::getId,ids);
        setmealLambdaQueryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(setmealLambdaQueryWrapper);
        if(count>0){
            throw new RemoveSetmealException("选择的套餐包含处于在售状态的套餐，无法删除！");
        }
        this.removeByIds(ids);
        //删除关系表setmeal_dish中的关联信息
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(setmealDishLambdaQueryWrapper);
    }

    @Override
    public SetmealDto getSetmealAndDishById(Long id) {
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);
        LambdaQueryWrapper<SetmealDish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> setmealDishList = setmealDishService.list(dishLambdaQueryWrapper);
        setmealDto.setSetmealDishes(setmealDishList);
        /*LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> dishFlavorList= dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(dishFlavorList);
        return dishDto;*/
        return setmealDto;
    }

    @Override
    public void updateWithSetmealDish(SetmealDto setmealDto) {
        Long setmealId=setmealDto.getId();
        //更新dish表
        this.updateById(setmealDto);
        /*
        清理当前菜品对应的dish_flavor表的信息
        重新将编辑的口味添加进表里
        */
        /*LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        List<DishFlavor> dishFlavorList = dishDto.getFlavors();
        dishFlavorList = dishFlavorList.stream().map((item)->{

            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(dishFlavorList);*/
        LambdaQueryWrapper<SetmealDish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishService.remove(dishLambdaQueryWrapper);
        List<SetmealDish> setmealDishList = setmealDto.getSetmealDishes();
       setmealDishList = setmealDishList.stream().map((item)->{

            item.setSetmealId(setmealId);
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishList);
    }
}
