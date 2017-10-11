package br.com.liberdad.semiaberto.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Expediente {

    private boolean debito;

    private boolean atraso;

    private boolean nucleoFlex;

    private final long CINCO_HORAS_LONG = 5 * 60 * 60 * 1000;

    private final long OITO_HORAS_LONG = 8 * 60 * 60 * 1000;

    private final long NOVE_HORAS_LONG = 9 * 60 * 60 * 1000;

    private List<Date> marcacoes = new ArrayList<Date>();

    private Jornada jornada;

    private Contrato contrato;

    private final Date INICIO_EXPEDIENTE;

    private final Date FIM_EXPEDIENTE;

    private final Date INICIO_ALMOCO;

    private final Date FIM_ALMOCO;

    private Date inicioNucleo;

    private Date fimNucleo;

    public Expediente(boolean nucleoFlex) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        INICIO_EXPEDIENTE = calendar.getTime();

        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        FIM_EXPEDIENTE = calendar.getTime();

        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        INICIO_ALMOCO = calendar.getTime();

        calendar.set(Calendar.HOUR_OF_DAY, 16);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        FIM_ALMOCO = calendar.getTime();

        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        inicioNucleo = calendar.getTime();

        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        fimNucleo = calendar.getTime();

        setNucleoFlex(nucleoFlex);

    }

    public boolean isNucleoFlex() {
        return nucleoFlex;
    }

    public void setNucleoFlex(boolean nucleoFlex) {
        Calendar calendar = Calendar.getInstance();
        if (nucleoFlex){
            inicioNucleo = INICIO_EXPEDIENTE;

            fimNucleo = FIM_EXPEDIENTE;
        }else{
            calendar.set(Calendar.HOUR_OF_DAY, 9);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            inicioNucleo = calendar.getTime();

            calendar.set(Calendar.HOUR_OF_DAY, 18);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            fimNucleo = calendar.getTime();
        }
        this.nucleoFlex = nucleoFlex;
    }

    public void setMarcacoes(List<Date> marcacoes) {

        this.marcacoes = marcacoes;

    }

    public List<Date> getMarcacoes(){
        return marcacoes;
    }

    public void setJornada(long jornada) {

        if (null == this.jornada)
            this.jornada = new Jornada(jornada);
        else
            this.jornada.setJornada(jornada);

    }

    public void setContrato(int contrato){

        if (contrato == 6)
            this.contrato = Contrato.SEIS;
        else
            this.contrato = Contrato.OITO;
    }

    public Contrato getContrato() {

        return contrato;
    }

    public Date getUltimaSaidaProposta() {

        return calcularUltimaSaida();

    }

    private Date calcularUltimaSaida() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date result = calendar.getTime();

        //long umaHora = 60 * 60 * 1000;
        long pnaAlmoco = 0;
        long complementoNucleo = 0;
        long complementoJornada = 0;

        debito = false;
        atraso = false;

        if (marcacoes.size() == 0) {

            return result;
        }

        // Obter última entrada
        Date ultimaMarcacao = ajustaHorario(marcacoes.get(marcacoes.size()-1));

        // Obter PNA de almoço
        if (calcularAlmoco() < jornada.getAlmoco()) {

            pnaAlmoco = (jornada.getAlmoco()) - calcularAlmoco();

        }

        // Obter complemento de nucleo

            if ((calcularPresencasNucleo() - pnaAlmoco) < contrato.getNucleo()) {

                complementoNucleo = (contrato.getNucleo()) - (calcularPresencasNucleo() - pnaAlmoco); // Total menos o que já cumpriu

                // Verificar se existe horas disponíveis para cumprir o horário núcleo
                if (fimNucleo.before(ultimaMarcacao)) { // Não existe mais núcleo a ser cumprido

                    complementoNucleo = 0;
                    atraso = true;

                } else {

                    if ((fimNucleo.getTime() - ultimaMarcacao.getTime()) < complementoNucleo) { // Verificar disponibilidade de horário núcleo a ser cumprido

                        complementoNucleo = fimNucleo.getTime() - ultimaMarcacao.getTime();
                        atraso = true;

                    }

                }

            }


        // Obter complemento de jornada
        if (calcularPresencasNucleo() - pnaAlmoco + calcularPresencasNaoNucleo() < jornada.getJornada()) {

            complementoJornada = jornada.getJornada() - (calcularPresencasNucleo() - pnaAlmoco + calcularPresencasNaoNucleo());

            // Verificar se existe horas disponíveis para cumprir a jornada
            if ( FIM_EXPEDIENTE.before(ultimaMarcacao) ) {

                complementoJornada = 0;
                debito = true;

            } else {

                if ( (FIM_EXPEDIENTE.getTime() - ultimaMarcacao.getTime()) < complementoJornada ) {

                    complementoJornada = FIM_EXPEDIENTE.getTime() - ultimaMarcacao.getTime();
                    debito = true;

                }

            }

        }

        // Cálculo final - ALELUIA !!!
        if ( inicioNucleo.after(ultimaMarcacao) ) {

            if ( complementoJornada > (complementoNucleo + (inicioNucleo.getTime() - ultimaMarcacao.getTime())) ) {

                result.setTime(ultimaMarcacao.getTime() + complementoJornada);

            }else{

                result.setTime(inicioNucleo.getTime() + complementoNucleo);

            }

        } else { // Última entrada é igual ou maior que às 9h

            if ( complementoJornada > complementoNucleo ) {

                result.setTime(ultimaMarcacao.getTime() + complementoJornada);

            } else {

                result.setTime(ultimaMarcacao.getTime() + complementoNucleo);

            }

        }

        return result;

    }

    private long calcularAlmoco() {

        long result = 0;

        for (int i = 1; i < marcacoes.size() - 1; i += 2) { // pares de marcações para avaliar ausências

            Date saida = ajustaHorario(marcacoes.get(i));
            Date entrada = ajustaHorario(marcacoes.get(i + 1));

            // Verificar o intervalo válido de almoço acumulando valores válidos
            if (excedeTodoIntervaloAlmoco(saida, entrada)) { // F

                result = result + CINCO_HORAS_LONG; // +5h

            } else if (estaDentroHorarioAlmoco(saida) && estaDentroHorarioAlmoco(entrada)) { // B

                result = result + entrada.getTime() - saida.getTime();

            } else if (estaDentroHorarioAlmoco(saida)) { // E

                result = result + FIM_ALMOCO.getTime() - saida.getTime();

            } else if (estaDentroHorarioAlmoco(entrada)) { // D

                result = result + entrada.getTime() - INICIO_ALMOCO.getTime();

            }
        }

        return result;

    }

    private long calcularPresencasNucleo() {

        long result = 0;

        for (int i = 0; i < marcacoes.size() - 1; i += 2) { // pares de marcações para avaliar permanências

            Date entrada = ajustaHorario(marcacoes.get(i));
            Date saida = ajustaHorario(marcacoes.get(i + 1));

            if (excedeTodoHorarioNucleo(entrada, saida)) { // F

                result = result + NOVE_HORAS_LONG; // +9h

            } else if (estaDentroHorarioNucleo(entrada) && estaDentroHorarioNucleo(saida)) { // B

                result = result + saida.getTime() - entrada.getTime();

            } else if (estaDentroHorarioNucleo(entrada)) { // E

                result = result + fimNucleo.getTime() - entrada.getTime();

            } else if (estaDentroHorarioNucleo(saida)) { // D

                result = result + saida.getTime() - inicioNucleo.getTime();

            }

        }

        return result;

    }

    private long calcularPresencasNaoNucleo() {

        long result = 0;

        for (int i = 0; i < marcacoes.size() - 1; i += 2) { // pares de marcações para avaliar permanências

            Date entrada = ajustaHorario(marcacoes.get(i));
            Date saida = ajustaHorario(marcacoes.get(i + 1));

            if (excedeTodoHorarioNucleo(entrada, saida)) { // F

                result = result + inicioNucleo.getTime() - entrada.getTime() + saida.getTime() - fimNucleo.getTime();

            } else if (estaDentroHorarioNucleo(entrada) && estaDentroHorarioNucleo(saida)) { // B

                result = result + 0;

            } else if (!estaDentroHorarioNucleo(entrada) && !estaDentroHorarioNucleo(saida)) { // A ou C

                result = result + saida.getTime() - entrada.getTime();

            } else if (estaDentroHorarioNucleo(entrada)) { // E

                result = result + saida.getTime() - fimNucleo.getTime();

            } else if (estaDentroHorarioNucleo(saida)) { // D

                result = result + inicioNucleo.getTime() - entrada.getTime();

            }

        }

        return result;

    }

   private boolean estaDentroHorarioAlmoco(Date marcacao) {

        return marcacao.after(INICIO_ALMOCO) && marcacao.before(FIM_ALMOCO);
    }

    private boolean estaDentroHorarioNucleo(Date marcacao) {

        return marcacao.after(inicioNucleo) && marcacao.before(fimNucleo);
    }

    private boolean excedeTodoIntervaloAlmoco(Date saida, Date entrada) {

        return saida.before(INICIO_ALMOCO) && entrada.after(FIM_ALMOCO);
    }

    private boolean excedeTodoHorarioNucleo(Date entrada, Date saida) {

        return entrada.before(inicioNucleo) && saida.after(fimNucleo);
    }

    private Date ajustaHorario (Date horario){

        if (horario.before(INICIO_EXPEDIENTE)){
            return INICIO_EXPEDIENTE;
        }

        if (horario.after(FIM_EXPEDIENTE)) {
            return FIM_EXPEDIENTE;
        }

        return horario;

    }

    public boolean isDebito() {

        return debito;
    }

    public boolean isAtrasado() {

        return atraso;
    }
}