package com.gustavo.dao;

import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class ConexaoTest {

    @Test
    void deveConectarNoBanco() {

        Connection connection = Conexao.getConnection();

        assertNotNull(connection);
    }

    @Test
    void conexaoDeveEstarAberta() throws Exception {

        Connection connection = Conexao.getConnection();

        assertFalse(connection.isClosed());
    }
}