package com.junorz.travelbook.controller;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.junorz.travelbook.context.consts.Messages;
import com.junorz.travelbook.context.dto.TravelBookCreateDto;
import com.junorz.travelbook.context.dto.TravelBookDto;
import com.junorz.travelbook.context.response.Response;
import com.junorz.travelbook.context.response.Status;
import com.junorz.travelbook.domain.AccessUrl;
import com.junorz.travelbook.domain.TravelBook;
import com.junorz.travelbook.service.AccessUrlService;
import com.junorz.travelbook.service.TravelBookService;

@RestController
@RequestMapping("/travelbooks")
@CrossOrigin("*")
public class TravelBookController {

    private final TravelBookService travelBookService;
    private final AccessUrlService accessUrlService;
    private final MessageSource messageSource;

    @Autowired
    public TravelBookController(TravelBookService travelBookService, AccessUrlService accessUrlService,
            MessageSource messageSource) {
        this.travelBookService = travelBookService;
        this.accessUrlService = accessUrlService;
        this.messageSource = messageSource;
    }

    @GetMapping("")
    public ResponseEntity<Response> findAll() {
        List<TravelBookDto> data = travelBookService.findAll().stream().map(travelBook -> TravelBookDto.of(travelBook))
                .collect(Collectors.toList());
        String message = messageSource.getMessage(Messages.FETCH_TRAVELBOOKS_SUCCESS, null, Locale.getDefault());
        return ResponseEntity.status(HttpStatus.OK).body(Response.of(data, message, Status.SUCCESS));
    }

    @GetMapping("/{url}")
    public ResponseEntity<Response> findByUrl(@PathVariable("url") String url) {
        AccessUrl accessUrl = accessUrlService.findByUrl(url);
        String message = null;
        if (accessUrl == null) {
            message = messageSource.getMessage(Messages.NO_ACCESSURL_FOUND, null, Locale.getDefault());
            return ResponseEntity.status(HttpStatus.OK).body(Response.of(null, message, Status.FAILED));
        }
        TravelBook travelBook = accessUrl.getTravelBook();
        TravelBookDto data = TravelBookDto.of(travelBook);
        message = messageSource.getMessage(Messages.FETCH_TRAVELBOOK_BY_URL_SUCCESS, null, Locale.getDefault());
        return ResponseEntity.status(HttpStatus.OK).body(Response.of(data, message, Status.SUCCESS));
    }

    @PostMapping("/create")
    public ResponseEntity<Response> create(@RequestBody @Valid TravelBookCreateDto dto) {
        TravelBook travelBook = travelBookService.create(dto);
        TravelBookDto data = TravelBookDto.of(travelBook);
        String message;
        if (data == null) {
            message = messageSource.getMessage(Messages.TRAVELBOOK_CREATE_FAILED, null, Locale.getDefault());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.of(data, message, Status.FAILED));
        }
        message = messageSource.getMessage(Messages.TRAVELBOOK_CREATE_SUCCESS, null, Locale.getDefault());
        return ResponseEntity.status(HttpStatus.OK).body(Response.of(data, message, Status.SUCCESS));
    }

    // Enter password and get a jwt token
    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody Map<String, String> res) {
        String id = res.get("travelBookId");
        String password = res.get("password");
        String message = null;
        if (StringUtils.isNotEmpty(id) && StringUtils.isNotEmpty(password)) {
            String token = travelBookService.login(id, password);
            if (token != null) {
                message = messageSource.getMessage(Messages.AUTHENTICATION_SUCCESS, null, Locale.getDefault());
                return ResponseEntity.status(HttpStatus.OK).body(Response.of(token, message, Status.SUCCESS));
            }
        }
        message = messageSource.getMessage(Messages.AUTHENTICATION_FAILED, null, Locale.getDefault());
        return ResponseEntity.status(HttpStatus.OK).body(Response.of(null, message, Status.FAILED));
    }

}
