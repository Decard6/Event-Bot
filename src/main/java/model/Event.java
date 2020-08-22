package model;

import util.SignUpComparator;

import javax.persistence.*;
import javax.persistence.Entity;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity (name = "Event")
@Table (name = "events")
public class Event implements model.Entity<Long>, Fetchable<SignUp>{

    @Id
    @Column (name = "id", nullable = false, unique = true)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long eventId;

    @Column (name = "name", nullable = false, length = 64)
    private String eventName;

    @Column (name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventDate;

    @Column (name = "channel_id")
    private long channelID;

    @Column (name = "message_id")
    private long messageID;

    @OneToMany(
            mappedBy = "event",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<SignUp> signUps = new ArrayList<>();

    public Event(){}

    public Event(String eventName, Date eventDate, long channelID){
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.channelID = channelID;
    }

    public Event(String eventName, Date eventDate, long channelID, long messageID){
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.channelID = channelID;
        this.messageID = messageID;
    }

    public Event(long eventId, String eventName, Date eventDate, long channelID, long messageID){
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.channelID = channelID;
        this.messageID = messageID;
    }

    public List<SignUp> getSignUps() {
        return signUps;
    }

    public void setSignUps(List<SignUp> signUps) {
        this.signUps = signUps;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public long getChannelID() {
        return channelID;
    }

    public void setChannelID(long channelID) {
        this.channelID = channelID;
    }

    public long getMessageID() {
        return messageID;
    }

    public void setMessageID(long messageID) {
        this.messageID = messageID;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;

        if(!(obj instanceof Event))
            return false;

        Event that = (Event)obj;
        if(this.eventId == that.eventId)
            return true;

        return false;
    }

    @Override
    public String toString() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE HH:mm\ndd/MM/yyy", Locale.ENGLISH);
        String header = String.format("%s (ID: %d)\n%s\n\n", eventName, eventId, simpleDateFormat.format(eventDate));
        StringBuilder stringBuilder = new StringBuilder();
        signUps.sort(new SignUpComparator());

        List<SignUp> confirmed = new ArrayList<>();
        List<SignUp> standby = new ArrayList<>();
        List<SignUp> signedUp = new ArrayList<>();
        List<SignUp> tentative = new ArrayList<>();
        List<SignUp> out = new ArrayList<>();

        for(SignUp signUp : signUps){
            if(signUp.getConfirmStatus() == 1)
                confirmed.add(signUp);
            else if(signUp.getConfirmStatus() == 2)
                standby.add(signUp);
            else if(signUp.getConfirmStatus() == 0 && signUp.getSignUpStatus() == 1)
                signedUp.add(signUp);
            else if(signUp.getConfirmStatus() == 0 && signUp.getSignUpStatus() == 2)
                tentative.add(signUp);
            else if(signUp.getConfirmStatus() == 3 || signUp.getSignUpStatus() == 3)
                out.add(signUp);
        }

        List<String> statusStrings = new ArrayList<>();
        statusStrings.add(statusListString("Confirmed:", confirmed));
        statusStrings.add(statusListString("Standby:", standby));
        statusStrings.add(statusListString("Signed Up:", signedUp));
        statusStrings.add(statusListString("Tentative:", tentative));
        statusStrings.add(statusListString("Out:", out));

        for(String string : statusStrings){
            if(string != null)
                stringBuilder.append(string);
        }

        while(stringBuilder.charAt(stringBuilder.length() - 1) == '\n')
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        return String.format("```%s%s```", header, stringBuilder.toString());
    }

    private String statusListString(String string, List<SignUp> list){
        if(list.isEmpty())
            return null;

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string).append("\n");
        for(int i = 0; i<list.size(); i++){
            String characterString = list.get(i).getCharacter().toString();
            String toAppend = String.format("%d. %s\n", i+1, characterString);
            stringBuilder.append(toAppend);
        }
        stringBuilder.append("\n");

        return stringBuilder.toString();
    }

    @Override
    public Long getID() {
        return getEventId();
    }

    @Override
    public List<SignUp> fetch() {
        return getSignUps();
    }
}
