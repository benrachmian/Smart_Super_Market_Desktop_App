package SDMSystemDTO;

import SDMSystemDTO.customer.DTOCustomer;

public class DTOFeedback {

    private static final int MIN_RANK = 1;
    private static final int MAX_RANK = 5;
    private int rank;
    private String verbalFeedback;
    private final DTOCustomer feedbackGiver;

    public DTOFeedback(int rank, String verbalFeedback, DTOCustomer feedbackGiver) {
        this.rank = rank;
        this.verbalFeedback = verbalFeedback;
        this.feedbackGiver = feedbackGiver;
    }

    public int getRank() {
        return rank;
    }

    public String getVerbalFeedback() {
        return verbalFeedback;
    }

    public DTOCustomer getFeedbackGiver() {
        return feedbackGiver;
    }
}

