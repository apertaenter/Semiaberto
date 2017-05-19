package br.com.liberdad.semiaberto.model;

public class Jornada {

    private long jornada;
    private long almoco;

    public Jornada(long j) {
        this.jornada = j;
        calcularAlmoco();
    }

    public long getJornada() {
        return jornada;
    }

    public void setJornada(long j){
        this.jornada = j;
        calcularAlmoco();
    }

    public long getAlmoco() {
        return almoco;
    }

    private void calcularAlmoco(){

        if (jornada > 6*60*60*1000)
            almoco = 60*60*1000;
        else
            almoco = 15*60*1000;
    }
}
