package com.gr.his.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public class Db implements Serializable  {

    private @Getter @Setter String dbName;
    private @Getter @Setter String dbTable;
    private @Getter @Setter String tbDato;

    public Db(String dbName, String dbTable, String tbDato) {
        this.dbName = dbName;
        this.dbTable = dbTable;
        this.tbDato = tbDato;
    }


    @Override
    public String toString() {
        return "Db{" +
                "dbName='" + dbName + '\'' +
                ", dbTable='" + dbTable + '\'' +
                ", tbDato='" + tbDato + '\'' +
                '}';
    }
}
