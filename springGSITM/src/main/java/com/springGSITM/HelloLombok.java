package com.springGSITM;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class HelloLombok {
	private final String hello;
	private final int lombok;

	// 모든 final 필드를 초기화하는 생성자
// public HelloLombok(String hello, int lombok) {
// this.hello = hello;
// this.lombok = lombok;
// }
	public static void main(String[] args) {
		HelloLombok helloLombok = new HelloLombok("헬로", 5);
		System.out.println(helloLombok.getHello());
		System.out.println(helloLombok.getLombok());
	}
}
