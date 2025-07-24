package com.namhatta.repository;

import com.namhatta.entity.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    
    // Additional geography queries for GeographyService
    @Query("SELECT DISTINCT a.country FROM Address a WHERE a.country IS NOT NULL ORDER BY a.country")
    List<String> findDistinctCountries();
    
    @Query("SELECT DISTINCT a.stateNameEnglish FROM Address a WHERE a.stateNameEnglish IS NOT NULL ORDER BY a.stateNameEnglish")
    List<String> findDistinctStates();
    
    @Query("SELECT DISTINCT a.districtNameEnglish FROM Address a WHERE a.districtNameEnglish IS NOT NULL ORDER BY a.districtNameEnglish")
    List<String> findDistinctDistricts();
    
    // Sub-district queries
    @Query("SELECT DISTINCT a.subdistrictNameEnglish FROM Address a WHERE a.subdistrictNameEnglish IS NOT NULL ORDER BY a.subdistrictNameEnglish")
    List<String> findDistinctSubDistricts();
    
    @Query("SELECT DISTINCT a.subdistrictNameEnglish FROM Address a WHERE a.districtNameEnglish = :district AND a.subdistrictNameEnglish IS NOT NULL ORDER BY a.subdistrictNameEnglish")
    List<String> findDistinctSubDistrictsByDistrict(@Param("district") String district);
    
    @Query("SELECT DISTINCT a.subdistrictNameEnglish FROM Address a WHERE a.pincode = :pincode AND a.subdistrictNameEnglish IS NOT NULL ORDER BY a.subdistrictNameEnglish")
    List<String> findDistinctSubDistrictsByPincode(@Param("pincode") String pincode);
    
    @Query("SELECT DISTINCT a.subdistrictNameEnglish FROM Address a WHERE a.districtNameEnglish = :district AND a.pincode = :pincode AND a.subdistrictNameEnglish IS NOT NULL ORDER BY a.subdistrictNameEnglish")
    List<String> findDistinctSubDistrictsByDistrictAndPincode(@Param("district") String district, @Param("pincode") String pincode);
    
    // Village queries
    @Query("SELECT DISTINCT a.villageNameEnglish FROM Address a WHERE a.villageNameEnglish IS NOT NULL ORDER BY a.villageNameEnglish")
    List<String> findDistinctVillages();
    
    @Query("SELECT DISTINCT a.villageNameEnglish FROM Address a WHERE a.subdistrictNameEnglish = :subDistrict AND a.villageNameEnglish IS NOT NULL ORDER BY a.villageNameEnglish")
    List<String> findDistinctVillagesBySubDistrict(@Param("subDistrict") String subDistrict);
    
    @Query("SELECT DISTINCT a.villageNameEnglish FROM Address a WHERE a.pincode = :pincode AND a.villageNameEnglish IS NOT NULL ORDER BY a.villageNameEnglish")
    List<String> findDistinctVillagesByPincode(@Param("pincode") String pincode);
    
    @Query("SELECT DISTINCT a.villageNameEnglish FROM Address a WHERE a.subdistrictNameEnglish = :subDistrict AND a.pincode = :pincode AND a.villageNameEnglish IS NOT NULL ORDER BY a.villageNameEnglish")
    List<String> findDistinctVillagesBySubDistrictAndPincode(@Param("subDistrict") String subDistrict, @Param("pincode") String pincode);
    
    // Pincode queries
    @Query("SELECT DISTINCT a.pincode FROM Address a WHERE a.pincode IS NOT NULL ORDER BY a.pincode")
    List<String> findDistinctPincodes();
    
    @Query("SELECT DISTINCT a.pincode FROM Address a WHERE a.villageNameEnglish = :village AND a.pincode IS NOT NULL ORDER BY a.pincode")
    List<String> findDistinctPincodesByVillage(@Param("village") String village);
    
    @Query("SELECT DISTINCT a.pincode FROM Address a WHERE a.subdistrictNameEnglish = :subDistrict AND a.pincode IS NOT NULL ORDER BY a.pincode")
    List<String> findDistinctPincodesBySubDistrict(@Param("subDistrict") String subDistrict);
    
    @Query("SELECT DISTINCT a.pincode FROM Address a WHERE a.districtNameEnglish = :district AND a.pincode IS NOT NULL ORDER BY a.pincode")
    List<String> findDistinctPincodesByDistrict(@Param("district") String district);
    
    // Pincode search with pagination
    @Query("SELECT DISTINCT a.pincode FROM Address a WHERE a.pincode IS NOT NULL ORDER BY a.pincode")
    Page<String> findAllPincodes(Pageable pageable);
    
    @Query("SELECT DISTINCT a.pincode FROM Address a WHERE a.country = :country AND a.pincode IS NOT NULL ORDER BY a.pincode")
    Page<String> findPincodesByCountry(@Param("country") String country, Pageable pageable);
    
    @Query("SELECT DISTINCT a.pincode FROM Address a WHERE a.pincode LIKE %:searchTerm% ORDER BY a.pincode")
    Page<String> searchPincodesByTerm(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT DISTINCT a.pincode FROM Address a WHERE a.country = :country AND a.pincode LIKE %:searchTerm% ORDER BY a.pincode")
    Page<String> searchPincodesByCountryAndTerm(@Param("country") String country, @Param("searchTerm") String searchTerm, Pageable pageable);
    
    // Address lookup by pincode
    List<Address> findByPincode(String pincode);
}