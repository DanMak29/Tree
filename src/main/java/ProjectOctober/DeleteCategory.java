package ProjectOctober;

import javax.persistence.*;
import java.util.List;
import java.util.Scanner;

public class DeleteCategory {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();

        try {
            manager.getTransaction().begin();

            Scanner scanner = new Scanner(System.in);

            TypedQuery<Tree> query = manager.createQuery(
                    "select t from Tree t", Tree.class
            );
            List<Tree> treeList = query.getResultList();
            for (Tree tree : treeList) {
                System.out.println(tree.getId() + ") " + tree.getName());
            }
            System.out.println("Введите id категории: ");
            String categoryId = scanner.nextLine();
            Tree parent = manager.find(Tree.class, Long.parseLong(categoryId));

            Query query1 = manager.createQuery("delete from Tree where left_key >= ?1 and   right_key <=?2");
            query1.setParameter(1, parent.getLeft_key());
            query1.setParameter(2, parent.getRight_key());
            Query queryRight_key = manager.createQuery("update Tree set right_key = right_key - ?1 where right_key > ?2");
            queryRight_key.setParameter(1, parent.getRight_key() - parent.getLeft_key() + 1);
            queryRight_key.setParameter(2, parent.getRight_key());
            Query queryLeft_key = manager.createQuery("update Tree set left_key = left_key - ?1 where left_key > ?2");
            queryLeft_key.setParameter(1, parent.getRight_key() - parent.getLeft_key() + 1);
            queryLeft_key.setParameter(2, parent.getRight_key());
            query1.executeUpdate();
            queryLeft_key.executeUpdate();
            queryRight_key.executeUpdate();

            Tree tree = new Tree();
            tree.setLeft_key(parent.getRight_key());
            tree.setRight_key(parent.getRight_key() - 1);

            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        }
    }
}
