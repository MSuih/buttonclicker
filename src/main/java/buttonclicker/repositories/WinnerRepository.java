package buttonclicker.repositories;

import buttonclicker.models.Winner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WinnerRepository extends JpaRepository<Winner, Long> {
    // No custom methods
}
