package co.id.artslv.repository;

import co.id.artslv.lib.inventory.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by root on 11/10/16.
 */
public interface InventoryRepository extends JpaRepository<Inventory,String>{
    List<Inventory> findByBookstatAndTripdateAndStoporderBetween(String bookstat, LocalDate tripdate, int org, int dest);
    List<Inventory> findByBookstatAndTripdateAndSchedulenokaAndStoporderBetween(String bookstat, LocalDate tripdate, String noka, int org, int dest);
    List<Inventory> findByBookstatAndTripdateAndSchedulenokaAndStoporder(String bookstat,LocalDate tripdate, String noka, int org);
    List<Inventory> findByBookstatAndTripdateAndStoporder(String bookstat,LocalDate tripdate, int org);
    List<Inventory> findByStoporder(int org); //Find Data by Origin stop orders
}
