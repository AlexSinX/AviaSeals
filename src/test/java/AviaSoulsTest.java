import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

public class AviaSoulsTest {
    private AviaSouls manager;
    private Ticket ticket1;
    private Ticket ticket2;
    private Ticket ticket3;
    private Ticket ticket4;
    private Ticket ticket5;
    private Ticket ticket6;

    @BeforeEach
    public void setUp() {
        manager = new AviaSouls();

        // Создаем тестовые билеты
        ticket1 = new Ticket("MSK", "SPB", 5000, 1000, 1200); // время полета 200
        ticket2 = new Ticket("MSK", "SPB", 3000, 900, 1100);  // время полета 200
        ticket3 = new Ticket("MSK", "SPB", 7000, 800, 1200);  // время полета 400
        ticket4 = new Ticket("MSK", "KZN", 4000, 1000, 1300); // другой маршрут
        ticket5 = new Ticket("MSK", "SPB", 2000, 1000, 1100); // время полета 100
        ticket6 = new Ticket("MSK", "SPB", 6000, 1500, 1700); // время полета 200

        manager.add(ticket1);
        manager.add(ticket2);
        manager.add(ticket3);
        manager.add(ticket4);
        manager.add(ticket5);
        manager.add(ticket6);
    }

    @Test
    public void testCompareTo() {
        // Проверяем, что билет с меньшей ценой считается меньше
        Assertions.assertTrue(ticket5.compareTo(ticket2) < 0); // 2000 < 3000
        Assertions.assertTrue(ticket2.compareTo(ticket1) < 0); // 3000 < 5000
        Assertions.assertTrue(ticket1.compareTo(ticket3) < 0); // 5000 < 7000
        Assertions.assertTrue(ticket3.compareTo(ticket2) > 0); // 7000 > 3000
        Assertions.assertEquals(0, ticket1.compareTo(ticket1)); // сравнение с самим собой
    }

    @Test
    public void testSearchWithNoResults() {
        Ticket[] expected = {};
        Ticket[] actual = manager.search("MSK", "NYC");

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void testSearchWithSingleResult() {
        AviaSouls singleManager = new AviaSouls();
        Ticket singleTicket = new Ticket("MSK", "LED", 4000, 1000, 1200);
        singleManager.add(singleTicket);

        Ticket[] expected = {singleTicket};
        Ticket[] actual = singleManager.search("MSK", "LED");

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void testTicketTimeComparator() {
        TicketTimeComparator comparator = new TicketTimeComparator();

        // Сравниваем билеты с разным временем полета
        Assertions.assertTrue(comparator.compare(ticket5, ticket3) < 0); // 100 < 400
        Assertions.assertTrue(comparator.compare(ticket3, ticket5) > 0); // 400 > 100

        // Сравниваем билеты с одинаковым временем полета
        Assertions.assertEquals(0, comparator.compare(ticket1, ticket2)); // оба 200
        Assertions.assertEquals(0, comparator.compare(ticket1, ticket6)); // оба 200
    }

    @Test
    public void testSearchAndSortByTime() {
        TicketTimeComparator timeComparator = new TicketTimeComparator();

        // Ожидаемый порядок по времени полета:
        // ticket5 (100), затем ticket1, ticket2, ticket6 (все 200), затем ticket3 (400)
        Ticket[] expected = {ticket5, ticket1, ticket2, ticket6, ticket3};
        Ticket[] actual = manager.searchAndSortBy("MSK", "SPB", timeComparator);

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void testSearchAndSortByTimeWithNoResults() {
        TicketTimeComparator timeComparator = new TicketTimeComparator();

        Ticket[] expected = {};
        Ticket[] actual = manager.searchAndSortBy("MSK", "NYC", timeComparator);

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void testSearchAndSortByTimeWithSingleResult() {
        AviaSouls singleManager = new AviaSouls();
        TicketTimeComparator timeComparator = new TicketTimeComparator();
        Ticket singleTicket = new Ticket("MSK", "LED", 4000, 1000, 1200);
        singleManager.add(singleTicket);

        Ticket[] expected = {singleTicket};
        Ticket[] actual = singleManager.searchAndSortBy("MSK", "LED", timeComparator);

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void testSearchAndSortWithCustomComparator() {
        // Создаем компаратор для сортировки по убыванию цены
        Comparator<Ticket> priceDescComparator = (t1, t2) ->
                Integer.compare(t2.getPrice(), t1.getPrice());

        Ticket[] expected = {ticket3, ticket6, ticket1, ticket2, ticket5};
        Ticket[] actual = manager.searchAndSortBy("MSK", "SPB", priceDescComparator);

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFindAll() {
        Ticket[] expected = {ticket1, ticket2, ticket3, ticket4, ticket5, ticket6};
        Ticket[] actual = manager.findAll();

        Assertions.assertArrayEquals(expected, actual);
    }
}