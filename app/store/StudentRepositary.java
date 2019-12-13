package store;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.RawJsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.AsyncN1qlQueryResult;
import com.couchbase.client.java.query.N1qlQuery;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.Student;
import play.Logger;
import utils.CouchbaseConfig;

@Singleton
public class StudentRepositary {

	private static final Logger.ALogger logger = Logger.of(StudentRepositary.class);

	@Inject
	CouchbaseConfig couchbaseConfig;

	public List<Student> fetchAllStudent() {

		

		List<Student> serviceMasterList = couchbaseConfig.student.async()
				.query(N1qlQuery.simple("SELECT * FROM `Student`")).flatMap(AsyncN1qlQueryResult::rows)
				.map(data -> {
					return createObject(data.value().get("Student").toString());
				}).toList().timeout(10000000, TimeUnit.SECONDS).toBlocking().single();

		

		return serviceMasterList;
	}

	public Student fetchStudentId(String id) {
		RawJsonDocument jsonDocument = couchbaseConfig.student.get(id, RawJsonDocument.class);

		return createObject(jsonDocument.content());
	}

	public Student createStudent(Student std) throws JsonProcessingException {

		ObjectMapper objectMapper = new ObjectMapper();
		JsonObject user = JsonObject.fromJson(objectMapper.writeValueAsString(std));

		String id = UUID.randomUUID().toString();
		std.setId(id);
		JsonDocument doc = JsonDocument.create(id, user);

		couchbaseConfig.student.upsert(doc);

		return std;

	}

	public Student updateStudent(Student std) throws JsonProcessingException {

		ObjectMapper objectMapper = new ObjectMapper();
		JsonObject user = JsonObject.fromJson(objectMapper.writeValueAsString(std));

		JsonDocument doc = JsonDocument.create(std.getServiceId(), user);

		couchbaseConfig.student.upsert(doc);

		return std;

	}

	public String deleteStudent(String id) {
		RawJsonDocument jsonDocument = couchbaseConfig.student.remove(id, RawJsonDocument.class);

		return jsonDocument.id();
	}

	private Student createObject(String json) {

		Student std = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {

			std = objectMapper.readValue(json, Student.class);

			logger.info("the converted value is :" + std);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return std;
	}

}
