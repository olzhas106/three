import entity.Nes;

import javax.persistence.*;
import javax.swing.text.html.parser.Entity;
import javax.swing.text.html.parser.Parser;
import java.util.List;
import java.util.Scanner;

public class Delete {
    public static void main(String[] args) {
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
        System.out.println("Какую категорию следует удалить? Введите id:");
        String id = scanner.nextLine();
        int idInt = Integer.parseInt(id);
        long idLong = Long.parseLong(id);
        Nes nes = manager.find(Nes.class, idLong);

        try {
            manager.getTransaction().begin();

            Query query = manager.createQuery(
                    "delete from Nes n where  n.left >= ?1 and n.right <= ?2");
            query.setParameter(1, nes.getLeft());
            query.setParameter(2, nes.getRight());
            query.executeUpdate();

            Query query1 = manager.createQuery(
                    "update Nes n set n.left = n.left - ?1 where n.left > ?2");
            query1.setParameter(1, nes.getRight() - nes.getLeft() + 1);
            query1.setParameter(2, nes.getRight());
            query1.executeUpdate();

            Query query2 = manager.createQuery(
                    "update Nes n set  n.right = n.right - ?1 where n.right > ?2");
            query2.setParameter(1, nes.getRight()- nes.getLeft() + 1);
            query2.setParameter(2,nes.getRight());
            query2.executeUpdate();

            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        }
    }
}
