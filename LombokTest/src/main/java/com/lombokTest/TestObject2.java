package com.lombokTest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@NoArgsConstructor
@AllArgsConstructor
//@RequiredArgsConstructor //this would create a constructor for only required fields i.e. any final attribtues
//@Getter
//@Setter
//@ToString
//@EqualsAndHashCode
@Data // this does the same as the above 4 combined.
@SuperBuilder
@EqualsAndHashCode(callSuper=true)

public class TestObject2 extends TestObject{

	private String attribute5;
	private float attribute6;
	private int attribute7;
	private boolean attribute8;

}
