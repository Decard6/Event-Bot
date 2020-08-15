package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;

@Entity
@Table (name = "characters")
public class Character {

    @Column (name = "DiscordID", nullable = false, unique = true)
    private BigInteger discordID;

    @Id
    @Column (name = "CharName", nullable = false, unique = true, length = 12)
    private String charName;

    @Column (name = "ClassName", nullable = false, length = 20)
    private String className;

    @Column (name = "Spec", length = 32)
    private String spec;

    public Character(){}

    public Character(BigInteger discordID, String charName, String className, String spec){
        this.discordID = discordID;
        this.charName = charName;
        this.className = className;
        this.spec = spec;
    }

    public BigInteger getDiscordID() {
        return discordID;
    }

    public void setDiscordID(BigInteger discordID) {
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
}
