package org.example.hibernate1;

import java.util.List;
import java.util.logging.Level;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import org.example.hibernate1.entities.Car;
import org.example.hibernate1.entities.CarType;
import org.example.hibernate1.entities.Owner;
import org.example.hibernate1.entities.Word;

/*
використовуючи hibernate:
- створити табличку Word (id, value)
- наповнити її
- дістати всі value слів та запакувати в List .


Створити клас Car з полями:
id
model,
Type (ENUM)
power,
price,
year.

Створити клас Owner
id,
name
Car

Зв'язок між авто та водієм 1:1 Unidirectional (Власник знає про авто, авто про власника не знає нічого)
*/
public class Main {
    public static StandardServiceRegistry serviceRegistry;
    public static SessionFactory sessionFactory;
    public static Metadata metadata;
    public static Session session;

    public static void startConnection() {
        serviceRegistry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build();

        metadata = new MetadataSources(serviceRegistry)
                .addAnnotatedClass(Word.class)
                .addAnnotatedClass(Car.class)
                .addAnnotatedClass(Owner.class)
                .getMetadataBuilder()
                .build();

        sessionFactory = metadata.getSessionFactoryBuilder().build();
        session = sessionFactory.openSession();
    }

    public static void stopConnection() {
        session.close();
        sessionFactory.close();
        serviceRegistry.close();
    }

    public static void task1() {
        session.beginTransaction();

        Word word1 = new Word("Word 1");
        Word word2 = new Word("Word 2");
        Word word3 = new Word("Word 3");

        session.persist(word1);
        session.persist(word2);
        session.persist(word3);

        session.getTransaction().commit();

        List<Word> words = session.createQuery("select w from Word w", Word.class).getResultList();
        System.out.println(words);

    }

    public static void task2() {
        session.beginTransaction();

        Car car1 = new Car("BMV X5", CarType.SUV, 320, 20000.0, 2015);
        Car car2 = new Car("VOLVO", CarType.Sedan, 220, 25000.0, 2011);
        Car car3 = new Car("MAZDA", CarType.Coupe, 180, 17000.0, 2017);

        session.persist(new Owner("Vasya", car1));
        session.persist(new Owner("Petya", car2));
        session.persist(new Owner("Olya", car3));

        session.getTransaction().commit();

        List<Owner> resultList = session.createQuery("select owner from Owner owner", Owner.class).getResultList();
        System.out.println(resultList);

    }

    public static void main(String[] args) {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.WARNING);
        startConnection();

        task1();
        task2();

        stopConnection();
    }

}