package com.namhatta.service;

import com.namhatta.dto.DevotionalStatusDTO;
import com.namhatta.entity.DevotionalStatus;
import com.namhatta.mapper.EntityMapper;
import com.namhatta.repository.DevotionalStatusRepository;
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
import static org.mockito.Mockito.*;

/**
 * Unit tests for DevotionalStatusService
 */
@ExtendWith(MockitoExtension.class)
class DevotionalStatusServiceTest {

    @Mock
    private DevotionalStatusRepository devotionalStatusRepository;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private DevotionalStatusService devotionalStatusService;

    private DevotionalStatus testStatus;
    private DevotionalStatusDTO.CreateRequest createRequest;
    private DevotionalStatusDTO.Response responseDTO;

    @BeforeEach
    void setUp() {
        testStatus = new DevotionalStatus();
        testStatus.setId(1L);
        testStatus.setName("Bhakta");

        createRequest = new DevotionalStatusDTO.CreateRequest();
        createRequest.setName("Initiated");

        responseDTO = new DevotionalStatusDTO.Response();
        responseDTO.setId(1L);
        responseDTO.setName("Bhakta");
    }

    @Test
    void getAllStatuses_WithPageable_ShouldReturnPagedResults() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<DevotionalStatus> statuses = Arrays.asList(testStatus);
        Page<DevotionalStatus> page = new PageImpl<>(statuses, pageable, 1);
        
        when(devotionalStatusRepository.findAll(pageable)).thenReturn(page);
        when(entityMapper.toDevotionalStatusResponse(testStatus)).thenReturn(responseDTO);

        // Act
        Page<DevotionalStatusDTO.Response> result = devotionalStatusService.getAllStatuses(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals("Bhakta", result.getContent().get(0).getName());

        verify(devotionalStatusRepository).findAll(pageable);
        verify(entityMapper).toDevotionalStatusResponse(testStatus);
    }

    @Test
    void getAllStatuses_WithoutPageable_ShouldReturnAllResults() {
        // Arrange
        List<DevotionalStatus> statuses = Arrays.asList(testStatus);
        when(devotionalStatusRepository.findAll()).thenReturn(statuses);
        when(entityMapper.toDevotionalStatusResponse(testStatus)).thenReturn(responseDTO);

        // Act
        List<DevotionalStatusDTO.Response> result = devotionalStatusService.getAllStatuses();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Bhakta", result.get(0).getName());

        verify(devotionalStatusRepository).findAll();
        verify(entityMapper).toDevotionalStatusResponse(testStatus);
    }

    @Test
    void getStatusById_WithExistingId_ShouldReturnStatus() {
        // Arrange
        when(devotionalStatusRepository.findById(1L)).thenReturn(Optional.of(testStatus));
        when(entityMapper.toDevotionalStatusResponse(testStatus)).thenReturn(responseDTO);

        // Act
        DevotionalStatusDTO.Response result = devotionalStatusService.getStatusById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Bhakta", result.getName());
        assertEquals(1L, result.getId());

        verify(devotionalStatusRepository).findById(1L);
        verify(entityMapper).toDevotionalStatusResponse(testStatus);
    }

    @Test
    void getStatusById_WithNonExistingId_ShouldThrowException() {
        // Arrange
        when(devotionalStatusRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> devotionalStatusService.getStatusById(999L));
        
        assertEquals("DevotionalStatus not found with id: 999", exception.getMessage());
        verify(devotionalStatusRepository).findById(999L);
        verify(entityMapper, never()).toDevotionalStatusResponse(any());
    }

    @Test
    void createStatus_WithValidRequest_ShouldReturnCreatedStatus() {
        // Arrange
        DevotionalStatus newStatus = new DevotionalStatus();
        newStatus.setName("Initiated");
        
        when(devotionalStatusRepository.existsByName("Initiated")).thenReturn(false);
        when(entityMapper.toDevotionalStatusEntity(createRequest)).thenReturn(newStatus);
        when(devotionalStatusRepository.save(any(DevotionalStatus.class))).thenReturn(newStatus);
        when(entityMapper.toDevotionalStatusResponse(newStatus)).thenReturn(responseDTO);

        // Act
        DevotionalStatusDTO.Response result = devotionalStatusService.createStatus(createRequest);

        // Assert
        assertNotNull(result);
        verify(devotionalStatusRepository).existsByName("Initiated");
        verify(entityMapper).toDevotionalStatusEntity(createRequest);
        verify(devotionalStatusRepository).save(any(DevotionalStatus.class));
        verify(entityMapper).toDevotionalStatusResponse(newStatus);
    }

    @Test
    void createStatus_WithDuplicateName_ShouldThrowException() {
        // Arrange
        when(devotionalStatusRepository.existsByName("Initiated")).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> devotionalStatusService.createStatus(createRequest));
        
        assertEquals("DevotionalStatus with name 'Initiated' already exists", exception.getMessage());
        verify(devotionalStatusRepository).existsByName("Initiated");
        verify(devotionalStatusRepository, never()).save(any());
    }

    @Test
    void updateStatusName_WithValidIdAndName_ShouldUpdateSuccessfully() {
        // Arrange
        when(devotionalStatusRepository.findById(1L)).thenReturn(Optional.of(testStatus));
        when(devotionalStatusRepository.existsByName("Updated Status")).thenReturn(false);
        when(devotionalStatusRepository.save(any(DevotionalStatus.class))).thenReturn(testStatus);
        when(entityMapper.toDevotionalStatusResponse(testStatus)).thenReturn(responseDTO);

        // Act
        DevotionalStatusDTO.Response result = devotionalStatusService.updateStatusName(1L, "Updated Status");

        // Assert
        assertNotNull(result);
        assertEquals("Updated Status", testStatus.getName());
        verify(devotionalStatusRepository).findById(1L);
        verify(devotionalStatusRepository).existsByName("Updated Status");
        verify(devotionalStatusRepository).save(testStatus);
        verify(entityMapper).toDevotionalStatusResponse(testStatus);
    }

    @Test
    void updateStatusName_WithNonExistingId_ShouldThrowException() {
        // Arrange
        when(devotionalStatusRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> devotionalStatusService.updateStatusName(999L, "New Name"));
        
        assertEquals("DevotionalStatus not found with id: 999", exception.getMessage());
        verify(devotionalStatusRepository).findById(999L);
        verify(devotionalStatusRepository, never()).save(any());
    }

    @Test
    void updateStatusName_WithDuplicateName_ShouldThrowException() {
        // Arrange
        when(devotionalStatusRepository.findById(1L)).thenReturn(Optional.of(testStatus));
        when(devotionalStatusRepository.existsByName("Existing Status")).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> devotionalStatusService.updateStatusName(1L, "Existing Status"));
        
        assertEquals("DevotionalStatus with name 'Existing Status' already exists", exception.getMessage());
        verify(devotionalStatusRepository).findById(1L);
        verify(devotionalStatusRepository).existsByName("Existing Status");
        verify(devotionalStatusRepository, never()).save(any());
    }

    @Test
    void deleteStatus_WithExistingId_ShouldDeleteSuccessfully() {
        // Arrange
        when(devotionalStatusRepository.existsById(1L)).thenReturn(true);
        doNothing().when(devotionalStatusRepository).deleteById(1L);

        // Act
        devotionalStatusService.deleteStatus(1L);

        // Assert
        verify(devotionalStatusRepository).existsById(1L);
        verify(devotionalStatusRepository).deleteById(1L);
    }

    @Test
    void deleteStatus_WithNonExistingId_ShouldThrowException() {
        // Arrange
        when(devotionalStatusRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> devotionalStatusService.deleteStatus(999L));
        
        assertEquals("DevotionalStatus not found with id: 999", exception.getMessage());
        verify(devotionalStatusRepository).existsById(999L);
        verify(devotionalStatusRepository, never()).deleteById(any());
    }

    @Test
    void isNameUnique_WithExistingName_ShouldReturnFalse() {
        // Arrange
        when(devotionalStatusRepository.existsByName("Bhakta")).thenReturn(true);

        // Act
        boolean result = devotionalStatusService.isNameUnique("Bhakta");

        // Assert
        assertFalse(result);
        verify(devotionalStatusRepository).existsByName("Bhakta");
    }

    @Test
    void isNameUnique_WithNewName_ShouldReturnTrue() {
        // Arrange
        when(devotionalStatusRepository.existsByName("New Status")).thenReturn(false);

        // Act
        boolean result = devotionalStatusService.isNameUnique("New Status");

        // Assert
        assertTrue(result);
        verify(devotionalStatusRepository).existsByName("New Status");
    }
}