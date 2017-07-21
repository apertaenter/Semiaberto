package br.com.liberdad.semiaberto.model;

public enum Contrato {

    SEIS(6*60*60*1000,4*60*60*1000), OITO(8*60*60*1000, 5*60*60*1000);

    private final int contrato;
    private final long nucleo;

    Contrato(int j, long a) {

        contrato = j;
        nucleo = a;

    }

    public int getContrato() {
        return contrato;
    }

    public long getNucleo() {
        return nucleo;
    }
}
