package com.junorz.travelbook.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.junorz.travelbook.context.consts.Messages;
import com.junorz.travelbook.context.dto.DetailCreateDto;
import com.junorz.travelbook.context.dto.DetailDto;
import com.junorz.travelbook.context.response.Response;
import com.junorz.travelbook.context.validator.ValidateType;
import com.junorz.travelbook.context.validator.Validator;
import com.junorz.travelbook.service.DetailService;
import com.junorz.travelbook.utils.ControllerUtil;

@RestController
@RequestMapping("/api/travelbooks/details")
@CrossOrigin("*")
public class DetailController {
    
    private final DetailService detailService;
    private final Response response;
    
    public DetailController(DetailService detailService, Response response) {
        this.detailService = detailService;
        this.response = response;
    }
    
    @PostMapping("/create")
    public ResponseEntity<Response> create(@RequestBody @Valid DetailCreateDto dto) {
        DetailDto data = DetailDto.of(detailService.create(dto));
        return ControllerUtil.ok(response.of(data, Messages.DETAIL_CREATE_SUCCESS));
    }
    
    @PutMapping("/{id}/edit")
    public ResponseEntity<Response> edit(@PathVariable("id") String id, @RequestBody @Valid DetailCreateDto dto) {
        DetailDto data = DetailDto.of(detailService.edit(id, dto));
        return ControllerUtil.ok(response.of(data, Messages.DETAIL_EDIT_SUCCESS));
    }
    
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Response> delete(@PathVariable("id") String id, @RequestBody Map<String, String> params) {
        String travelBookId = params.get("travelBookId");
        Validator.validate(ValidateType.REQUIRED, travelBookId, Messages.TRAVELBOOK_ID_NOT_SPECIFIED);
        DetailDto data = DetailDto.of(detailService.delete(id));
        return ControllerUtil.ok(response.of(data, Messages.DETAIL_DELETE_SUCCESS));
    }
     
}
