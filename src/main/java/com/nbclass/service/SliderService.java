package com.nbclass.service;

import com.nbclass.model.BlogSlider;
import javafx.scene.control.Slider;

import java.util.List;

/**
 * SliderService
 *
 * @author nbclass
 * @version V1.0
 * @date 2019-10-18
 */
public interface SliderService {

    List<BlogSlider> selectList(Integer type, String name);

    List<BlogSlider> selectByType(Integer type);

    void save(BlogSlider slider);

    void deleteBatch(Integer[] ids);
}
