package com.project.pontointeligente.api.controllers;

import com.project.pontointeligente.api.dtos.CentroCustoDto;
import com.project.pontointeligente.api.dtos.LancamentoDto;
import com.project.pontointeligente.api.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/centroCusto")
@CrossOrigin(origins = "*")
public class CentroCustoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CentroCustoController.class);

    @PostMapping
    public CentroCustoDto adicionar(@Valid @RequestBody CentroCustoDto centroCustoDto) {
        return null;
    }

    @PutMapping(value = "/{id}")
    public CentroCustoDto alterar(@PathVariable("id") Long id, @Valid @RequestBody CentroCustoDto centroCustoDto) {
        return null;
    }

    @DeleteMapping(value = "/{id}")
    public void remover(@PathVariable("id") Long id) {

    }

    @GetMapping(value = "/{id}")
    public CentroCustoDto exibir(@PathVariable("id") Long id) {
        return null;
    }

    @GetMapping(value = "/listAll")
    public List<CentroCustoDto> listarTodos() {
        return null;
    }
}
