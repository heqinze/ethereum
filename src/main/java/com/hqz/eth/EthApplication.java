package com.hqz.eth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication

public class EthApplication {

	public static void main(String[] args) {
		SpringApplication.run(EthApplication.class, args);
	}
//	public static void main(String[] args) {
//		A a = new A();
//		a.setA(1);
//	}
//
//	static class A {
//		int a;
//		B b = new B();
//
//		public A() {
//			System.out.println("aaa");
//		}
//
//		void setA(int a) {
//			this.a = a;
//			System.out.println("a");
//		}
//	}
//
//	static class B {
//		public B() {
//			System.out.println("bbb");
//		}
//	}


}
