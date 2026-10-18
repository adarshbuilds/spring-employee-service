package com.adarshpandey.testing.testing.module7;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Slf4j
class ApplicationTests {

	@BeforeEach
	void setUp(){
		log.info("starting the method setting up config");
	}

	@AfterEach
	void tearDown(){
		log.info("Tearing down the method");
	}

	@BeforeAll
	static void setUpOnce(){
		log.info("Setup Once ...");
	}

	@AfterAll
	static void tearDownOnce(){
		log.info("Tearing down Once ....");
	}




//	@Test
////	@Disabled
//	void testNumberOne() {
//		log.info("test one is run");
//	}





//	@Test
////	@DisplayName("displayTestNameTwo")
//	void testNumberTwo(){
//		log.info("test two is run");
//	}




	@Test
	void testNumberOne() {
		int a=5;
		int  b=3;
		int result = addTwoNumber(a,b);
//		Assertions.assertEquals(8,result);
//		Assertions.assertThat(result).isEqualTo(8).isCloseTo(9, Offset.offset(1));

		assertThat(result).isEqualTo(8).isCloseTo(9, Offset.offset(1));
		assertThat("Apple")
				.isEqualTo("Apple")
				.startsWith("App")
				.endsWith("le")
				.hasSize(5);

	}

	int addTwoNumber(int a,int b){
		return a+b;
	}


	@Test
	void testDivideTwoNumber_whenDenominatorIsZero_ThenArithmeticException(){
		int a=5;
		int b=0;

//		double result = divideTwoNumber(a,b);
//		System.out.println(result);

		assertThatThrownBy(() -> divideTwoNumber(a,b))
				.isInstanceOf(ArithmeticException.class)
				.hasMessage("Tried to divide by 0");
	}




	double divideTwoNumber(int a,int b){
		try {
			return  a/b;
		} catch (Exception e){
			log.error("Arithmetic exception occurred: "+e.getLocalizedMessage());
//			throw new ArithmeticException(e.getLocalizedMessage());
			throw new ArithmeticException("Tried to divide by 0");

		}
	}



}
