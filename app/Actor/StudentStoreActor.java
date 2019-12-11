package Actor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import model.Student;
import store.StudentRepositary;

public class StudentStoreActor extends AbstractActor {

	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	private Map<Integer, Student> students = new HashMap<>();

	private StudentRepositary repoStore;

	public static Props props() {

		return Props.create(StudentStoreActor.class);
	}

	{
		repoStore = new StudentRepositary();
	}

	@Override
	public Receive createReceive() {
		// TODO Auto-generated method stub
		return receiveBuilder().match(Student.class, s -> s.isUpdate(), this::updateStudent)
				.match(Student.class, s -> s.isCreate(), this::addStudent)
				.match(Student.class, s -> s.isDelete(), this::deleteStudent).match(String.class, this::getStudent)
				.matchAny(this::getAllStudents).build();
	}

	public void addStudent(Student student) throws JsonProcessingException {

		log.info("into the addStudent :" + getSender().toString());
		repoStore.createStudent(student);
		log.info("the sender is :" + getSender().toString());
		getSender().tell(Optional.of(student), getSelf());

	}

	public void getAllStudents(Object obj) {

		log.info("the sender is :" + getSender().toString());
		getSender().tell(Optional.of(new ArrayList<>(repoStore.fetchAllStudent())), getSelf());

	}

	public void getStudent(String id) {

		getSender().tell(Optional.ofNullable(repoStore.fetchStudentId(id)), getSelf());

	}

	public void updateStudent(Student student) throws JsonProcessingException {

		getSender().tell(Optional.of(repoStore.updateStudent(student)), getSelf());

	}

	public void deleteStudent(Student student) {

		getSender().tell(students.remove(student.getServiceId()) != null, getSelf());

	}

}
