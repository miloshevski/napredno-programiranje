package lab8;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

enum QuestionType {
    TRUE_FALSE,
    FREE_FORM
}

final class TriviaQuestion {
    private final String question;
    private final String answer;
    private final int value;
    private final QuestionType type;

    public TriviaQuestion(String question, String answer, int value, QuestionType type) {
        this.question = question;
        this.answer = answer;
        this.value = value;
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public int getValue() {
        return value;
    }

    public QuestionType getType() {
        return type;
    }

    public void printPrompt(int index) {
        System.out.println("Question " + (index + 1) + ".  " + value + " points.");
        System.out.println(question);
        if (type == QuestionType.TRUE_FALSE) {
            System.out.println("Enter 'T' for true or 'F' for false.");
        }
    }

    public boolean isCorrect(String userAnswer) {
        if (userAnswer == null) return false;

        userAnswer = userAnswer.trim();
        if (userAnswer.isEmpty()) return false;

        if (type == QuestionType.TRUE_FALSE) {
            // само првиот карактер, case-insensitive
            char ua = Character.toUpperCase(userAnswer.charAt(0));
            char ca = Character.toUpperCase(answer.trim().charAt(0));
            return ua == ca;
        }

        // FREE_FORM: case-insensitive цел стринг
        return userAnswer.equalsIgnoreCase(answer.trim());
    }
}

final class TriviaData {
    private final List<TriviaQuestion> data = new ArrayList<>();

    public void addQuestion(String q, String a, int v, QuestionType t) {
        data.add(new TriviaQuestion(q, a, v, t));
    }

    public int size() {
        return data.size();
    }

    public TriviaQuestion get(int index) {
        return data.get(index);
    }
}

public class TriviaGame {

    private final TriviaData questions = new TriviaData();

    public TriviaGame() {
        loadQuestions();
    }

    private void loadQuestions() {
        questions.addQuestion(
                "The possession of more than two sets of chromosomes is termed?",
                "polyploidy", 3, QuestionType.FREE_FORM
        );
        questions.addQuestion(
                "Erling Kagge skiied into the north pole alone on January 7, 1993.",
                "F", 1, QuestionType.TRUE_FALSE
        );
        questions.addQuestion(
                "1997 British band that produced 'Tub Thumper'",
                "Chumbawumba", 2, QuestionType.FREE_FORM
        );
        questions.addQuestion(
                "I am the geometric figure most like a lost parrot",
                "polygon", 2, QuestionType.FREE_FORM
        );
        questions.addQuestion(
                "Generics were introducted to Java starting at version 5.0.",
                "T", 1, QuestionType.TRUE_FALSE
        );
    }

    public void run(Scanner in) {
        int score = 0;

        for (int i = 0; i < questions.size(); i++) {
            TriviaQuestion q = questions.get(i);

            q.printPrompt(i);
            String userAnswer = in.nextLine();

            if (q.isCorrect(userAnswer)) {
                System.out.println("That is correct!  You get " + q.getValue() + " points.");
                score += q.getValue();
            } else {
                System.out.println("Wrong, the correct answer is " + q.getAnswer());
            }

            System.out.println("Your score is " + score);
        }

        System.out.println("Game over!  Thanks for playing!");
    }

    public static void main(String[] args) {
        TriviaGame game = new TriviaGame();
        try (Scanner keyboard = new Scanner(System.in)) {
            game.run(keyboard);
        }
    }
}
