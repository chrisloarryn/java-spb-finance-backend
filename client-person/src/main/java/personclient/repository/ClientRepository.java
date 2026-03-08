package personclient.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import personclient.entities.Client;

import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> { }