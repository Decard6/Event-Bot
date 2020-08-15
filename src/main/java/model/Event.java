package model;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table (name = "events")
public class Event {

    @Id
    @Column (name = "EventID", nullable = false, unique = true)
    @GeneratedValue (strategy = GenerationType.AUTO)
    private long eventId;

    @Column (name = "EventName", nullable = false, length = 64)
    private String eventName;

    @Column (name = "EventDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventDate;

    @Column (name = "ChannelID")
    private BigInteger channelID;

    @Column (name = "MessageID")
    private BigInteger messageID;
}
