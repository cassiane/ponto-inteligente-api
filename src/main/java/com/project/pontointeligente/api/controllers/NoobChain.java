package com.project.pontointeligente.api.controllers;

import com.project.pontointeligente.api.entities.Lancamento;

import java.util.List;

public class NoobChain {

    public static int difficulty = 5;

    public static Boolean isChainValid(List<Lancamento> blockchain) {
        Lancamento currentBlock;
        Lancamento previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        //loop through blockchain to check hashes:
        for (int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);
            //compare registered hash and calculated hash:
            if (!currentBlock.getHash().equals(currentBlock.calculateHashInsert())) {
                System.out.println("Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
                System.out.println("Previous Hashes not equal");
                return false;
            }
//            //check if hash is solved
//            if (!currentBlock.getHash().substring(0, difficulty).equals(hashTarget)) {
//                System.out.println("This block hasn't been mined");
//                return false;
//            }

        }
        return true;
    }
}
