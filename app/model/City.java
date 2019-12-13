package model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class City {

	@NotNull
	@Size(min = 5, max = 10)
	private String cName ;
	
	@NotNull
	@Size(min = 5, max = 10)
	private String country;
	
	private String street;
	
}
