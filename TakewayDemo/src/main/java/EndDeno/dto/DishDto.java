

package EndDeno.dto;

import EndDeno.domain.Dish;
import EndDeno.domain.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/*
* 用于封装增加菜品时的数据类型
* */
@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
