package com.junorz.travelbook.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.junorz.travelbook.context.consts.Messages;
import com.junorz.travelbook.context.dto.CategoryDto;
import com.junorz.travelbook.context.response.Response;
import com.junorz.travelbook.domain.PrimaryCategory;
import com.junorz.travelbook.service.CategoryService;
import com.junorz.travelbook.utils.ControllerUtil;

@RestController
@RequestMapping("/api/travelbooks/categories")
@CrossOrigin("*")
public class CategoryController {
    
    private final CategoryService categoryService;
    private final Response response;
    
    public CategoryController(CategoryService categoryService, Response response) {
        this.categoryService = categoryService;
        this.response = response;
    }
    
    @GetMapping("")
    public ResponseEntity<Response> findAll() {
        List<PrimaryCategory> primaryCategoryList = categoryService.findAll();
        List<CategoryDto> data = primaryCategoryList.stream().map(CategoryDto::of).collect(Collectors.toList());
        return ControllerUtil.ok(response.of(data, Messages.PRIMARY_CATEGORY_FETCH_SUCCESS));
    }
}
