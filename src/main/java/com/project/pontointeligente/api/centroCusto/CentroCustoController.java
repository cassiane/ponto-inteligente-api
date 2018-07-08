package com.project.pontointeligente.api.centroCusto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/centro-custo")
@CrossOrigin(origins = "*")
public class CentroCustoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CentroCustoController.class);

    private CentroCustoService service;
    private CentroCustoDtoToCentroCusto converter;
    private CentroCustoToCentroCustoDto convertToDto;

    @Autowired
    public CentroCustoController(CentroCustoService service, CentroCustoDtoToCentroCusto converter, CentroCustoToCentroCustoDto convertToDto) {
        this.service = service;
        this.converter = converter;
        this.convertToDto = convertToDto;
    }

    @PostMapping
    public CentroCustoDto adicionar(@Valid @RequestBody CentroCustoDto centroCustoDto) {
        return convertToDto.convert(service.persistir(converter.convert(centroCustoDto)));
    }

    @PutMapping(value = "/{id}")
    public CentroCustoDto alterar(@PathVariable("id") Long id, @Valid @RequestBody CentroCustoDto centroCustoDto) {
        centroCustoDto.setId(id);
        return convertToDto.convert(service.persistir(converter.convert(centroCustoDto)));
    }

    @DeleteMapping(value = "/{id}")
    public void remover(@PathVariable("id") Long id) throws Exception {
        service.excluir(id);
    }

    @GetMapping(value = "/{id}")
    public CentroCustoDto exibir(@PathVariable("id") Long id) {
        return service.buscarPorId(id).get();
    }

    @GetMapping(value = "/listAll")
    public List<CentroCustoDto> listarTodos() {
        return service.listar();
    }
}
