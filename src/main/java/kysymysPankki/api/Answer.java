package kysymysPankki.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Answer {
    private int id;
    private Boolean correct;
    private String text;

    @JsonProperty
    public int getId() {
        return id;
    }

    @JsonProperty
    public Boolean getCorrect() {
        return correct;
    }

    public Answer(int id, Boolean correct, String text) {
        this.id = id;
        this.correct = correct;
        this.text = text;
    }

    @JsonProperty
    public String getText() {
        return text;
    }
}
