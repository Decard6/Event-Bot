package util;

import model.SignUp;

import java.util.Comparator;

public class SignUpComparator implements Comparator<SignUp> {

    @Override
    public int compare(SignUp o1, SignUp o2) {
        String class1 = o1.getCharacter().getClassName();
        String class2 = o2.getCharacter().getClassName();
        int comparison = class1.compareTo(class2);
        if(comparison != 0)
            return comparison;

        String name1 = o1.getCharacter().getCharName();
        String name2 = o2.getCharacter().getCharName();
        comparison = name1.compareTo(name2);
        return comparison;
    }
}
