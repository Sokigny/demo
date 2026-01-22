package com.cinema.demo.service;

import com.cinema.demo.model.Client;
import com.cinema.demo.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    
    @Autowired
    private ClientRepository clientRepository;
    
    // Create
    public Client createClient(Client client) {
        if (clientRepository.existsByEmail(client.getEmail())) {
            throw new RuntimeException("Un client avec cet email existe déjà");
        }
        return clientRepository.save(client);
    }
    
    // Read - tous les clients
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
    
    // Read - par ID
    public Optional<Client> getClientById(Integer id) {
        return clientRepository.findById(id);
    }
    
    // Read - par email
    public Optional<Client> getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }
    
    // Read - recherche par nom
    public List<Client> searchByNom(String nom) {
        return clientRepository.findByNomContainingIgnoreCase(nom);
    }
    
    // Read - recherche par prénom
    public List<Client> searchByPrenom(String prenom) {
        return clientRepository.findByPrenomContainingIgnoreCase(prenom);
    }
    
    // Update
    public Client updateClient(Integer id, Client clientDetails) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'id: " + id));
        
        client.setNom(clientDetails.getNom());
        client.setPrenom(clientDetails.getPrenom());
        client.setEmail(clientDetails.getEmail());
        client.setTelephone(clientDetails.getTelephone());
        
        return clientRepository.save(client);
    }
    
    // Delete
    public void deleteClient(Integer id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'id: " + id));
        clientRepository.delete(client);
    }
    
    // Vérifier si l'email existe
    public boolean emailExists(String email) {
        return clientRepository.existsByEmail(email);
    }
}
