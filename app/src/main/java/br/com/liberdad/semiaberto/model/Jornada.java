package br.com.liberdad.semiaberto.model;

public enum Jornada {

    QUATRO(4,15), CINCO(5, 15), SEIS(6, 15), OITO(8, 60);

    private final int jornada;
    private final int almoco;

    Jornada(int j, int a) {

        jornada = j;
        almoco = a;

    }

    public int getJornada() {
        return jornada;
    }

    public int getAlmoco() {
        return almoco;
    }
}
