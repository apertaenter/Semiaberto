package br.com.liberdad.semiaberto.model;

import java.util.Comparator;
import java.util.Date;

public class Comparador<T> implements Comparator<T>{

    @Override
    public int compare(T lhs, T rhs) {

        Date m1 = (Date)lhs;
        Date m2 = (Date)rhs;

        return m1.compareTo(m2);
    }
}