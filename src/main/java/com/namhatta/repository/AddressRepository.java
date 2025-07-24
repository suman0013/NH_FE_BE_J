package com.namhatta.repository;

import com.namhatta.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    
    List<Address> findByCountry(String country);
    
    List<Address> findByStateNameEnglish(String stateName);
    
    List<Address> findByDistrictNameEnglish(String districtName);
    
    @Query("SELECT DISTINCT a.stateNameEnglish FROM Address a WHERE a.country = :country ORDER BY a.stateNameEnglish")
    List<String> findDistinctStatesByCountry(@Param("country") String country);
    
    @Query("SELECT DISTINCT a.districtNameEnglish FROM Address a WHERE a.stateNameEnglish = :state ORDER BY a.districtNameEnglish")
    List<String> findDistinctDistrictsByState(@Param("state") String state);
    
    Optional<Address> findByCountryAndStateNameEnglishAndDistrictNameEnglishAndSubdistrictNameEnglishAndVillageNameEnglishAndPincode(
        String country, String stateNameEnglish, String districtNameEnglish, 
        String subdistrictNameEnglish, String villageNameEnglish, String pincode);
}