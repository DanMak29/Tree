package ProjectOctober;

import javax.persistence.*;
import java.util.List;
import java.util.Scanner;

public class NewCategory {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();

        // Введите id категории: ___

        // Увеличить правый ключ на 2 всем категориям у которых правый ключ больше или равен правому ключу
        // родительской категории.
        // Увеличить левый ключ на 2 всем категориям у которых левый ключ больше правого ключа родителя.

//        Query query = manager.createQuery("");
//        query.executeUpdate();

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

            if (categoryId.equals("0")) {
                System.out.println("Введите название: ");
                String name = scanner.nextLine();

//
//                Tree tree = new Tree();
//                tree.setName(name);
//                tree.setLevel(1);
//                tree.setLeft_key(newPlace.getLeft_key());
////                tree.setRight_key(newPlace.getRight_key() + 1);
//
//                manager.persist(tree);

            } else {

                Query query1 = manager.createQuery("update Tree set right_key = right_key + 2 where right_key>=?1");
                query1.setParameter(1, parent.getRight_key());
                Query query2 = manager.createQuery("update Tree set left_key = left_key + 2 where left_key>?2");
                query2.setParameter(2, parent.getRight_key());
                query1.executeUpdate();
                query2.executeUpdate();

                System.out.println("Введите название: ");
                String name = scanner.nextLine();

                Tree tree = new Tree();
                tree.setName(name);
                tree.setLevel(parent.getLevel() + 1);
                tree.setRight_key(parent.getRight_key() + 1);
                tree.setLeft_key(parent.getRight_key());
                manager.persist(tree);
            }

            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        }

    }
}
