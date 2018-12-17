package in.ajna.ajnamobile.ajna.RecentMessages;

public class RecentMessages {

    private Long time;
    private String message;

    public RecentMessages(){
        //Required empty constructor
        }

    public RecentMessages(Long time, String message) {
        this.time = time;
        this.message = message;
    }

    public Long getTime() { return time; }

    public void setTime(Long time) { this.time = time; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }
}


