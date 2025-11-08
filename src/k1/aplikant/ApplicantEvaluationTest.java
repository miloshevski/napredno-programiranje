package k1.aplikant;
import java.util.Scanner;

/**
 * I partial exam 2016
 */
public class ApplicantEvaluationTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        int creditScore = scanner.nextInt();
        int employmentYears = scanner.nextInt();
        boolean hasCriminalRecord = scanner.nextBoolean();
        int choice = scanner.nextInt();
        Applicant applicant = new Applicant(name, creditScore, employmentYears, hasCriminalRecord);
        Evaluator.TYPE type = Evaluator.TYPE.values()[choice];
        Evaluator evaluator = null;
        try {
            evaluator = EvaluatorBuilder.build(type);
            System.out.println("Applicant");
            System.out.println(applicant);
            System.out.println("Evaluation type: " + type.name());
            if (evaluator.evaluate(applicant)) {
                System.out.println("Applicant is ACCEPTED");
            } else {
                System.out.println("Applicant is REJECTED");
            }
        } catch (InvalidEvaluation invalidEvaluation) {
            System.out.println("Invalid evaluation");
        }
    }
}

class Applicant {
    private String name;

    private int creditScore;
    private int employmentYears;
    private boolean hasCriminalRecord;

    public Applicant(String name, int creditScore, int employmentYears, boolean hasCriminalRecord) {
        this.name = name;
        this.creditScore = creditScore;
        this.employmentYears = employmentYears;
        this.hasCriminalRecord = hasCriminalRecord;
    }

    public String getName() {
        return name;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public int getEmploymentYears() {
        return employmentYears;
    }

    public boolean hasCriminalRecord() {
        return hasCriminalRecord;
    }

    @Override
    public String toString() {
        return String.format("Name: %s\nCredit score: %d\nExperience: %d\nCriminal record: %s\n",
                name, creditScore, employmentYears, hasCriminalRecord ? "Yes" : "No");
    }
}

interface Evaluator {
    enum TYPE {
        NO_CRIMINAL_RECORD,
        MORE_EXPERIENCE,
        MORE_CREDIT_SCORE,
        NO_CRIMINAL_RECORD_AND_MORE_EXPERIENCE,
        MORE_EXPERIENCE_AND_MORE_CREDIT_SCORE,
        NO_CRIMINAL_RECORD_AND_MORE_CREDIT_SCORE,
        INVALID // should throw exception
    }

    boolean evaluate(Applicant applicant);
}

class InvalidEvaluation extends Exception{
    public InvalidEvaluation(String message) {
        super(message);
    }
}

class BiEvaluator implements Evaluator{
    private final Evaluator a;
    private final Evaluator b;

    public BiEvaluator(Evaluator a, Evaluator b){
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean evaluate(Applicant applicant) {
        return a.evaluate(applicant) && b.evaluate(applicant);
    }
}

class EvaluatorBuilder{
    public static Evaluator build(Evaluator.TYPE type) throws InvalidEvaluation {
        switch (type){
            case NO_CRIMINAL_RECORD:
                return hasNoCriminalRecord;
            case MORE_EXPERIENCE:
                return hasMoreExperience;
            case MORE_CREDIT_SCORE:
                return hasMoreCreditScore;
            case NO_CRIMINAL_RECORD_AND_MORE_CREDIT_SCORE:
                return new BiEvaluator(hasNoCriminalRecord,hasMoreCreditScore);
            case MORE_EXPERIENCE_AND_MORE_CREDIT_SCORE:
                return new BiEvaluator(hasMoreExperience,hasMoreCreditScore);
            case NO_CRIMINAL_RECORD_AND_MORE_EXPERIENCE:
                return new BiEvaluator(hasNoCriminalRecord,hasMoreExperience);
            default:
                throw new InvalidEvaluation("Invalid");
        }
    }

    private static final Evaluator hasNoCriminalRecord = x -> !x.hasCriminalRecord();
    private static final Evaluator hasMoreExperience = x -> x.getEmploymentYears() >= 10;
    private static final Evaluator hasMoreCreditScore = x -> x.getCreditScore() >= 500;
 }


// имплементација на евалуатори овде


