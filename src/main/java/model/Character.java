package model;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalIdCache;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.*;

@Entity (name = "character")
@Table (name = "characters")
@NaturalIdCache
@Cache(
        usage = CacheConcurrencyStrategy.READ_WRITE
)
public class Character implements model.Entity<String>, Fetchable<SignUp> {

    @Column (name = "user_id", nullable = false, unique = true)
    private long discordID;

    @Id
    @Column (name = "name", nullable = false, unique = true, length = 12)
    private String charName;

    @Column (name = "class_name", nullable = false, length = 20)
    private String className;

    @Column (name = "spec", length = 32)
    private String spec;

    @OneToMany(
            mappedBy = "character",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<SignUp> signUps = new ArrayList<>();

    public Character(){}

    public Character(long discordID, String charName, String className, String spec){
        this.discordID = discordID;
        this.charName = charName;
        this.className = className;
        this.spec = spec;
    }

    public List<SignUp> getSignUps() {
        return signUps;
    }

    public void setSignUps(List<SignUp> signUps) {
        this.signUps = signUps;
    }

    public long getDiscordID() {
        return discordID;
    }

    public void setDiscordID(long discordID) {
        this.discordID = discordID;
    }

    public String getCharName() {
        return charName;
    }

    public void setCharName(String charName) {
        this.charName = charName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    @Override
    public boolean equals(Object object) {
        if(object instanceof Character)
            return this.charName.equals(((Character) object).charName);
        return false;
    }

    @Override
    public String toString() {
        String output = String.format("%-13s %-13s %s", charName + ",", className + ",", spec);
        return output;
    }

    @Override
    public String getID() {
        return getCharName();
    }

    @Override
    public List<SignUp> fetch() {
        return getSignUps();
    }
}
