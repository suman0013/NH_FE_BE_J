package com.namhatta.service;

import com.namhatta.entity.Leader;
import com.namhatta.repository.LeaderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeaderService {
    
    private final LeaderRepository leaderRepository;
    
    public List<Leader> getAllLeaders() {
        log.debug("Fetching all leaders");
        return leaderRepository.findAll();
    }
    
    public List<Leader> getTopLevelLeaders() {
        log.debug("Fetching top-level leaders");
        return leaderRepository.findTopLevelLeaders();
    }
    
    public List<Leader> getLeadersByRole(String role) {
        log.debug("Fetching leaders by role: {}", role);
        return leaderRepository.findByRole(role);
    }
    
    public List<Leader> getLeadersByReportingTo(Long reportingTo) {
        log.debug("Fetching leaders reporting to: {}", reportingTo);
        return leaderRepository.findByReportingTo(reportingTo);
    }
    
    public List<Leader> getLeadersByRoleAndLocation(String role, String country, String state, String district) {
        log.debug("Fetching leaders by role and location - role: {}, country: {}, state: {}, district: {}", 
                 role, country, state, district);
        return leaderRepository.findByRoleAndLocation(role, country, state, district);
    }
    
    public Optional<Leader> getLeaderById(Long id) {
        log.debug("Fetching leader by ID: {}", id);
        return leaderRepository.findById(id);
    }
    
    public Leader createLeader(Leader leader) {
        log.info("Creating new leader: {}", leader.getName());
        return leaderRepository.save(leader);
    }
    
    public Leader updateLeader(Long id, Leader leaderData) {
        log.info("Updating leader ID: {}", id);
        return leaderRepository.findById(id)
                .map(existing -> {
                    existing.setName(leaderData.getName());
                    existing.setRole(leaderData.getRole());
                    existing.setReportingTo(leaderData.getReportingTo());
                    existing.setCountry(leaderData.getCountry());
                    existing.setState(leaderData.getState());
                    existing.setDistrict(leaderData.getDistrict());
                    return leaderRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Leader not found with id: " + id));
    }
    
    public void deleteLeader(Long id) {
        log.info("Deleting leader ID: {}", id);
        leaderRepository.deleteById(id);
    }
    
    public List<String> getAllRoles() {
        log.debug("Fetching all leadership roles");
        return leaderRepository.findAllRoles();
    }
}