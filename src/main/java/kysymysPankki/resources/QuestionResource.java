package kysymysPankki.resources;

import kysymysPankki.api.Answer;
import kysymysPankki.api.Error;
import kysymysPankki.api.Question;
import kysymysPankki.db.QuestionDao;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.SQLException;
import java.util.List;

@Path("/questions")
@Produces(MediaType.APPLICATION_JSON)
public class QuestionResource {
    private final String template;
    private final String defaultName;
    private final QuestionDao model;

    public QuestionResource(String template, String defaultName, QuestionDao model) {
        this.template = template;
        this.defaultName = defaultName;
        this.model = model;
    }

    @GET
    public List<Question> getQuestions() throws SQLException, ClassNotFoundException {
        return model.findAll();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getQuestion(@PathParam("id") String id) throws SQLException, ClassNotFoundException {
        Question question = model.findById(Integer.parseInt(id));

        if (question != null) {
            return Response.ok(question).build();
        }

        return Response.status(Response.Status.NOT_FOUND).entity(new Error(404, "Question not found")).build();
    }

    @GET
    @Path("{id}/answers")
    public List<Answer> getAnswers(@PathParam("id") String id) throws SQLException, ClassNotFoundException {
        return model.findAllAnswers(Integer.parseInt(id));
    }

    @DELETE
    @Path("{id}")
    public Response deleteQuestion(@PathParam("id") String id) throws SQLException, ClassNotFoundException {
        int deleted = model.deleteQuestion(Integer.parseInt(id));

        if (deleted > 0) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(new Error(400, "Question could not be deleted")).build();
    }

    @DELETE
    @Path("{id}/answers/{answerId}")
    public Response deleteAnswer(
            @PathParam("id") String id,
            @PathParam("answerId") String answerId
    ) throws SQLException, ClassNotFoundException {
        int deletedRows = model.deleteAnswer(Integer.parseInt(id), Integer.parseInt(answerId));

        if (deletedRows > 0) {
           return Response.status(Response.Status.NO_CONTENT).build();
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(new Error(400, "Answer could not be deleted")).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createQuestion(
            @FormParam("course") String course,
            @FormParam("subject") String subject,
            @FormParam("text") String text
    ) throws SQLException, ClassNotFoundException {
        Question question = model.insertQuestion(text, course, subject);

        if (question != null) {
            return Response.ok(question).build();
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(new Error(400, "Question could not be created")).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("{id}/answers")
    public Response createAnswer(
            @FormParam("answer") String text,
            @FormParam("correct") String correct,
            @PathParam("id") String id
    ) throws SQLException, ClassNotFoundException {
        Answer answer = model.insertAnswer(Integer.parseInt(id), Boolean.parseBoolean(correct), text);

        if (answer != null) {
            return Response.ok(answer).build();
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(new Error(400, "Answer could not be created")).build();
    }

}
