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
import java.util.*;
import javax.validation.Valid;

@NoArgsConstructor
@Getter
@Setter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class Adress {
	
	@NotNull
	@Size(min = 5, max = 10)
	private String house;
	
	@NotNull
	@Size(min = 5, max = 10)
	private String pathName;
	
	private String landMark;
	
	@NotNull
	@Valid
	List <City> cities;

}
