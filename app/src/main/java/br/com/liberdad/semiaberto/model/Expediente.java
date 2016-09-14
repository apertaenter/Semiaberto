package br.com.liberdad.semiaberto.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Expediente {

    private boolean debito;

    private boolean atraso;

    private List<Date> marcacoes = new ArrayList<Date>();

    private Jornada jornada;

    private final Date INICIO_EXPEDIENTE;

    private final Date FIM_EXPEDIENTE;

    private final Date INICIO_ALMOCO;

    private final Date FIM_ALMOCO;

    private final Date INICIO_NUCLEO;

    private final Date FIM_NUCLEO;

    public Expediente() {

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
        INICIO_NUCLEO = calendar.getTime();

        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        FIM_NUCLEO = calendar.getTime();

    }

    public void setMarcacoes(List<Date> marcacoes) {

        this.marcacoes = marcacoes;

    }

    public void setJornada(int jornada) {

        switch (jornada) {

            case 5:
                this.jornada = Jornada.CINCO;
                break;
            case 6:
                this.jornada = Jornada.SEIS;
                break;
            case 8:
                this.jornada = Jornada.OITO;
                break;
            default:
                throw new IllegalArgumentException();
        }

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

        long umaHora = 60 * 60 * 1000;
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
        if (calcularAlmoco() < jornada.getAlmoco() * 60 * 1000) {

            pnaAlmoco = (jornada.getAlmoco() * 60 * 1000) - calcularAlmoco();

        }

        // Obter complemento de nucleo
        if ((calcularPresencasNucleo() - pnaAlmoco) < 5 * umaHora) {

            complementoNucleo = (5 * umaHora) - (calcularPresencasNucleo() - pnaAlmoco); // Total menos o que já cumpriu

            // Verificar se existe horas disponíveis para cumprir o horário núcleo
            if (FIM_NUCLEO.before(ultimaMarcacao)) { // Não existe mais núcleo a ser cumprido

                complementoNucleo = 0;
                atraso = true;

            } else {

                if ((FIM_NUCLEO.getTime() - ultimaMarcacao.getTime()) < complementoNucleo) { // Verificar disponibilidade de horário núcleo a ser cumprido

                    complementoNucleo = FIM_NUCLEO.getTime() - ultimaMarcacao.getTime();
                    atraso = true;

                }

            }

        }

        // Obter complemento de jornada
        if (calcularPresencasNucleo() - pnaAlmoco + calcularPresencasNaoNucleo() < jornada.getJornada() * umaHora) {

            complementoJornada = (jornada.getJornada() * umaHora) - (calcularPresencasNucleo() - pnaAlmoco + calcularPresencasNaoNucleo());

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
        if ( INICIO_NUCLEO.after(ultimaMarcacao) ) {

            if ( complementoJornada > (complementoNucleo + (INICIO_NUCLEO.getTime() - ultimaMarcacao.getTime())) ) {

                result.setTime(ultimaMarcacao.getTime() + complementoJornada);

            }else{

                result.setTime(INICIO_NUCLEO.getTime() + complementoNucleo);

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

                result = result + 5 * 60 * 60 * 1000; // +5h

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

                result = result + 9 * 60 * 60 * 1000; // +9h

            } else if (estaDentroHorarioNucleo(entrada) && estaDentroHorarioNucleo(saida)) { // B

                result = result + saida.getTime() - entrada.getTime();

            } else if (estaDentroHorarioNucleo(entrada)) { // E

                result = result + FIM_NUCLEO.getTime() - entrada.getTime();

            } else if (estaDentroHorarioNucleo(saida)) { // D

                result = result + saida.getTime() - INICIO_NUCLEO.getTime();

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

                result = result + INICIO_NUCLEO.getTime() - entrada.getTime() + saida.getTime() - FIM_NUCLEO.getTime();

            } else if (estaDentroHorarioNucleo(entrada) && estaDentroHorarioNucleo(saida)) { // B

                result = result + 0;

            } else if (!estaDentroHorarioNucleo(entrada) && !estaDentroHorarioNucleo(saida)) { // A ou C

                result = result + saida.getTime() - entrada.getTime();

            } else if (estaDentroHorarioNucleo(entrada)) { // E

                result = result + saida.getTime() - FIM_NUCLEO.getTime();

            } else if (estaDentroHorarioNucleo(saida)) { // D

                result = result + INICIO_NUCLEO.getTime() - entrada.getTime();

            }

        }

        return result;

    }

   private boolean estaDentroHorarioAlmoco(Date marcacao) {

        return marcacao.after(INICIO_ALMOCO) && marcacao.before(FIM_ALMOCO);
    }

    private boolean estaDentroHorarioNucleo(Date marcacao) {

        return marcacao.after(INICIO_NUCLEO) && marcacao.before(FIM_NUCLEO);
    }

    private boolean excedeTodoIntervaloAlmoco(Date saida, Date entrada) {

        return saida.before(INICIO_ALMOCO) && entrada.after(FIM_ALMOCO);
    }

    private boolean excedeTodoHorarioNucleo(Date entrada, Date saida) {

        return entrada.before(INICIO_NUCLEO) && saida.after(FIM_NUCLEO);
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