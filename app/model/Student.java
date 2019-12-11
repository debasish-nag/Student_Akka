package model;

import java.util.UUID;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;
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
public class Student {
	
	
	@Id
	@Field
	private String serviceId = UUID.randomUUID().toString();

	@NotNull
	@Size(min = 5, max = 10)
	@Field
	private String firstName;
	
	@NotNull
	@Field
	private String lastName;

	@Min(18)
	@Field
	private int age;
	
	@Field
	private String id;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private boolean create;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private boolean update;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private boolean delete;


}
