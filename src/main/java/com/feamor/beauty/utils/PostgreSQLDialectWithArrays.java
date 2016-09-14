package com.feamor.beauty.utils;

import org.hibernate.dialect.PostgreSQL9Dialect;

import java.sql.Types;

/**
 * Created by Home on 09.05.2016.
 */
public class PostgreSQLDialectWithArrays extends PostgreSQL9Dialect {
    public PostgreSQLDialectWithArrays() {
        super();
        registerColumnType(Types.ARRAY, "integer[]");
        registerColumnType(Types.ARRAY, "character varying[]");
    }
}
