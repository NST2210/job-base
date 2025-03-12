package com.gtel.api.interfaces.intercepter;

import com.gtel.api.configs.read_write_routing.DataSourceContextHolder;
import com.gtel.api.configs.read_write_routing.DataSourceType;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Log4j2
public class DataSourceAspect {
    @Before("execution(* com.gtel.api.infrastracture.repository.postgres..*.write*(..)) ||" +
            "execution(* com.gtel.api.infrastracture.repository.postgres..*.insert*(..)) ||" +
            "execution(* com.gtel.api.infrastracture.repository.postgres..*.update*(..)) ||" +
            "execution(* com.gtel.api.infrastracture.repository.postgres..*.delete*(..)) ||" +
            "execution(* com.gtel.api.infrastracture.repository..*.save*(..))")
    public void setWriteDataSourceType() {
        log.info("Writing to database");
        DataSourceContextHolder.setDataSourceType(DataSourceType.WRITE);
    }

    @Before("execution(* com.gtel.api.infrastracture.repository.postgres..*.find*(..)) ||" +
            "execution(* com.gtel.api.infrastracture.repository.postgres..*.get*(..)) ||" +
            "execution(* com.gtel.api.infrastracture.repository.postgres..*.read*(..))")
    public void setReadDataSourceType() {
        log.info("Reading from database");
        DataSourceContextHolder.setDataSourceType(DataSourceType.READ);
    }
}
