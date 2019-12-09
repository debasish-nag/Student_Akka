package controllers;

import static akka.pattern.Patterns.ask;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;

import Actor.StudentStoreActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.util.Timeout;
import model.Student;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import scala.compat.java8.FutureConverters;
import utils.Util;

public class StudentController extends Controller {
	private HttpExecutionContext ec;

	private ActorRef storeRef;

	@Inject
	public StudentController(HttpExecutionContext ec, ActorSystem system) {

		this.ec = ec;
		storeRef = system.actorOf(StudentStoreActor.props(), "store");

	}

	public CompletionStage<Result> create(Http.Request request) {
		JsonNode json = request.body().asJson();
		return supplyAsync(() -> {
			if (json == null) {
				return badRequest(Util.createResponse("Expecting Json data", false));
			}
			// create student and set create true
			Student std = Json.fromJson(json, Student.class);
			std.setCreate(true);
			// set the timeout for te asyc call
			Timeout timeout = Timeout.create(Duration.ofMillis(5000));
			// pass the measage with future completion
			CompletionStage<Object> completionStage = FutureConverters.toJava(ask(storeRef, std, timeout));
			// call the futrue aysnc for vale
			CompletableFuture<Object> ask = completionStage.toCompletableFuture();
			// merge the future from actor
			// get the value
			final Optional<Student> studentOptional = (Optional<Student>) ask.join();

			return studentOptional.map(student -> {
				JsonNode jsonObject = Json.toJson(student);
				return created(Util.createResponse(jsonObject, true));
			}).orElse(internalServerError(Util.createResponse("Could not create data.", false)));
		}, ec.current());
	}

	@SuppressWarnings("unchecked")
	public CompletionStage<Result> listStudents() {
		return supplyAsync(() -> {

			Timeout timeout = Timeout.create(Duration.ofMillis(5000));

			CompletionStage<Object> completionStage = FutureConverters.toJava(ask(storeRef, new Object(), timeout));

			CompletableFuture<Object> ask = completionStage.toCompletableFuture();

			final Optional<List<Student>> studentOptioanl = (Optional<List<Student>>) ask.join();

			return studentOptioanl.map(res -> {
				JsonNode jsonObject = Json.toJson(res);
				return created(Util.createResponse(jsonObject, true));
			}).orElse(internalServerError(Util.createResponse("Could not fetch data.", false)));
		}, ec.current());
	}

	public CompletionStage<Result> retrieve(int id) {
		return supplyAsync(() -> {

			Timeout timeout = Timeout.create(Duration.ofMillis(5000));

			CompletionStage<Object> completionStage = FutureConverters.toJava(ask(storeRef, new Integer(id), timeout));

			CompletableFuture<Object> ask = completionStage.toCompletableFuture();

			final Optional<Student> studentOptional = (Optional<Student>) ask.join();
			return studentOptional.map(student -> {
				JsonNode jsonObjects = Json.toJson(student);
				return ok(Util.createResponse(jsonObjects, true));
			}).orElse(notFound(Util.createResponse("Student with id:" + id + " not found", false)));
		}, ec.current());
	}

	public CompletionStage<Result> update(Http.Request request) {
		JsonNode json = request.body().asJson();
		return supplyAsync(() -> {
			if (json == null) {
				return badRequest(Util.createResponse("Expecting Json data", false));
			}

			Student std = Json.fromJson(json, Student.class);
			std.setUpdate(true);
			Timeout timeout = Timeout.create(Duration.ofMillis(5000));

			CompletionStage<Object> completionStage = FutureConverters.toJava(ask(storeRef, std, timeout));

			CompletableFuture<Object> ask = completionStage.toCompletableFuture();

			Optional<Student> studentOptional = (Optional<Student>) ask.join();
			return studentOptional.map(student -> {
				if (student == null) {
					return notFound(Util.createResponse("Student not found", false));
				}
				JsonNode jsonObject = Json.toJson(student);
				return ok(Util.createResponse(jsonObject, true));
			}).orElse(internalServerError(Util.createResponse("Could not update data.", false)));
		}, ec.current());
	}

	public CompletionStage<Result> delete(int id) {
		return supplyAsync(() -> {

			Student std = new Student();
			std.setId(id);
			std.setDelete(true);

			Timeout timeout = Timeout.create(Duration.ofMillis(5000));

			CompletionStage<Object> completionStage = FutureConverters.toJava(ask(storeRef, std, timeout));

			CompletableFuture<Object> ask = completionStage.toCompletableFuture();

			boolean status = (boolean) ask.join();
			if (!status) {
				return notFound(Util.createResponse("Student with id:" + id + " not found", false));
			}
			return ok(Util.createResponse("Student with id:" + id + " deleted", true));
		}, ec.current());
	}
}
