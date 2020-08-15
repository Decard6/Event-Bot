
import model.Character;

import org.hibernate.SessionFactory;
import service.ServiceCharacter;
import service.ServiceCharacterImpl;

import java.math.BigInteger;

public class Main {

    public static void main(String[] args){
        Character decard = new Character(new BigInteger("183628462089699329"), "Decard", "Paladin", "Holy");
        SessionFactory sessionFactory = util.HibernateUtil.getSessionFactory();
        ServiceCharacter serviceCharacter = new ServiceCharacterImpl(sessionFactory, "HIBERNATE");

        serviceCharacter.deletaAll();
        serviceCharacter.delete(decard.getCharName());
        serviceCharacter.persist(decard);
        Character found = serviceCharacter.findById("Decard");
        System.out.println(found.toString());
        found.setClassName("Death Knight");
        serviceCharacter.update(found);
        found = serviceCharacter.findById("Decard");
        System.out.println(found.toString());
        serviceCharacter.delete(found.getCharName());
    }
}
