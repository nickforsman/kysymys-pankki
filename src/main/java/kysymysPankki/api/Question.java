package kysymysPankki.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Question {
    private int id;
    private String text;
    private String course;
    private String subject;

    public Question(int id, String text, String course, String subject) {
        this.id = id;
        this.text = text;
        this.course = course;
        this.subject = subject;
    }

    @JsonProperty
    public String getText() {
        return text;
    }

    @JsonProperty
    public String getCourse() {
        return course;
    }

    @JsonProperty
    public String getSubject() {
        return subject;
    }

    @JsonProperty
    public int getId() {
        return id;
    }
}
