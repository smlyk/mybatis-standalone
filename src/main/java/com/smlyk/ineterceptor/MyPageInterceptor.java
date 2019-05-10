package com.smlyk.ineterceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.util.Properties;

import static java.util.Objects.isNull;

/**
 * 拦截Executor的query方法
 * <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler) throws SQLException;
 * @author yekai
 */
@Slf4j
@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class MyPageInterceptor implements Interceptor{

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        log.info("将逻辑分页改为物理分页");

        Object[] args = invocation.getArgs();
        MappedStatement ms= (MappedStatement) args[0];
        BoundSql boundSql = ms.getBoundSql(args[1]);
        RowBounds rowBounds = (RowBounds) args[2];

        //RowBounds为空，无需分页
        if (isNull(rowBounds)){
            args[2] = RowBounds.DEFAULT;
            return invocation.proceed();
        }

        //分页，在SQL后加上limit语句
        String sql = boundSql.getSql();
        String limit = String.format(" LIMIT %d,%d", rowBounds.getOffset(),rowBounds.getLimit());
        sql = sql + limit;
        // 自定义sqlSource
        SqlSource sqlSource = new StaticSqlSource(ms.getConfiguration(), sql, boundSql.getParameterMappings());
        // 修改原来的sqlSource
        Field sqlSourceField = MappedStatement.class.getDeclaredField("sqlSource");
        sqlSourceField.setAccessible(true);
        sqlSourceField.set(ms, sqlSource);

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
