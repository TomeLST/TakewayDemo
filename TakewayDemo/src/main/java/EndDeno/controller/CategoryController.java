package EndDeno.controller;

import EndDeno.domain.Category;
import EndDeno.domain.Result;
import EndDeno.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;


    /*
    * 新增分类
    * */
    @PostMapping
    public Result<String> Save(@RequestBody Category category){
        //log.info("category:{}",category);
        categoryService.save(category);
        return Result.success("操作成功！");
    }

    /*
    * 对菜品信息进行分页展示
    * */
    @GetMapping("/page")
    public Result<Page> page(int page,int pageSize){
        //log.info("执行分页查询...");
        Page<Category> pageInfo=new Page<>(page,pageSize,true);
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo,lambdaQueryWrapper);
        return Result.success(pageInfo);
    }


    /*
    * 删除菜品信息
    * */
    @DeleteMapping
    public Result<String> delete(Long ids){
      //  log.info("删除分类 id为{}",ids);
        /*categoryService.removeById(ids);*/
        categoryService.remove(ids);
      //  log.info("删除分类成功");
        return Result.success("删除分类成功");

    }


    /*
    * 修改分类信息
    * */
    @PutMapping
    public Result<String> update(@RequestBody Category category){
       //log.info("修改分类信息：catrgory",category);
       categoryService.updateById(category);
       return Result.success("修改成功！");
    }

    /*
    * 根据条件查询菜品的所有类别
    * */
    @GetMapping("/list")
    public Result<List<Category>>  getCategoryList(Category category){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        queryWrapper.orderByAsc(Category::getSort);
        List<Category> list = categoryService.list(queryWrapper);
        return Result.success(list);

    }
}
