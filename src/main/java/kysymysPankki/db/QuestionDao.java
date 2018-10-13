package kysymysPankki.db;

import kysymysPankki.api.Answer;
import kysymysPankki.api.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDao {
    private String database;
    private String user;
    private String password;

    public QuestionDao(String database, String user, String password) {
        this.database = database;
        this.user = user;
        this.password = password;
    }

    private Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");

        return DriverManager.getConnection(database, user, password);
    }

    public Question findById(int id) throws SQLException, ClassNotFoundException {
        try (Connection conn = getConnection()) {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Kysymys WHERE id = ?");
            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                return new Question(
                        result.getInt("id"),
                        result.getString("teksti"),
                        result.getString("kurssi"),
                        result.getString("aihe")
                );
            }
        }

        return null;

    }

    public List<Question> findAll() throws SQLException, ClassNotFoundException{
        List<Question> questions = new ArrayList<>();
        try (Connection conn = getConnection(); ResultSet result = conn.prepareStatement("SELECT * FROM Kysymys").executeQuery()) {


            while (result.next()) {
                questions.add(new Question(
                        result.getInt("id"),
                        result.getString("teksti"),
                        result.getString("kurssi"),
                        result.getString("aihe")
                ));
            }
        }

        return questions;
    }

    public List<Answer> findAllAnswers(int id) throws SQLException, ClassNotFoundException {
        List<Answer> answers = new ArrayList<>();
        try (Connection conn = getConnection()) {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Vastaus WHERE kysymysId = ?");

            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                answers.add(new Answer(
                        result.getInt("id"),
                        result.getBoolean("oikein"),
                        result.getString("teksti")
                ));
            }
        }

        return answers;
    }

    public Question insertQuestion(String text, String course, String subject) throws SQLException, ClassNotFoundException {
        try (Connection conn = getConnection()) {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO Kysymys (teksti, kurssi, aihe) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, text);
            statement.setString(2, course);
            statement.setString(3, subject);

            int rows = statement.executeUpdate();

            ResultSet result = statement.getGeneratedKeys();

            if (result.next()) {
                int id = result.getInt(1);

                return new Question(id, text, course, subject);
            }

        }

        return null;
    }

    public Answer insertAnswer(int questionId, boolean correct, String text) throws SQLException, ClassNotFoundException {
        try (Connection conn = getConnection()) {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO Vastaus (oikein, teksti, kysymysId) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            statement.setBoolean(1, correct);
            statement.setString(2, text);
            statement.setInt(3, questionId);

            int rows = statement.executeUpdate();

            ResultSet result = statement.getGeneratedKeys();

            if (result.next()) {
                int id = result.getInt(1);

                return new Answer(id, correct, text);
            }

        }

        return null;
    }

    public Integer deleteQuestion(int id) throws SQLException, ClassNotFoundException {
        try (Connection conn = getConnection()) {

            PreparedStatement statement = conn.prepareStatement("DELETE FROM Kysymys WHERE id = ?");

            statement.setInt(1, id);

            return statement.executeUpdate();
        }
    }

    public Integer deleteAnswer(int questionId, int id) throws SQLException, ClassNotFoundException {
        try (Connection conn = getConnection()) {

            PreparedStatement statement = conn.prepareStatement("DELETE FROM Vastaus WHERE id = ? AND kysymysid = ?");

            statement.setInt(1, id);
            statement.setInt(2, questionId);

            return statement.executeUpdate();


        }
    }

}
