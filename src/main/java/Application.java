import entity.Nes;

import javax.persistence.*;
import java.util.List;
import java.util.Scanner;

public class Application {

    private static final EntityManagerFactory FACTORY = Persistence.createEntityManagerFactory("default");

    private static final Scanner IN = new Scanner(System.in);

    public static void main(String[] args){
        System.out.println("-Добавление категории [1]");
        System.out.println("-Удаление категории [2]");
        System.out.println("-Перемещение категории [3]");
        System.out.println("Выберите действие:");
        String actionNum = IN.nextLine();
        switch (actionNum){
            case "1" -> createCategory();
            case "2" -> delete();
            case "3" -> move();
            default -> System.out.println("Такого действия не существует!");
        }
    }

    private static void createCategory(){
        EntityManager manager = FACTORY.createEntityManager();
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
        String id = IN.nextLine();
        Long idLong = Long.parseLong(id);
        Nes nes1 = manager.find(Nes.class,idLong);

        EntityTransaction transaction = manager.getTransaction();
        try {
            transaction.begin();
            System.out.println("Введите название:");
            String categoryName = IN.nextLine();

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
        } catch (Exception e){
            manager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            manager.close();
        }
    }

    private static void delete(){
        EntityManager manager = FACTORY.createEntityManager();
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

        try {
            manager.getTransaction().begin();
            System.out.println("Какую категорию следует удалить? Введите id:");
            String id = IN.nextLine();
            int idInt = Integer.parseInt(id);
            long idLong = Long.parseLong(id);
            Nes nes1 = manager.find(Nes.class, idLong);

            Query query = manager.createQuery(
                    "delete from Nes n where  n.left >= ?1 and n.right <= ?2");
            query.setParameter(1, nes1.getLeft());
            query.setParameter(2, nes1.getRight());
            query.executeUpdate();

            Query query1 = manager.createQuery(
                    "update Nes n set n.left = n.left - ?1 where n.left > ?2");
            query1.setParameter(1, nes1.getRight() - nes1.getLeft() + 1);
            query1.setParameter(2, nes1.getRight());
            query1.executeUpdate();

            Query query2 = manager.createQuery(
                    "update Nes n set  n.right = n.right - ?1 where n.right > ?2");
            query2.setParameter(1, nes1.getRight()- nes1.getLeft() + 1);
            query2.setParameter(2, nes1.getRight());
            query2.executeUpdate();

            manager.getTransaction().commit();
        } catch (Exception e){
            manager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            manager.close();
        }

    }

    private static void move(){
        EntityManager manager = FACTORY.createEntityManager();
        TypedQuery<Nes> nesTypedQuery = manager.createQuery(
                "select n from Nes n order by n.left", Nes.class
        );
        List<Nes> nesList = nesTypedQuery.getResultList();
        for (Nes nes : nesList) {
            String r = "";
            for (int i = 0; i < nes.getLevel(); i++) {
                r += "- ";
            }
            System.out.println(r + nes.getName() + "[id: " + nes.getId() + "]");
        }
        manager.getTransaction().begin();
        System.out.println("Введите id категории, которую хотите переместить:");
        String id = IN.nextLine();
        int idInt = Integer.parseInt(id);
        long idLong = Long.parseLong(id);
        Nes nes1 = manager.find(Nes.class, idLong);

        try {

             System.out.println("Введи id категории, куда хотите переместить:");
             String id2 = IN.nextLine();
             int id2Int = Integer.parseInt(id2);
             long id2Long = Long.parseLong(id2);
             Nes nes = manager.find(Nes.class, id2Long);

             //----------------------для перемещаемой кат-и сделать минусовыми ключи
             Query query = manager.createQuery(
                     "update Nes n set n.left = n.left * -1, n.right = n.right * -1 where n.left >= ?2 and n.right <= ?1"
             );
             query.setParameter(1, nes.getRight());
             query.setParameter(2, nes.getLeft());
             query.executeUpdate();

             //------------------------убрать образовавшийся промежуток
             Query query5 = manager.createQuery(
                     "update Nes n set n.left = n.left - ?1 where n.left > ?2");
             query5.setParameter(1, nes.getRight() - nes.getLeft() + 1);
             query5.setParameter(2, nes.getRight());
             query5.executeUpdate();

             Query query4 = manager.createQuery(
                     "update Nes n set  n.right = n.right - ?1 where n.right > ?2");
             query4.setParameter(1, nes.getRight()- nes.getLeft() + 1);
             query4.setParameter(2,nes.getRight());
             query4.executeUpdate();
             //----------------------выделить место в новой кат-и
             manager.refresh(nes1);

             Query query2 = manager.createQuery(
                     "update Nes n set n.left = n.left + ?1 where n.left > ?2");
             query2.setParameter(1, nes.getRight() - nes.getLeft() + 1);
             query2.setParameter(2, nes1.getRight());
             query2.executeUpdate();

             Query query3 = manager.createQuery(
                     "update Nes n set n.right = n.right + ?1 where n.right >=?2");
             query3.setParameter(1, nes.getRight() - nes.getLeft() +1);
             query3.setParameter(2, nes1.getRight());
             query3.executeUpdate();

             //отрицательне ключи перемещаемой кат-и сделать положительными
             manager.refresh(nes1);
             System.out.println(nes.getLevel());
             System.out.println(nes1.getLevel());
             Query query1 = manager.createQuery(
                     "update Nes n set n.left = n.left * -1 + ?1, n.right = n.right * -1 + ?1, n.level = n.level - ?2 + ?3 + 1 where n.left < 0 and n.right < 0"
             );
             query1.setParameter(1, nes1.getRight() - nes.getRight() - 1);
             query1.setParameter(2, nes.getLevel());
             query1.setParameter(3, nes1.getLevel());
             query1.executeUpdate();


             manager.getTransaction().commit();
         } catch (Exception e){
             manager.getTransaction().rollback();
             e.printStackTrace();
         } finally {
             manager.close();
         }
    }
}
