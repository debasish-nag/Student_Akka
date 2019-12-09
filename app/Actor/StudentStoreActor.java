package Actor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import model.Student;

public class StudentStoreActor extends AbstractActor {

	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	private Map<Integer, Student> students = new HashMap<>();

	public static Props props() {

		return Props.create(StudentStoreActor.class);
	}

	@Override
	public Receive createReceive() {
		// TODO Auto-generated method stub
		return receiveBuilder().match(Student.class, s -> s.isUpdate(), this::updateStudent)
									.match(Student.class,s->s.isCreate(), this::addStudent)
									.match(Student.class,s->s.isDelete(), this::deleteStudent)
									.match(Integer.class, this::getStudent)
									.matchAny(this::getAllStudents).build();
	}

	public void addStudent(Student student) {

		log.info("into the addStudent :" + getSender().toString());
		int id = students.size();
		student.setId(id);
		students.put(id, student);
		log.info("the sender is :" + getSender().toString());
		getSender().tell(Optional.of(student), getSelf());

	}

	public void getAllStudents(Object obj) {

		log.info("the sender is :" + getSender().toString());
		getSender().tell(Optional.of(new ArrayList<>(students.values())), getSelf());

	}

	public void getStudent(int id) {

		getSender().tell(Optional.ofNullable(students.get(id)), getSelf());

	}

	public void updateStudent(Student student) {
		int id = student.getId();
		if (students.containsKey(id)) {
			students.put(id, student);
			getSender().tell(Optional.of(student), getSelf());
		}
		getSender().tell(Optional.empty(), getSelf());
	}
	
	 public void deleteStudent(Student student) {
		 
		 getSender().tell(students.remove(student.getId()) != null, getSelf());
	        
	    }

}
