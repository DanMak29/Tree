package ProjectOctober;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();
        // Комплектующие
        // - Процессоры
        // - - Intel
        // - - AMD
        // - ОЗУ
        // Аудиотехника
        // - Наушники
        // - Колонки
        try {
            manager.getTransaction().begin();

            Scanner scanner = new Scanner(System.in);

            System.out.println("Список:");
            TypedQuery<Tree> query = manager.createQuery(
                    "select t from Tree t", Tree.class
            );
            List<Tree> treeList = query.getResultList();
            for (Tree tree : treeList) {
                String str = " -";
                String repeated = str.repeat(tree.getLevel() - 1);
                System.out.println(repeated + tree.getName());
            }
        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        }
    }
}
