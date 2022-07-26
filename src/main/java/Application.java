import entity.Nes;

import javax.persistence.*;
import java.util.List;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        // Комплектующие[id: 1]
        // - Процессоры[id: 2]
        // - - Intel[id: 3]
        // - - AMD[id: 4]
        // - ОЗУ[id: 5]
        // Аудиотехника[id: 6]
        // - Наушники[id: 7]
        // - - С микрофоном[id: 8]
        // - - Без микрофона[id: 9]
        // - Колонки[id: 10]
        // --- Добавление новой категории ---
        // Куда добавить: 2
        // Название: МЦСТ

        // TypedQuery<Category> query = manager.createQuery("...", Category.class);
        // query.getResultList();

        // Query query = manager.createQuery("...");
        // query.executeUpdate();
        Scanner scanner = new Scanner(System.in);
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("default");
        EntityManager manager = factory.createEntityManager();
        TypedQuery<Nes> nesTypedQuery = manager.createQuery(
                "select n from Nes n", Nes.class
        );

        List<Nes> nesList = nesTypedQuery.getResultList();
        for (Nes nes : nesList) {
            String r = "";
            for (int i = 0; i < nes.getLevel(); i++) {
                r += "- ";
            }
            System.out.println(r + nes.getName() + "[id: " + nes.getId() + "]");
        }
        System.out.println("Куда добавить категорию? Введите id.");
        String id = scanner.nextLine();
        int IdInt = Integer.parseInt(id);
        Long IdLong = Long.parseLong(id);
        Nes nes1 = manager.find(Nes.class, IdLong);

        EntityTransaction transaction = manager.getTransaction();
        try {
            transaction.begin();
            System.out.println("Введите название:");
            String categoryName = scanner.nextLine();

            Query query = manager.createQuery(
                    "update  Nes n set n.left = n.left + 2 where n.left > ?1");
            query.setParameter(1, nes1.getRight());
            query.executeUpdate();

            Query query1 = manager.createQuery(
                    "update  Nes n set n.right = n.right +2 where n.right > ?1");
            query1.setParameter(1, nes1.getRight());
            query1.executeUpdate();

            Nes nes = new Nes();
            nes.setName(categoryName);
            nes.setLeft(nes1.getRight());
            nes.setRight(nes1.getRight() + 1);
            nes.setLevel(nes1.getLevel() + 1);
            nes1.setRight(nes.getRight() +1);
            manager.persist(nes);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

    }
}
