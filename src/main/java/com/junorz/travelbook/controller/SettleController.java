package com.junorz.travelbook.controller;

import com.junorz.travelbook.context.consts.Messages;
import com.junorz.travelbook.context.dto.SettleCreateDto;
import com.junorz.travelbook.context.dto.SettleDto;
import com.junorz.travelbook.context.response.Response;
import com.junorz.travelbook.service.SettleService;
import com.junorz.travelbook.utils.ControllerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/travelbooks/settles")
public class SettleController {

    private final SettleService settleService;
    private final Response response;

    @Autowired
    public SettleController(SettleService settleService, Response response) {
        this.settleService = settleService;
        this.response = response;
    }

    @GetMapping("/{travelBookId}")
    public ResponseEntity<Response> findAll(@PathVariable("travelBookId") String travelBookId) {
        List<SettleDto> data = settleService.findAll(travelBookId).stream()
                .map(SettleDto::of)
                .collect(Collectors.toList());
        return ControllerUtil.ok(response.of(data, Messages.FETCH_SETTLES_SUCCESS));
    }

    @PostMapping("/create")
    public ResponseEntity<Response> create(@Valid @RequestBody SettleCreateDto dto) {
        SettleDto data = SettleDto.of(settleService.create(dto));
        return ControllerUtil.ok(response.of(data, Messages.SETTLE_CREATE_SUCCESS));
    }

}
