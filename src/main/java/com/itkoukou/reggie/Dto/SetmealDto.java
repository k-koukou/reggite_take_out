package com.itkoukou.reggie.Dto;

import com.itkoukou.reggie.pojo.Setmeal;
import com.itkoukou.reggie.pojo.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
