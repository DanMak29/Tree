package ProjectOctober;

import ProjectOctober.Entity.Tree;

import javax.persistence.*;
import java.util.List;
import java.util.Scanner;

public class Application {

    private static final String WRITE_ID_OF_CATEGORY = "Введите id категории: ";
    private static final String ONE = "1";
    private static final String TWO = "2";
    private static final String THREE = "3";
    private static final String FOUR = "4";

    private static final EntityManagerFactory factory =
            Persistence.createEntityManagerFactory("main");

    private static final Scanner IN = new Scanner(System.in);

    public static void main(String[] args) {

        while (true) {


            System.out.println("-Создание[1]");
            System.out.println("-Редактирование[2]");
            System.out.println("-Удаление[3]");
            System.out.println("-Завершить программу[4]");

            System.out.println("Выберите действие: ");
            String action = IN.nextLine();

            switch (action) {
                case ONE -> create();
                case TWO -> update();
                case THREE -> delete();
                case FOUR -> {
                    return;
                }
            }
        }
    }

    private static void create() {
        EntityManager manager = factory.createEntityManager();


        try {

            manager.getTransaction().begin();

            TypedQuery<Tree> query = manager.createQuery(
                    "select t from Tree t", Tree.class
            );
            List<Tree> treeList = query.getResultList();
            for (Tree tree : treeList) {
                System.out.println(tree.getId() + ") " + tree.getName());
            }
            System.out.println(WRITE_ID_OF_CATEGORY);
            String categoryId = IN.nextLine();
            Tree parent = manager.find(Tree.class, Long.parseLong(categoryId));

            Query query1 = manager.createQuery("update Tree set right_key = right_key + 2 where right_key>=?1");
            query1.setParameter(1, parent.getRight_key());
            query1.executeUpdate();

            Query query2 = manager.createQuery("update Tree set left_key = left_key + 2 where left_key>?2");
            query2.setParameter(2, parent.getRight_key());
            query2.executeUpdate();

            System.out.println("Введите название: ");
            String name = IN.nextLine();

            Tree tree = new Tree();
            tree.setName(name);
            tree.setLevel(parent.getLevel() + 1);
            tree.setRight_key(parent.getRight_key() + 1);
            tree.setLeft_key(parent.getRight_key());
            manager.persist(tree);

            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            manager.close();
        }
    }

    private static void update() {
        EntityManager manager = factory.createEntityManager();

        try {
            manager.getTransaction().begin();

            System.out.println(WRITE_ID_OF_CATEGORY);
            String categoryId = IN.nextLine();
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
            String newCategory = IN.nextLine();
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
        } finally {
            manager.close();
        }
    }

    private static void delete() {
        EntityManager manager = factory.createEntityManager();

        try {
            manager.getTransaction().begin();
            TypedQuery<Tree> query = manager.createQuery(
                    "select t from Tree t", Tree.class
            );
            List<Tree> treeList = query.getResultList();
            for (Tree tree : treeList) {
                System.out.println(tree.getId() + ") " + tree.getName());
            }

            System.out.println(WRITE_ID_OF_CATEGORY);
            String categoryId = IN.nextLine();
            Tree parent = manager.find(Tree.class, Long.parseLong(categoryId));

            Query query1 = manager.createQuery("delete from Tree where left_key >= ?1 and   right_key <=?2");
            query1.setParameter(1, parent.getLeft_key());
            query1.setParameter(2, parent.getRight_key());
            query1.executeUpdate();

            Query queryRight_key = manager.createQuery("update Tree set right_key = right_key - ?1 where right_key > ?2");
            queryRight_key.setParameter(1, parent.getRight_key() - parent.getLeft_key() + 1);
            queryRight_key.setParameter(2, parent.getRight_key());
            queryRight_key.executeUpdate();

            Query queryLeft_key = manager.createQuery("update Tree set left_key = left_key - ?1 where left_key > ?2");
            queryLeft_key.setParameter(1, parent.getRight_key() - parent.getLeft_key() + 1);
            queryLeft_key.setParameter(2, parent.getRight_key());
            queryLeft_key.executeUpdate();

            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            manager.close();
        }
    }
}
