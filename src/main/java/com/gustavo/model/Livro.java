package com.gustavo.model;

public class Livro {
    private String nomeLivro;
    private String autor;
    private int id;
    private int qntdDisponivel;

    public Livro() {}

    public Livro (String nomeLivro, String autor, int qntdDisponivel, int id) {
        this.nomeLivro = nomeLivro;
        this.autor = autor;
        this.id = id;
        this.qntdDisponivel = qntdDisponivel;
    }

    public String getNomeLivro() {
        return nomeLivro;
    }

    public void setNomeLivro(String nomeLivro) {
        this.nomeLivro = nomeLivro;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQntdDisponivel() {
        return qntdDisponivel;
    }

    public void setQntdDisponivel(int qntdDisponivel) {
        this.qntdDisponivel = qntdDisponivel;
    }

    public String toString () {
        return "Nome do livro: " + nomeLivro + "\nId: " + id + "\nQuantidade disponivel: "
                    + qntdDisponivel + "\n=====================================";
    }

    public String toStringUsuario () {
        return "Nome do livro: " + nomeLivro + "\nId: " + id + "\n=====================================";
    }


}
