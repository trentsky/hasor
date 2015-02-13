/*
 * Copyright 2008-2009 the original 赵永春(zyc@hasor.net).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.hasor.db.ar.record.dialect;
import java.sql.SQLException;
import net.hasor.db.ar.record.SQLBuilder;
import net.hasor.db.ar.record.dialect.mysql.MySqlBuilder;
import net.hasor.db.ar.record.dialect.oracle.OracleSqlBuilder;
import net.hasor.db.ar.record.dialect.sqlserver.SqlServerSqlBuilder;
/**
 * 
 * @version : 2015年2月13日
 * @author 赵永春(zyc@hasor.net)
 */
public enum DialectEnum {
    /*MySQL方言*/
    MySql(MySqlBuilder.class),
    /*SQLServer 2000*/
    SqlServer(SqlServerSqlBuilder.class),
    /*Oracle方言*/
    Oracle(OracleSqlBuilder.class);
    //
    //
    DialectEnum(Class<? extends SQLBuilder> sqlBuilder) {
        this.sqlBuilder = sqlBuilder;
    }
    private Class<? extends SQLBuilder> sqlBuilder;
    public SQLBuilder createBuilder() throws SQLException {
        try {
            return this.sqlBuilder.newInstance();
        } catch (Exception e) {
            throw new SQLException(e.getMessage(), e);
        }
    }
}