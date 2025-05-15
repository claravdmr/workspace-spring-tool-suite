package com.lombokTest;

import lombok.AllArgsConstructor;
import lombok.Data;
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

public class TestObject {

	private String attribute1;
	private float attribute2;
	private int attribute3;
	private boolean attribute4;

}
