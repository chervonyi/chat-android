package chr.chat.components;

public class BotAttachments {

    public static final int ATTACHMENTS_TOPIC = 30001;
    public static final int ATTACHMENTS_QUESTION = 30002;
    public static final int ATTACHMENTS_QUESTION_COUPLE = 30003;

    public BotAttachments() {}

    public String getRandom(int attachmentType) {
        switch (attachmentType) {
            case ATTACHMENTS_TOPIC:             return "Topic: "    + getRandomTopic();
            case ATTACHMENTS_QUESTION:          return "Question: " + getRandomQuestion();
            case ATTACHMENTS_QUESTION_COUPLE:   return "Question: " + getRandomQuestionCouple();
        }

        return "Sorry, not found anything..";
    }


    private String getRandomTopic() {
        return "getRandomTopic";
    }

    private String getRandomQuestion() {
        return "getRandomQuestion";
    }

    private String getRandomQuestionCouple() {
        return "getRandomQuestionCouple";
    }


}
