package model;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity (name = "SignUp")
@Table (name = "signups")
public class SignUp implements model.Entity<SignUpId> {

    @EmbeddedId
    private SignUpId signUpId;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("eventId")
    private Event event;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("charName")
    private Character character;

    @Column (name = "signup_status")
    private byte signUpStatus;

    @Column (name = "confirm_status")
    private byte confirmStatus;

    public SignUp(){}

    public SignUp(Event event, Character character, byte signUpStatus){
        this.signUpId = new SignUpId(event.getEventId(), character.getCharName());
        this.event = event;
        this.character = character;
        this.signUpStatus = signUpStatus;
        this.confirmStatus = 0;
    }

    public SignUpId getSignUpId() {
        return signUpId;
    }

    public void setSignUpId(SignUpId signUpId) {
        this.signUpId = signUpId;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public byte getSignUpStatus() {
        return signUpStatus;
    }

    public void setSignUpStatus(byte signUpStatus) {
        this.signUpStatus = signUpStatus;
    }

    public byte getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(byte confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    @Override
    public SignUpId getID() {
        return signUpId;
    }
}
