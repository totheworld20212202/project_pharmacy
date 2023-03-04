package com.example.project

import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.containers.GenericContainer
import spock.lang.Specification

// redis의 경우 testcontainer가 없어서 밑에와 같이 따로 만들어준다고함.
@SpringBootTest
abstract class AbstractIntegrationContainerBaseTest extends Specification{
    static final GenericContainer MY_REDIS_CONTAINER

    static {
        MY_REDIS_CONTAINER = new GenericContainer<>("redis:6")
            .withExposedPorts(6379) // redis:6이미지를 연결지음 host포트는 알아서 지정되고, docker container안의 redis는 6379
        MY_REDIS_CONTAINER.start()
        System.setProperty("spring.redis.host", MY_REDIS_CONTAINER.getHost())   //host를 spring에 전달
        System.setProperty("spring.redis.port", MY_REDIS_CONTAINER.getMappedPort(6379).toString())  // host port를 spring에 전달
    }

}
