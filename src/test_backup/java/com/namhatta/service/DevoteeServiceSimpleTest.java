package com.namhatta.service;

import com.namhatta.dto.DevoteeDTO;
import com.namhatta.entity.Devotee;
import com.namhatta.mapper.EntityMapper;
import com.namhatta.repository.DevoteeRepository;
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
 * Unit tests for DevoteeService - Simplified version
 */
@ExtendWith(MockitoExtension.class)
class DevoteeServiceSimpleTest {

    @Mock
    private DevoteeRepository devoteeRepository;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private DevoteeService devoteeService;

    private Devotee testDevotee;
    private DevoteeDTO devoteeDTO;

    @BeforeEach
    void setUp() {
        testDevotee = new Devotee();
        testDevotee.setId(1L);
        testDevotee.setLegalName("John Doe");
        testDevotee.setSpiritualName("Bhakta John");

        devoteeDTO = new DevoteeDTO();
        devoteeDTO.setId(1L);
        devoteeDTO.setLegalName("John Doe");
        devoteeDTO.setSpiritualName("Bhakta John");
    }

    @Test
    void getAllDevotees_WithPageable_ShouldReturnPagedResults() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<Devotee> devotees = Arrays.asList(testDevotee);
        Page<Devotee> page = new PageImpl<>(devotees, pageable, 1);
        
        when(devoteeRepository.findAll(pageable)).thenReturn(page);
        when(entityMapper.toDevoteeResponse(testDevotee)).thenReturn(devoteeDTO);

        // Act
        Page<DevoteeDTO> result = devoteeService.getAllDevotees(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("John Doe", result.getContent().get(0).getLegalName());

        verify(devoteeRepository).findAll(pageable);
        verify(entityMapper).toDevoteeResponse(testDevotee);
    }

    @Test
    void getDevoteeById_WithExistingId_ShouldReturnDevotee() {
        // Arrange
        when(devoteeRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(testDevotee));
        when(entityMapper.toDevoteeResponse(testDevotee)).thenReturn(devoteeDTO);

        // Act
        DevoteeDTO result = devoteeService.getDevoteeById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getLegalName());

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
    }
}