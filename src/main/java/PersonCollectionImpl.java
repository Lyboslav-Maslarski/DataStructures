import java.util.*;
import java.util.stream.Collectors;

public class PersonCollectionImpl implements PersonCollection {
    private final Map<String, Person> personByEmail;
    private final Map<String, TreeSet<Person>> personByEmailDomain;
    private final Map<String, TreeSet<Person>> personByNameAndTown;
    private final TreeMap<Integer, TreeSet<Person>> personByAge;
    private final Map<String, TreeMap<Integer, TreeSet<Person>>> personByTownAndAge;

    public PersonCollectionImpl() {
        personByEmail = new HashMap<>();
        personByEmailDomain = new HashMap<>();
        personByNameAndTown = new HashMap<>();
        personByAge = new TreeMap<>();
        personByTownAndAge = new HashMap<>();
    }

    @Override

    public boolean add(String email, String name, int age, String town) {
        if (personByEmail.containsKey(email)) {
            return false;
        }
        Person person = new Person(email, name, age, town);
        personByEmail.put(email, person);
        String domain = email.substring(email.indexOf("@") + 1);
        personByEmailDomain.putIfAbsent(domain, new TreeSet<>());
        personByEmailDomain.get(domain).add(person);
        personByNameAndTown.putIfAbsent(name + "_" + town, new TreeSet<>());
        personByNameAndTown.get(name + "_" + town).add(person);
        personByAge.putIfAbsent(age, new TreeSet<>());
        personByAge.get(age).add(person);
        personByTownAndAge.putIfAbsent(town, new TreeMap<>());
        personByTownAndAge.get(town).putIfAbsent(age, new TreeSet<>());
        personByTownAndAge.get(town).get(age).add(person);
        return true;
    }

    @Override
    public int getCount() {
        return personByEmail.size();
    }

    @Override
    public boolean delete(String email) {
        Person personToRemove = personByEmail.remove(email);
        if (personToRemove == null) {
            return false;
        }
        String domain = email.substring(email.indexOf("@") + 1);
        personByEmailDomain.get(domain).remove(personToRemove);
        String nameAndTown = personToRemove.getName() + "_" + personToRemove.getTown();
        personByNameAndTown.get(nameAndTown).remove(personToRemove);
        personByAge.get(personToRemove.getAge()).remove(personToRemove);
        personByTownAndAge.get(personToRemove.getTown()).get(personToRemove.getAge()).remove(personToRemove);
        return true;
    }

    @Override
    public Person find(String email) {
        return personByEmail.get(email);
    }

    @Override
    public Iterable<Person> findAll(String emailDomain) {
        TreeSet<Person> people = personByEmailDomain.get(emailDomain);
        if (people == null) {
            return new TreeSet<>();
        }
        return people;
    }

    @Override
    public Iterable<Person> findAll(String name, String town) {
        TreeSet<Person> people = personByNameAndTown.get(name + "_" + town);
        if (people == null) {
            return new TreeSet<>();
        }
        return people;
    }

    @Override
    public Iterable<Person> findAll(int startAge, int endAge) {
        TreeSet<Person> people = new TreeSet<>();
        personByAge.subMap(startAge, true, endAge, true)
                .values()
                .forEach(people::addAll);
        return people;
    }

    @Override
    public Iterable<Person> findAll(int startAge, int endAge, String town) {
        TreeSet<Person> people = new TreeSet<>();
        if (personByTownAndAge.containsKey(town)) {
            personByTownAndAge.get(town)
                    .subMap(startAge, true, endAge, true)
                    .values()
                    .forEach(people::addAll);
        }
        return people;
    }
}
