import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            int choice = getChoice(scanner);
            exit = handleChoice(scanner, choice);
        }
        scanner.close();
    }

    private static int getChoice(Scanner scanner) {
        System.out.println("Pick an action:");
        System.out.println("1. Add");
        System.out.println("2. Update");
        System.out.println("3. Delete");
        System.out.println("4. Show all");
        System.out.println("5. Find by ID");
        System.out.println("6. Exit");
        System.out.print("Enter the action number: ");
        return scanner.nextInt();
    }

    private static boolean handleChoice(Scanner scanner, int choice) {
        scanner.nextLine(); // Consume newline
        switch (choice) {
            case 1:
                addPerson(scanner);
                return false;
            case 2:
                updatePerson(scanner);
                return false;
            case 3:
                deletePerson(scanner);
                return false;
            case 4:
                viewAllPersons();
                return false;
            case 5:
                findPersonById(scanner);
                return false;
            case 6:
                return true;
            default:
                System.out.println("Wrong choice. Try again.");
                return false;
        }
    }

    private static void addPerson(Scanner scanner) {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter lastname: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter age: ");
        int age = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine();

        Long personId = createPerson(name, lastName, age, phoneNumber);
        System.out.println("Person added by ID: " + personId);
    }

    private static Long createPerson(String name, String lastName, int age, String phoneNumber) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = null;
        Long personId = null;

        try {
            transaction = session.beginTransaction();

            Person person = new Person();
            person.setName(name);
            person.setLastName(lastName);
            person.setAge(age);
            person.setPhoneNumber(phoneNumber);

            personId = (Long) session.save(person);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return personId;
    }

    private static void updatePerson(Scanner scanner) {
        System.out.print("Enter person ID for update: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        System.out.print("Enter new name: ");
        String name = scanner.nextLine();

        System.out.print("Enter new lastname: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter new age: ");
        int age = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter new phone number: ");
        String phoneNumber = scanner.nextLine();

        updatePerson(id, name, lastName, age, phoneNumber);
        System.out.println("Person information updated.");
    }

    private static void updatePerson(Long id, String name, String lastName, int age, String phoneNumber) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            Person person = session.get(Person.class, id);
            if (person != null) {
                person.setName(name);
                person.setLastName(lastName);
                person.setAge(age);
                person.setPhoneNumber(phoneNumber);
                session.update(person);
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    private static void deletePerson(Scanner scanner) {
        System.out.print("Enter person ID for delete: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        deletePerson(id);
        System.out.println("Person deleted.");
    }

    private static void deletePerson(Long id) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            Person person = session.get(Person.class, id);
            if (person != null) {
                session.delete(person);
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    private static void viewAllPersons() {
        Session session = HibernateUtil.openSession();

        try {
            List<Person> persons = session.createQuery("from Person", Person.class).list();
            for (Person person : persons) {
                System.out.println("ID: " + person.getId() + ", Name: " + person.getName() +
                        ", Lastname: " + person.getLastName() + ", Age: " + person.getAge() +
                        ", Phone Number: " + person.getPhoneNumber());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    private static void findPersonById(Scanner scanner) {
        System.out.print("Find person by ID: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        findPersonById(id);
    }

    private static void findPersonById(Long id) {
        Session session = HibernateUtil.openSession();

        try {
            Person person = session.get(Person.class, id);
            if (person != null) {
                System.out.println("ID: " + person.getId() + ", Name: " + person.getName() +
                        ", Lastname: " + person.getLastName() + ", Age: " + person.getAge() +
                        ", Phone number: " + person.getPhoneNumber());
            } else {
                System.out.println("Person with ID " + id + " not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
