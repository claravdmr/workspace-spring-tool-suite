package com.class4.alternativeexample;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnExpression("${type.class1:false} == false")
//type:class1 is defined in properties and can be called whatever we want!
public class Services2 implements ServiceInterface {

	public Services2() {
		System.out.println("Class2");
	}

}
