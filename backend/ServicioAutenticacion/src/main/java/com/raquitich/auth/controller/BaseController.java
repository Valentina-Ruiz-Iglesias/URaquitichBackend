package com.raquitich.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

public interface BaseController<Req, ReqUpd, Res, ID> {

    @GetMapping
    ResponseEntity<List<Res>> getAll();

    @GetMapping("/{id}")
    ResponseEntity<Res> getById(@PathVariable ID id);

    @PostMapping
    ResponseEntity<Res> save(@RequestBody Req request);

    @PutMapping("/{id}")
    ResponseEntity<Res> update(@PathVariable ID id, @RequestBody ReqUpd request);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable ID id);
}
