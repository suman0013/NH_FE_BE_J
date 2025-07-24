package com.namhatta.service;

import com.namhatta.dto.DevoteeDTO;
import com.namhatta.entity.Devotee;
import com.namhatta.entity.DevotionalStatus;
import com.namhatta.entity.Namhatta;
import com.namhatta.mapper.EntityMapper;
import com.namhatta.repository.DevoteeRepository;
import com.namhatta.repository.DevotionalStatusRepository;
import com.namhatta.repository.NamhattaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Unit tests for DevoteeService
 */
@ExtendWith(MockitoExtension.class)
class DevoteeServiceTest {

    @Mock
    private DevoteeRepository devoteeRepository;

    @Mock
    private NamhattaRepository namhattaRepository;

    @Mock
    private DevotionalStatusRepository devotionalStatusRepository;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private DevoteeService devoteeService;

    private Devotee testDevotee;
    private DevoteeDTO.CreateRequest createRequest;
    private DevoteeDTO.UpdateRequest updateRequest;
    private DevoteeDTO.Response responseDTO;
    private Namhatta testNamhatta;
    private DevotionalStatus testStatus;

    @BeforeEach
    void setUp() {
        testDevotee = new Devotee();
        testDevotee.setId(1L);
        testDevotee.setLegalName("John Doe");
        testDevotee.setSpiritualName("Bhakta John");
        testDevotee.setPhoneNumber("1234567890");
        testDevotee.setEmail("john@example.com");

        testNamhatta = new Namhatta();
        testNamhatta.setId(1L);
        testNamhatta.setName("Test Namhatta");

        testStatus = new DevotionalStatus();
        testStatus.setId(1L);
        testStatus.setName("Bhakta");

        createRequest = new DevoteeDTO.CreateRequest();
        createRequest.setLegalName("John Doe");
        createRequest.setSpiritualName("Bhakta John");
        createRequest.setPhoneNumber("1234567890");
        createRequest.setEmail("john@example.com");
        createRequest.setNamhattaId(1L);
        createRequest.setDevotionalStatusId(1L);

        updateRequest = new DevoteeDTO.UpdateRequest();
        updateRequest.setLegalName("John Smith");
        updateRequest.setSpiritualName("Bhakta John");
        updateRequest.setPhoneNumber("0987654321");
        updateRequest.setEmail("john.smith@example.com");

        responseDTO = new DevoteeDTO.Response();
        responseDTO.setId(1L);
        responseDTO.setLegalName("John Doe");
        responseDTO.setSpiritualName("Bhakta John");
        responseDTO.setPhoneNumber("1234567890");
        responseDTO.setEmail("john@example.com");
    }

    @Test
    void getAllDevotees_WithPageable_ShouldReturnPagedResults() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<Devotee> devotees = Arrays.asList(testDevotee);
        Page<Devotee> page = new PageImpl<>(devotees, pageable, 1);
        
        when(devoteeRepository.findAll(pageable)).thenReturn(page);
        when(entityMapper.toDevoteeResponse(testDevotee)).thenReturn(responseDTO);

        // Act
        Page<DevoteeDTO.Response> result = devoteeService.getAllDevotees(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals("John Doe", result.getContent().get(0).getLegalName());

        verify(devoteeRepository).findAll(pageable);
        verify(entityMapper).toDevoteeResponse(testDevotee);
    }

    @Test
    void getDevoteeById_WithExistingId_ShouldReturnDevotee() {
        // Arrange
        when(devoteeRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(testDevotee));
        when(entityMapper.toDevoteeResponse(testDevotee)).thenReturn(responseDTO);

        // Act
        DevoteeDTO.Response result = devoteeService.getDevoteeById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getLegalName());
        assertEquals("Bhakta John", result.getSpiritualName());

        verify(devoteeRepository).findByIdWithDetails(1L);
        verify(entityMapper).toDevoteeResponse(testDevotee);
    }

    @Test
    void getDevoteeById_WithNonExistingId_ShouldThrowException() {
        // Arrange
        when(devoteeRepository.findByIdWithDetails(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> devoteeService.getDevoteeById(999L));
        
        assertEquals("Devotee not found with id: 999", exception.getMessage());
        verify(devoteeRepository).findByIdWithDetails(999L);
        verify(entityMapper, never()).toDevoteeResponse(any());
    }

    @Test
    void createDevotee_WithValidRequest_ShouldReturnCreatedDevotee() {
        // Arrange
        when(namhattaRepository.findById(1L)).thenReturn(Optional.of(testNamhatta));
        when(devotionalStatusRepository.findById(1L)).thenReturn(Optional.of(testStatus));
        when(entityMapper.toDevoteeEntity(createRequest)).thenReturn(testDevotee);
        when(devoteeRepository.save(any(Devotee.class))).thenReturn(testDevotee);
        when(entityMapper.toDevoteeResponse(testDevotee)).thenReturn(responseDTO);

        // Act
        DevoteeDTO.Response result = devoteeService.createDevotee(createRequest);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getLegalName());

        verify(namhattaRepository).findById(1L);
        verify(devotionalStatusRepository).findById(1L);
        verify(entityMapper).toDevoteeEntity(createRequest);
        verify(devoteeRepository).save(any(Devotee.class));
        verify(entityMapper).toDevoteeResponse(testDevotee);
    }

    @Test
    void createDevotee_WithInvalidNamhattaId_ShouldThrowException() {
        // Arrange
        when(namhattaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> devoteeService.createDevotee(createRequest));
        
        assertEquals("Namhatta not found with id: 1", exception.getMessage());
        verify(namhattaRepository).findById(1L);
        verify(devoteeRepository, never()).save(any());
    }

    @Test
    void updateDevotee_WithValidRequest_ShouldReturnUpdatedDevotee() {
        // Arrange
        when(devoteeRepository.findById(1L)).thenReturn(Optional.of(testDevotee));
        when(devoteeRepository.save(any(Devotee.class))).thenReturn(testDevotee);
        when(entityMapper.toDevoteeResponse(testDevotee)).thenReturn(responseDTO);

        // Act
        DevoteeDTO.Response result = devoteeService.updateDevotee(1L, updateRequest);

        // Assert
        assertNotNull(result);
        verify(devoteeRepository).findById(1L);
        verify(devoteeRepository).save(testDevotee);
        verify(entityMapper).toDevoteeResponse(testDevotee);
    }

    @Test
    void updateDevotee_WithNonExistingId_ShouldThrowException() {
        // Arrange
        when(devoteeRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> devoteeService.updateDevotee(999L, updateRequest));
        
        assertEquals("Devotee not found with id: 999", exception.getMessage());
        verify(devoteeRepository).findById(999L);
        verify(devoteeRepository, never()).save(any());
    }

    @Test
    void deleteDevotee_WithExistingId_ShouldDeleteSuccessfully() {
        // Arrange
        when(devoteeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(devoteeRepository).deleteById(1L);

        // Act
        devoteeService.deleteDevotee(1L);

        // Assert
        verify(devoteeRepository).existsById(1L);
        verify(devoteeRepository).deleteById(1L);
    }

    @Test
    void deleteDevotee_WithNonExistingId_ShouldThrowException() {
        // Arrange
        when(devoteeRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> devoteeService.deleteDevotee(999L));
        
        assertEquals("Devotee not found with id: 999", exception.getMessage());
        verify(devoteeRepository).existsById(999L);
        verify(devoteeRepository, never()).deleteById(anyLong());
    }

    @Test
    void searchDevoteesByName_WithValidName_ShouldReturnResults() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<Devotee> devotees = Arrays.asList(testDevotee);
        Page<Devotee> page = new PageImpl<>(devotees, pageable, 1);
        
        when(devoteeRepository.findByLegalNameContainingIgnoreCase("John", pageable)).thenReturn(page);
        when(entityMapper.toDevoteeResponse(testDevotee)).thenReturn(responseDTO);

        // Act
        Page<DevoteeDTO.Response> result = devoteeService.searchDevoteesByName("John", pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("John Doe", result.getContent().get(0).getLegalName());

        verify(devoteeRepository).findByLegalNameContainingIgnoreCase("John", pageable);
        verify(entityMapper).toDevoteeResponse(testDevotee);
    }
}