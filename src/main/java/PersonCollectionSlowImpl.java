import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PersonCollectionSlowImpl implements PersonCollection {
    List<Person> people;

    public PersonCollectionSlowImpl() {
        this.people = new ArrayList<>();
    }

    @Override
    public boolean add(String email, String name, int age, String town) {
        boolean emailAlreadyExist = people.stream().anyMatch(person -> person.getEmail().equals(email));
        if (emailAlreadyExist) {
            return false;
        }
        Person person = new Person(email, name, age, town);
        people.add(person);
        return true;
    }

    @Override
    public int getCount() {
        return people.size();
    }

    @Override
    public boolean delete(String email) {
        return people.removeIf(person -> person.getEmail().equals(email));
    }

    @Override
    public Person find(String email) {
        return people.stream().filter(person -> person.getEmail().equals(email)).findFirst().orElse(null);
    }

    @Override
    public Iterable<Person> findAll(String emailDomain) {
        return people.stream()
                .filter(person -> person.getEmail().endsWith("@" + emailDomain))
                .sorted(Comparator.comparing(Person::getEmail))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Person> findAll(String name, String town) {
        return people.stream()
                .filter(person -> person.getName().equals(name) && person.getTown().equals(town))
                .sorted(Comparator.comparing(Person::getEmail))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Person> findAll(int startAge, int endAge) {
        return people.stream()
                .filter(person -> person.getAge() >= startAge && person.getAge() <= endAge)
                .sorted(Comparator.comparing(Person::getAge).thenComparing(Person::getEmail))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Person> findAll(int startAge, int endAge, String town) {
        return people.stream()
                .filter(person -> person.getAge() >= startAge && person.getAge() <= endAge && person.getTown().equals(town))
                .sorted(Comparator.comparing(Person::getAge).thenComparing(Person::getEmail))
                .collect(Collectors.toList());
    }
}
