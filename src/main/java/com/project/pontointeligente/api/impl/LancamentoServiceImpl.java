package com.project.pontointeligente.api.impl;

import com.project.pontointeligente.api.dtos.LancamentoDto;
import com.project.pontointeligente.api.entities.Funcionario;
import com.project.pontointeligente.api.security.JwtUser;
import com.project.pontointeligente.api.services.LancamentoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.Optional;

public class LancamentoServiceImpl  {

    public static Logger log = LoggerFactory.getLogger(LancamentoServiceImpl.class);


}
