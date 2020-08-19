package model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SignUpId implements Serializable {

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "char_name")
    private String charName;

    public SignUpId(){}

    public SignUpId(long eventId, String charName){
        this.eventId = eventId;
        this.charName = charName;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventID) {
        this.eventId = eventID;
    }

    public String getCharName() {
        return charName;
    }

    public void setCharName(String charName) {
        this.charName = charName;
    }


    @Override
    public boolean equals(Object obj) {

        if(this == obj)
            return true;

        if(!(obj instanceof SignUpId))
            return false;

        SignUpId that = (SignUpId)obj;

        return eventId.equals(that.getEventId()) && charName.equals(that.getCharName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, charName);
    }
}
