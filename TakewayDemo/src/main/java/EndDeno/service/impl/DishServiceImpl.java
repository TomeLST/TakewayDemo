package EndDeno.service.impl;

import EndDeno.dao.DishDao;
import EndDeno.domain.Dish;
import EndDeno.domain.DishFlavor;
import EndDeno.dto.DishDto;
import EndDeno.service.DishFlavorService;
import EndDeno.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishDao, Dish>implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;


    @Override
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的信息至dish表
        this.save(dishDto);
        //获得当前菜品id
        Long dishId = dishDto.getId();
        //保存菜品口味至dish_flavor表
     List<DishFlavor> dishFlavorList = dishDto.getFlavors();
     dishFlavorList = dishFlavorList.stream().map((item)->
     {item.setDishId(dishId);
     return item;}).collect(Collectors.toList());

     dishFlavorService.saveBatch(dishFlavorList);
    }

    @Override
    public DishDto getDishAndFlavorById(Long id) {
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> dishFlavorList= dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(dishFlavorList);
        return dishDto;
    }

    @Override
    public void updateWithFlavor(DishDto dishDto) {
        Long dishId=dishDto.getId();
        //更新dish表
        this.updateById(dishDto);
        /*
        清理当前菜品对应的dish_flavor表的信息
        重新将编辑的口味添加进表里
        */
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        List<DishFlavor> dishFlavorList = dishDto.getFlavors();
        dishFlavorList = dishFlavorList.stream().map((item)->{

            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(dishFlavorList);
    }
}
