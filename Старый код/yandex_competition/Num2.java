
import java.util.*;

class Email implements Comparable<Email> {
    private String login;
    private String domain;

    public Email(String arg) {
        int dog_ind = arg.indexOf("@");
        login = arg.substring(0, dog_ind);
        domain = arg.substring(dog_ind + 1);
        login = login.replaceAll("\\.", "");
        if (login.indexOf("-") > 0) {
            login = login.substring(0, login.indexOf("-"));
        }
        if (domain.lastIndexOf(".") > 0) {
            domain = domain.substring(0, domain.lastIndexOf(".")) + "@";
        }
        //System.out.println(login + " " + domain);
    }

    @Override
    public int compareTo(Email other) {
        if (other == null || !(other instanceof Email)) {return -1; }
        int res = this.login.compareTo(other.login);
        if (res == 0) {
            return this.domain.compareTo(other.domain);
        }
        return res;
    }
}

public class Num2 {
    static Set<Email> set = new TreeSet<>();

    static public void main(String[] args) {
        Scanner myInput = new Scanner(System.in);
        
        int n = myInput.nextInt();

        for (int i = 0; i < n; i++) {
            String line = myInput.next();
            set.add(new Email(line));
        }
        
        myInput.close();
        
        System.out.println(set.size());
    }
}
