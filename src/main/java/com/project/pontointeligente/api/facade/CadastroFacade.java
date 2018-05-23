package com.project.pontointeligente.api.facade;

import com.project.pontointeligente.api.dtos.CadastroPFDto;

public interface CadastroFacade<T> {

    T salvar(T dto);

}
