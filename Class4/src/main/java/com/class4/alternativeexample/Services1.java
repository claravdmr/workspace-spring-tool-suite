package com.class4.alternativeexample;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnExpression("${type.class1:false} == true")
//type:class1 is defined in properties and can be called whatever we want!
public class Services1 implements ServiceInterface {

	public Services1() {
		System.out.println("Class1");
	}

}
