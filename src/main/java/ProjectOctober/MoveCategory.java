package ProjectOctober;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.Scanner;

public class MoveCategory {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();

        try {
            manager.getTransaction().begin();

            Scanner scanner = new Scanner(System.in);

            System.out.println("Введите id категории: ");
            String categoryId = scanner.nextLine();
            Tree parent = manager.find(Tree.class, Long.parseLong(categoryId));

            Query queryKeys = manager.createQuery("update Tree set left_key = left_key * -1, right_key = right_key * -1 where left_key >= ?1 and   right_key <=?2");
            queryKeys.setParameter(1, parent.getLeft_key());
            queryKeys.setParameter(2, parent.getRight_key());
            queryKeys.executeUpdate();

            Query queryRightKey = manager.createQuery("update Tree set right_key = right_key - ?1 where right_key > ?2");
            queryRightKey.setParameter(1, parent.getRight_key() - parent.getLeft_key() + 1);
            queryRightKey.setParameter(2, parent.getRight_key());
            queryRightKey.executeUpdate();
            Query queryLeftKey = manager.createQuery("update Tree set left_key = left_key - ?1 where left_key > ?2");
            queryLeftKey.setParameter(1, parent.getRight_key() - parent.getLeft_key() + 1);
            queryLeftKey.setParameter(2, parent.getRight_key());
            queryLeftKey.executeUpdate();

            System.out.println("Введите новое место: ");
            String newCategory = scanner.nextLine();
            Tree newPlace = manager.find(Tree.class, Long.parseLong(newCategory));

            Query query1 = manager.createQuery("update Tree set right_key = right_key + ?1 where right_key>=?2");
            query1.setParameter(1, parent.getRight_key() - parent.getLeft_key() + 1);
            query1.setParameter(2, newPlace.getRight_key());
            query1.executeUpdate();

            Query query2 = manager.createQuery("update Tree set left_key = left_key + ?1 where left_key>?2");
            query2.setParameter(1, parent.getRight_key() - parent.getLeft_key() + 1);
            query2.setParameter(2, newPlace.getRight_key());
            query2.executeUpdate();

            manager.refresh(newPlace);

            Query queryNewKeys = manager.createQuery(
                    "update Tree set left_key = 0 - left_key + (?1), right_key = 0 - right_key +(?1), level = level - ?2 + ?3 + 1 where left_key < 0 and right_key < 0");
            queryNewKeys.setParameter(1, newPlace.getRight_key() - parent.getRight_key() - 1);
            queryNewKeys.setParameter(2, parent.getLevel());
            queryNewKeys.setParameter(3, newPlace.getLevel());
            queryNewKeys.executeUpdate();

            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        }
    }
}
