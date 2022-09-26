package EndDeno.controller;

import EndDeno.domain.Category;
import EndDeno.domain.Dish;
import EndDeno.domain.Result;
import EndDeno.domain.Setmeal;
import EndDeno.dto.DishDto;
import EndDeno.exception.RemoveDishException;
import EndDeno.service.CategoryService;
import EndDeno.service.DishFlavorService;
import EndDeno.service.DishService;
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
* 菜品管理
* */

@Slf4j
@RestController
@RequestMapping("/dish")

public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /*
     * 新增菜品
     * */
    @PostMapping
    public Result<String> addCategoty1(@RequestBody DishDto dishDto) {
        //log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return Result.success("新增菜品成功");
    }


    /*
     * 分页展示菜品
     * */

    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize, String name) {
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> pageallInfo = new Page<>();
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.like(name != null, Dish::getName, name);
        dishLambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo, dishLambdaQueryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo, pageallInfo, "records");
        List<Dish> records = pageInfo.getRecords();

        List<DishDto> listAll = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categortId = item.getCategoryId();
            Category category = categoryService.getById(categortId);
            String categortName = category.getName();
            dishDto.setCategoryName(categortName);
            return dishDto;
        }).collect(Collectors.toList());
        pageallInfo.setRecords(listAll);
        return Result.success(pageallInfo);

    }

    /*
    * 根据菜品id回显菜品信息
    * */
    @GetMapping("/{id}")
    public Result<DishDto> showDishById(@PathVariable Long id){
       DishDto dishDto = dishService.getDishAndFlavorById(id);
        return Result.success(dishDto);
    }


    /*
    * 修改菜品信息
    * */
    @PutMapping
    public Result<String> updateCategoty(@RequestBody DishDto dishDto) {
        //log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);
        return Result.success("修改菜品成功");
    }


    /*
    * 套餐页面获取每个菜品分类对应的菜品
    * */
    @GetMapping("/list")
    public Result<List<Dish>> GetDIshList(Dish dish){
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        dishLambdaQueryWrapper.like(dish.getName()!=null,Dish::getName,dish.getName());
        dishLambdaQueryWrapper.eq(Dish::getStatus,1);
        dishLambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> dishList = dishService.list(dishLambdaQueryWrapper);
        return Result.success(dishList);
    }


    /*
    * 批量停售菜品
    * */
    @PostMapping("/status/0")
    public Result<String> runDish(@RequestParam List<Long> ids){
        log.info(ids.toString());
        LambdaUpdateWrapper<Dish> dishLambdaQueryWrapper = new LambdaUpdateWrapper<>();
        dishLambdaQueryWrapper.in(ids!=null,Dish::getId,ids);
        dishLambdaQueryWrapper.set(Dish::getStatus,0);
        dishService.update(dishLambdaQueryWrapper);
        return Result.success("菜品停售成功");

    }

    /*
     * 批量启售菜品
     * */
    @PostMapping("/status/1")
    public Result<String> StopDish(@RequestParam List<Long> ids){
        log.info(ids.toString());
        LambdaUpdateWrapper<Dish> dishLambdaQueryWrapper = new LambdaUpdateWrapper<>();
        dishLambdaQueryWrapper.in(ids!=null,Dish::getId,ids);
        dishLambdaQueryWrapper.set(Dish::getStatus,1);
        dishService.update(dishLambdaQueryWrapper);
        return Result.success("菜品启售成功");

    }

    /*
    * 批量删除菜品
    * */
    @DeleteMapping
    public Result<String> deleteDish(@RequestParam List<Long> ids){
        log.info(ids.toString());
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper =new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.in(ids!=null,Dish::getId,ids);
        dishLambdaQueryWrapper.eq(Dish::getStatus,1);
        int count = dishService.count(dishLambdaQueryWrapper);
        if(count>0){
            throw new RemoveDishException("选择的菜品中包含处于在售状态的菜品，无法删除！");
        }
        dishService.removeByIds(ids);
        return Result.success("删除菜品成功");

    }

}
