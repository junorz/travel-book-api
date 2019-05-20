package com.junorz.travelbook.controller;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.context.MessageSource;
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
import com.junorz.travelbook.context.response.Status;
import com.junorz.travelbook.service.DetailService;

@RestController
@RequestMapping("/travelbooks/details")
@CrossOrigin("*")
public class DetailController {
    
    private final DetailService detailService;
    private final MessageSource messageSource;
    
    public DetailController(DetailService detailService, MessageSource messageSource) {
        this.detailService = detailService;
        this.messageSource = messageSource;
    }
    
    @PostMapping("/create")
    public ResponseEntity<Response> create(@RequestBody @Valid DetailCreateDto dto) {
        DetailDto data = DetailDto.of(detailService.create(dto));
        String message = messageSource.getMessage(Messages.DETAIL_CREATE_SUCCESS, null, Locale.getDefault());
        return ResponseEntity.status(HttpStatus.OK).body(Response.of(data, message, Status.SUCCESS));
    }
     
}
