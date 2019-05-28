package com.junorz.travelbook.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;
import com.junorz.travelbook.context.consts.Messages;
import com.junorz.travelbook.context.dto.TravelBookCreateDto;
import com.junorz.travelbook.context.dto.TravelBookDto;
import com.junorz.travelbook.context.response.Response;
import com.junorz.travelbook.context.response.Status;
import com.junorz.travelbook.context.validator.ValidateType;
import com.junorz.travelbook.context.validator.Validator;
import com.junorz.travelbook.domain.TravelBook;
import com.junorz.travelbook.service.TravelBookService;
import com.junorz.travelbook.utils.ControllerUtil;
import com.junorz.travelbook.utils.MessageUtil;

@RestController
@RequestMapping("/api/travelbooks")
@CrossOrigin("*")
public class TravelBookController {

    private static final Logger logger = LoggerFactory.getLogger(TravelBookController.class);
    
    private final TravelBookService travelBookService;
    private final Response response;

    @Autowired
    public TravelBookController(TravelBookService travelBookService, Response response) {
        this.travelBookService = travelBookService;
        this.response = response;
    }

    @GetMapping("")
    public ResponseEntity<Response> findAll() {
        List<TravelBookDto> data = travelBookService.findAll().stream().map(travelBook -> TravelBookDto.of(travelBook))
                .collect(Collectors.toList());
        return ControllerUtil.ok(response.of(data, Messages.FETCH_TRAVELBOOKS_SUCCESS));
    }

    @GetMapping("/{url}")
    public ResponseEntity<Response> findByUrl(@PathVariable("url") String url) {
        TravelBook travelBook = travelBookService.findByUrl(url);
        Validator.validate(ValidateType.REQUIRED, travelBook, Messages.NO_TRAVELBOOK_FOUND);
        TravelBookDto data = TravelBookDto.of(travelBook);
        return ControllerUtil.ok(response.of(data, Messages.FETCH_TRAVELBOOK_BY_URL_SUCCESS));
    }
    
    @PutMapping("/{id}/edit")
    public ResponseEntity<Response> edit(@PathVariable("id") String id, @RequestBody Map<String, String> params) {
        String idInBody = params.get("travelBookId");
        // The travelbook's ID must not be null from request body,
        // and it should equals to the one in URL.
        Validator.validateTbId(id, idInBody);
        
        String name = params.get("name");
        String adminPassword = params.get("adminPassword");
        String currency = params.get("currency");
        Validator.validate(ValidateType.REQUIRED, name, Messages.BAD_REQUEST);
        Validator.validate(ValidateType.REQUIRED, adminPassword, Messages.BAD_REQUEST);
        Validator.validate(ValidateType.REQUIRED, currency, Messages.BAD_REQUEST);
        
        TravelBook travelBook = travelBookService.edit(id, name, adminPassword, currency);
        Validator.validate(ValidateType.REQUIRED, travelBook, Messages.NO_TRAVELBOOK_FOUND);
        TravelBookDto data = TravelBookDto.of(travelBook);
        return ControllerUtil.ok(response.of(data, Messages.TRAVELBOOK_EDIT_SUCCESS));
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Response> delete(@PathVariable("id") String id, @RequestBody Map<String, String> params) {
        String idInBody = params.get("travelBookId");
        Validator.validateTbId(id, idInBody);

        TravelBook travelBook = travelBookService.delete(id);
        Validator.validate(ValidateType.REQUIRED, travelBook, Messages.NO_TRAVELBOOK_FOUND);
        TravelBookDto data = TravelBookDto.of(travelBook);
        return ControllerUtil.ok(response.of(data, Messages.TRAVELBOOK_DELETE_SUCCESS));
    }

    @PostMapping("/create")
    public ResponseEntity<Response> create(@RequestBody @Valid TravelBookCreateDto dto) {
        TravelBook travelBook = travelBookService.create(dto);
        Validator.validate(ValidateType.REQUIRED, travelBook, Messages.TRAVELBOOK_CREATE_FAILED);

        TravelBookDto data = TravelBookDto.of(travelBook);
        logger.info(MessageUtil.getMessage(Messages.LOG_TRAVELBOOK_CREATE_SUCCESS), data.getId());
        return ControllerUtil.ok(response.of(data, Messages.TRAVELBOOK_CREATE_SUCCESS));
    }

    // Enter password and get a jwt token
    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody Map<String, String> res) {
        String id = res.get("travelBookId");
        String password = res.get("password");
        if (!Strings.isNullOrEmpty(id) && !Strings.isNullOrEmpty(password)) {
            String token = travelBookService.login(id, password);
            if (token != null) {
                return ControllerUtil.ok(response.of(token, Messages.AUTHENTICATION_SUCCESS));
            }
        }
        return ControllerUtil.unauthorized(response.of(null, Messages.AUTHENTICATION_FAILED, Status.FAILED));
    }

}
