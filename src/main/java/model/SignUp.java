package model;

import javax.persistence.*;

@Entity
@Table (name = "signups")
public class SignUp {

    @EmbeddedId
    private SignUpId signUpId;

    @Column (name = "SignUpStatus")
    private byte signUpStatus;

    @Column (name = "ConfirmStatus")
    private byte confirmStatus;

}
