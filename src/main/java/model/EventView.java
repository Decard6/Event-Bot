package model;

import jdk.nashorn.internal.ir.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Immutable
public class EventView {

    @Id
    @Column (name = "EventID", updatable = false, nullable = false)
    private Long eventId;

    @Column (name = "CharName", length = 12)
    private String charName;

    @Column (name = "ClassName", length = 20)
    private String className;

    @Column (name = "Spec", length = 32)
    private String spec;

    @Column (name = "SignUpStatus")
    private byte signUpStatus;

    @Column (name = "ConfirmStatus")
    private byte confirmStatus;

}
