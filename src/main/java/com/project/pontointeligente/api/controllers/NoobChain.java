package com.project.pontointeligente.api.controllers;

import com.project.pontointeligente.api.entities.Lancamento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NoobChain {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoobChain.class);

    public static Boolean isChainValid(List<Lancamento> blockchain) {
        Lancamento currentBlock;
        Lancamento previousBlock;

        //loop through blockchain to check hashes:
        for (int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);
            //compare registered hash and calculated hash:
            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                LOGGER.error("Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
                System.out.println("Previous Hashes not equal");
                return false;
            }
        }
        return true;
    }
}
