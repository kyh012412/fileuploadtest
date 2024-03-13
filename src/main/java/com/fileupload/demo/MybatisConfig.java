package com.fileupload.demo;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor //생성자 주입을 받겠다.
public class MybatisConfig {
	
	private final ApplicationContext applicationContext;
	
	//외부 설정을 해당 클래스의 필드에 바인딩하기 위한 어노테이션
	@ConfigurationProperties(prefix ="spring.datasource.hikari")
	@Bean
	public HikariConfig hikariConfig() {
		return new HikariConfig();
	}
	
	//유저네임이라던가 비밀번호에 대한 설정을 HikariConfig객체에 넣어줘야 한다.
	//application.yml에 파일을 작성한다.
	
	@Bean
	public DataSource dataSource() {
		return new HikariDataSource(hikariConfig());
	}
	
	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception{
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource());
		//classpath가 리소시스까지 경로를 알고잇음
		bean.setMapperLocations(applicationContext.getResources("classpath*:/mapper/*.xml"));
		bean.setConfigLocation(applicationContext.getResource("classpath:/config/config.xml"));
		
		try {
			SqlSessionFactory sqlSessionFactory = bean.getObject();
			sqlSessionFactory.getConfiguration().setMapUnderscoreToCamelCase(true); //카멜표기법 허용
			return sqlSessionFactory;
		} catch (Exception e) {
			System.out.println("마이바티스 기본 설정중에 예외 발생");
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	
}
