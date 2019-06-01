package chr.chat.components;

import java.util.Random;

public class BotAttachments {

    public static final int ATTACHMENTS_TOPIC = 30001;
    public static final int ATTACHMENTS_QUESTION = 30002;
    public static final int ATTACHMENTS_QUESTION_COUPLE = 30003;

    private Random random;

    public BotAttachments() {
        random = new Random();
    }

    public String getRandom(int attachmentType) {
        switch (attachmentType) {
            case ATTACHMENTS_TOPIC:             return "Topic: "    + getRandomTopic();
            case ATTACHMENTS_QUESTION:          return "Question: " + getRandomQuestion();
            case ATTACHMENTS_QUESTION_COUPLE:   return "Question: " + getRandomQuestionCouple();
        }

        return "Sorry, not found anything..";
    }


    private String getRandomTopic() {

        String[] TOPICS = new String[]{
                "Books",
                "Movies",
                "Travel",
                "Music",
                "Hobbies",
                "Pets and Animals",
                "Future plans",
                "Education",
                "Dreams",
                "Sports",
                "Food",
                "Games"
        };

        return TOPICS[random.nextInt(TOPICS.length)];
    }

    private String getRandomQuestion() {

        String[] QUESTIONS = new String[]{
                "Do you believe in ghosts?",
                "Which house would you belong to in Hogwarts?",
                "How would you spend a million dollars?",
                "What’s your favorite movie?",
                "What would a perfect day look like for you?",
                "Cats or dogs?",
                "What is the best dream you ever had?",
                "What do you think about Marvel films?",
                "What do you think about Game of Thrones?",
                "Do you like read?",
                "What is your favorite type of music?",
                "Which country would you like to visit?",
                "Which country would you like to live in?",
                "Where are you right now?",
                "How old are you?",
                "Your dream vacation?",
                "What was the scariest moment in your life?",
                "What was the funniest moment in your life?",
                "What was the embarrassing moment in your life?",
                "What animal are you most afraid of?"
        };

        return QUESTIONS[random.nextInt(QUESTIONS.length)];
    }

    private String getRandomQuestionCouple() {

        String[] QUESTIONS_FOR_COUPLE = new String[]{
                "Are you single?",
                "What’s an ideal weekend for you?",
                "What is your idea of a perfect romantic date?",
                "What did your last relationship teach you?",
                "What’s your definition of cheating?",
                "Have you ever lost someone or something you truly loved?",
                "What’s your ideal type of vacation?"
        };

        return QUESTIONS_FOR_COUPLE[random.nextInt(QUESTIONS_FOR_COUPLE.length)];
    }
}
