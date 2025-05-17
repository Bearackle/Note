package com.dinhhuan.note.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan({"com.dinhhuan.note.mapper" , "com.dinhhuan.note.dao"})
public class MybatisConfig {
}
