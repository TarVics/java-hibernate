package org.example.hibernate2;

/*
    Car
        id
        model,
        Type (ENUM)
        power,
        price,
        year

    Owner
        id,
        name
        List<Car>
        DriveLicense

    DriveLicense
        id
        series

    За допомоги хібернейту реалізувати наступну структуру
    зв'язо unidirectional!!!
*/
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.example.hibernate2.entities.CarType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import org.example.hibernate2.entities.Car;
import org.example.hibernate2.entities.DriveLicense;
import org.example.hibernate2.entities.Owner;

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
                .addAnnotatedClass(Car.class)
                .addAnnotatedClass(Owner.class)
                .addAnnotatedClass(DriveLicense.class)
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

    public static void main(String[] args) {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.WARNING);
        startConnection();

        task();

        stopConnection();
    }

    public static void task() {
        session.beginTransaction();

        Car car1 = new Car("BMV X5", CarType.SUV, 320, 20000.0, 2015);
        Car car2 = new Car("VOLVO", CarType.Sedan, 220, 25000.0, 2011);
        Car car3 = new Car("MAZDA", CarType.Coupe, 180, 17000.0, 2017);
        Car car4 = new Car("ŠKODA", CarType.Coupe, 150, 19000.0, 2018);
        Car car5 = new Car("TOYOTA", CarType.Hatchback, 280, 47000.0, 2021);
        Car car6 = new Car("PEGEOT", CarType.Hatchback, 170, 21000.0, 2022);

        session.persist(new Owner(
                "Vasya",
                Arrays.asList(car1, car2),
                new DriveLicense("111-AAA")
        ));

        session.persist(new Owner(
                "Petya",
                Arrays.asList(car3, car4),
                new DriveLicense("222-BBB")
        ));

        session.persist(new Owner(
                "Olya",
                Arrays.asList(car5, car6),
                new DriveLicense("333-CCC")
        ));

        session.getTransaction().commit();

        List<Owner> resultList = session.createQuery("select owner from Owner owner", Owner.class).getResultList();
        System.out.println(resultList);

    }


}
