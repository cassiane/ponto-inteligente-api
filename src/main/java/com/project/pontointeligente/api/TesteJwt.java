package com.project.pontointeligente.api;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TesteJwt {

    public static void main (String [] args) {
        //Entradas do serviço: SenhaDeAssinatura e idDoLancamento
        //o método irá resgatar o funcionário a partir do token do serviço o email do funcionário logado
        //o método irá verificar se a senha informada é igual do cadastro do funcionário autenticado
        //Se for, irá buscar os dados do lançamento pelo id
        //Se o lançamento for encontrado irá gerar um token assinado pelo funcionário
        //Este token será o comprovante do funcionário e será gravado na coluna hash_assinado no lançamento informado
        String senhaDeAssinaturaDoFuncionario = "teste";
        String payloadExemplo = "{ " +
                "\"data\":\"2018-05-09 11:27:11\"" +
                "}";
        String token = Jwts.builder().setPayload(payloadExemplo)
                .signWith(SignatureAlgorithm.HS512, senhaDeAssinaturaDoFuncionario).compact();
        System.out.print(token);

    }
}
