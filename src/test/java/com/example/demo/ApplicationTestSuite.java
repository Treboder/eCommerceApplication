package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
@SpringBootTest
@RunWith(Suite.class)
@Suite.SuiteClasses({CartControllerTest.class, UserControllerTest.class, ItemControllerTest.class, OrderControllerTest.class})
public class ApplicationTestSuite {

}
