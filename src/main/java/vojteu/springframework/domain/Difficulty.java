package vojteu.springframework.domain;

public enum Difficulty {

    EASY("Easy"), MODERATE("Moderate"), KIND_OF_HARD("Kind of Hard"), HARD("Hard");

    private String value;

    Difficulty(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
