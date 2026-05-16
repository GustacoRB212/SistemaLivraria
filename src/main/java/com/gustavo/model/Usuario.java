package com.gustavo.model;

import java.util.ArrayList;

public class Usuario {
    private String nomeUsuario;
    private String cpf;
    private ArrayList<Livro> livrosPegados = new ArrayList<>();
    private ArrayList<Livro> historico = new ArrayList<>();
    private double multaPendente = 0;
    private int id;

    public int getId() {
        return id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario(String nomeUsuario, String cpf) {
        this.nomeUsuario = nomeUsuario;
        this.cpf = cpf;
    }
    public Usuario() {}

    public ArrayList<Livro> getLivrosPegados() {
        return livrosPegados;
    }

    public void acidonarHistorico(Livro livro) {
        historico.add(livro);
    }

    public ArrayList<Livro> getHistorico() {
        return historico;
    }

    public double getMultaPendente() { return multaPendente; }
    public void adicionarMulta(double valor) { this.multaPendente += valor; }
    public void pagarMulta() { this.multaPendente = 0.0; }
    public boolean temMulta() { return multaPendente > 0.0; }
    public String getNomeUsuario() { return nomeUsuario; }

    public void setLivrosPegados(ArrayList<Livro> livrosPegados) {
        this.livrosPegados = livrosPegados;
    }
    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }
}
