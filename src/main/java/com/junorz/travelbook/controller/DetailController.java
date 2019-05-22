package com.junorz.travelbook.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.junorz.travelbook.context.consts.Messages;
import com.junorz.travelbook.context.dto.DetailCreateDto;
import com.junorz.travelbook.context.dto.DetailDto;
import com.junorz.travelbook.context.response.Response;
import com.junorz.travelbook.service.DetailService;

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
        return ResponseEntity.status(HttpStatus.OK).body(response.of(data, Messages.DETAIL_CREATE_SUCCESS));
    }
     
}
