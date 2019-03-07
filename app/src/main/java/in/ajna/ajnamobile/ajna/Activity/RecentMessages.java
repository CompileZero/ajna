package in.ajna.ajnamobile.ajna.Activity;

public class RecentMessages {

    private Long time;
    private String member;
    private String message;

    public RecentMessages(){
        //Required empty constructor
        }

    public RecentMessages(Long time, String member,String message) {
        this.time = time;
        this.member=member;
        this.message = message;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public Long getTime() { return time; }

    public void setTime(Long time) { this.time = time; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }
}


