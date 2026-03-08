package personclient.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import personclient.entities.Client;

import java.util.UUID;

public interface PersonRepository extends JpaRepository<Client, UUID>
{
}