package SDMSystem;

import SDMSystem.customer.Customer;

public class Feedback {
    private static final int MIN_RANK = 1;
    private static final int MAX_RANK = 5;
    private int rank;
    private String verbalFeedback;
    private final Customer feedbackGiver;

    public Feedback(int rank, String verbalFeedback, Customer feedbackGiver) {
        this.rank = rank;
        this.verbalFeedback = verbalFeedback;
        this.feedbackGiver = feedbackGiver;
    }
}
