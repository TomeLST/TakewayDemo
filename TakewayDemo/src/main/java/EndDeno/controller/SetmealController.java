package EndDeno.controller;

import EndDeno.domain.Category;
import EndDeno.domain.Dish;
import EndDeno.domain.Result;
import EndDeno.domain.Setmeal;
import EndDeno.dto.DishDto;
import EndDeno.dto.SetmealDto;
import EndDeno.service.CategoryService;
import EndDeno.service.DishService;
import EndDeno.service.SetmealDishService;
import EndDeno.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/*
* 套餐管理
* */

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;

    /*
    * 保存新增的套餐信息
    * */
    @PostMapping
    public Result<String> save(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());
        setmealService.saveMealWithDish(setmealDto);
        return Result.success("操作成功！");

    }

    /*
    * 分页查询展示套餐信息
    * 根据套餐名模糊查询
    * */
    @GetMapping("/page")
    public Result<Page> Page(int page,int pageSize,String name){
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> pageInfoAll = new Page<>();
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.like(name!=null,Setmeal::getName,name);
        setmealLambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo,setmealLambdaQueryWrapper);
        //对象拷贝 获取套餐所属类别CategoryName
        BeanUtils.copyProperties(pageInfo,pageInfoAll,"records");
        List<Setmeal> records = pageInfo.getRecords();
         List<SetmealDto> setmealAll =records.stream().map((item)->{
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            setmealDto.setCategoryName(categoryName);
            return setmealDto;
        }).collect(Collectors.toList());
        pageInfoAll.setRecords(setmealAll);
        return Result.success(pageInfoAll);
    }

    /*
    * 删除套餐
    * */
    @DeleteMapping
    public  Result<String> deleteSetmeal(@RequestParam List<Long> ids){

        //log.info("ids:{}",ids);
        setmealService.deleteMealWithDish(ids);
        return Result.success("套餐删除成功");
    }

    /*
    * 批量停售套餐
    * */
    @PostMapping("/status/0")
    public Result<String> StopSetmeal(@RequestParam List<Long> ids){
        //log.info(ids.toString());
        LambdaUpdateWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaUpdateWrapper<>();
        setmealLambdaQueryWrapper.in(ids!=null,Setmeal::getId,ids);
        setmealLambdaQueryWrapper.set(Setmeal::getStatus,0);
        setmealService.update(setmealLambdaQueryWrapper);
        return Result.success("套餐停售成功");

    }

    /*
     * 批量启售套餐
     * */
    @PostMapping("/status/1")
    public Result<String> RunSetmeal(@RequestParam List<Long> ids){
        //log.info(ids.toString());
        LambdaUpdateWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaUpdateWrapper<>();
        setmealLambdaQueryWrapper.in(ids!=null,Setmeal::getId,ids);
        setmealLambdaQueryWrapper.set(Setmeal::getStatus,1);
        setmealService.update(setmealLambdaQueryWrapper);
        return Result.success("套餐启售成功");

    }

    /*
    * 根据id回显套餐信息
    * */
    @GetMapping("/{id}")
    public Result<SetmealDto> showDishById(@PathVariable Long id){

        /*DishDto dishDto = dishService.getDishAndFlavorById(id);
        return Result.success(dishDto);*/
        SetmealDto setmealDto =  setmealService.getSetmealAndDishById(id);

        return Result.success(setmealDto);
    }

    /*
     * 修改套餐信息
     * */
    @PutMapping
    public Result<String> updateSetmeal(@RequestBody SetmealDto setmealDto) {
        //log.info(dishDto.toString());
        setmealService.updateWithSetmealDish(setmealDto);
        return Result.success("修改套餐成功");
    }
}

