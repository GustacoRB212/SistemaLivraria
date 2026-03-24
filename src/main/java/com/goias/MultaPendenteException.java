package com.goias;

public class MultaPendenteException extends Exception {
    public MultaPendenteException(double valorMulta) {
        super("Você possui multa pendente de R$ " + String.format("%.2f", valorMulta)
                + ". Quite o débito antes de realizar um novo empréstimo.");
    }
}