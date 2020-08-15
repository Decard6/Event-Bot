package model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SignUpId implements Serializable {

    @Column (name = "EventID")
    private Long eventID;

    @Column (name = "CharName")
    private String charName;

    public Long getEventID() {
        return eventID;
    }

    public void setEventID(Long eventID) {
        this.eventID = eventID;
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

        return eventID.equals(that.getEventID()) && charName.equals(that.getCharName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventID, charName);
    }
}
