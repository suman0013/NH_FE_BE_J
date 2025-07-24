package com.namhatta.service;

import com.namhatta.dto.NamhattaDTO;
import com.namhatta.entity.Namhatta;
import com.namhatta.mapper.EntityMapper;
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
 * Unit tests for NamhattaService
 */
@ExtendWith(MockitoExtension.class)
class NamhattaServiceTest {

    @Mock
    private NamhattaRepository namhattaRepository;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private NamhattaService namhattaService;

    private Namhatta testNamhatta;
    private NamhattaDTO.CreateRequest createRequest;
    private NamhattaDTO.UpdateRequest updateRequest;
    private NamhattaDTO.Response responseDTO;

    @BeforeEach
    void setUp() {
        testNamhatta = new Namhatta();
        testNamhatta.setId(1L);
        testNamhatta.setCode("TEST001");
        testNamhatta.setName("Test Namhatta");
        testNamhatta.setMeetingDay("Sunday");
        testNamhatta.setMeetingTime("10:00 AM");
        testNamhatta.setSecretary("Test Secretary");
        testNamhatta.setStatus("APPROVED");

        createRequest = new NamhattaDTO.CreateRequest();
        createRequest.setCode("TEST001");
        createRequest.setName("Test Namhatta");
        createRequest.setMeetingDay("Sunday");
        createRequest.setMeetingTime("10:00 AM");
        createRequest.setSecretary("Test Secretary");

        updateRequest = new NamhattaDTO.UpdateRequest();
        updateRequest.setName("Updated Namhatta");
        updateRequest.setMeetingDay("Saturday");
        updateRequest.setMeetingTime("2:00 PM");
        updateRequest.setSecretary("Updated Secretary");

        responseDTO = new NamhattaDTO.Response();
        responseDTO.setId(1L);
        responseDTO.setCode("TEST001");
        responseDTO.setName("Test Namhatta");
        responseDTO.setMeetingDay("Sunday");
        responseDTO.setMeetingTime("10:00 AM");
        responseDTO.setSecretary("Test Secretary");
        responseDTO.setStatus("APPROVED");
    }

    @Test
    void getAllNamhattas_WithPageable_ShouldReturnPagedResults() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<Namhatta> namhattas = Arrays.asList(testNamhatta);
        Page<Namhatta> page = new PageImpl<>(namhattas, pageable, 1);
        
        when(namhattaRepository.findAll(pageable)).thenReturn(page);
        when(entityMapper.toNamhattaResponse(testNamhatta)).thenReturn(responseDTO);

        // Act
        Page<NamhattaDTO.Response> result = namhattaService.getAllNamhattas(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals("Test Namhatta", result.getContent().get(0).getName());

        verify(namhattaRepository).findAll(pageable);
        verify(entityMapper).toNamhattaResponse(testNamhatta);
    }

    @Test
    void getNamhattaById_WithExistingId_ShouldReturnNamhatta() {
        // Arrange
        when(namhattaRepository.findById(1L)).thenReturn(Optional.of(testNamhatta));
        when(entityMapper.toNamhattaResponse(testNamhatta)).thenReturn(responseDTO);

        // Act
        NamhattaDTO.Response result = namhattaService.getNamhattaById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Test Namhatta", result.getName());
        assertEquals("TEST001", result.getCode());
        assertEquals("APPROVED", result.getStatus());

        verify(namhattaRepository).findById(1L);
        verify(entityMapper).toNamhattaResponse(testNamhatta);
    }

    @Test
    void getNamhattaById_WithNonExistingId_ShouldThrowException() {
        // Arrange
        when(namhattaRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> namhattaService.getNamhattaById(999L));
        
        assertEquals("Namhatta not found with id: 999", exception.getMessage());
        verify(namhattaRepository).findById(999L);
        verify(entityMapper, never()).toNamhattaResponse(any());
    }

    @Test
    void createNamhatta_WithValidRequest_ShouldReturnCreatedNamhatta() {
        // Arrange
        when(namhattaRepository.existsByCode("TEST001")).thenReturn(false);
        when(entityMapper.toNamhattaEntity(createRequest)).thenReturn(testNamhatta);
        when(namhattaRepository.save(any(Namhatta.class))).thenReturn(testNamhatta);
        when(entityMapper.toNamhattaResponse(testNamhatta)).thenReturn(responseDTO);

        // Act
        NamhattaDTO.Response result = namhattaService.createNamhatta(createRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Test Namhatta", result.getName());
        assertEquals("TEST001", result.getCode());

        verify(namhattaRepository).existsByCode("TEST001");
        verify(entityMapper).toNamhattaEntity(createRequest);
        verify(namhattaRepository).save(any(Namhatta.class));
        verify(entityMapper).toNamhattaResponse(testNamhatta);
    }

    @Test
    void createNamhatta_WithDuplicateCode_ShouldThrowException() {
        // Arrange
        when(namhattaRepository.existsByCode("TEST001")).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> namhattaService.createNamhatta(createRequest));
        
        assertEquals("Namhatta code already exists: TEST001", exception.getMessage());
        verify(namhattaRepository).existsByCode("TEST001");
        verify(namhattaRepository, never()).save(any());
    }

    @Test
    void updateNamhatta_WithValidRequest_ShouldReturnUpdatedNamhatta() {
        // Arrange
        when(namhattaRepository.findById(1L)).thenReturn(Optional.of(testNamhatta));
        when(namhattaRepository.save(any(Namhatta.class))).thenReturn(testNamhatta);
        when(entityMapper.toNamhattaResponse(testNamhatta)).thenReturn(responseDTO);

        // Act
        NamhattaDTO.Response result = namhattaService.updateNamhatta(1L, updateRequest);

        // Assert
        assertNotNull(result);
        verify(namhattaRepository).findById(1L);
        verify(namhattaRepository).save(testNamhatta);
        verify(entityMapper).toNamhattaResponse(testNamhatta);
    }

    @Test
    void updateNamhatta_WithNonExistingId_ShouldThrowException() {
        // Arrange
        when(namhattaRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> namhattaService.updateNamhatta(999L, updateRequest));
        
        assertEquals("Namhatta not found with id: 999", exception.getMessage());
        verify(namhattaRepository).findById(999L);
        verify(namhattaRepository, never()).save(any());
    }

    @Test
    void approveNamhatta_WithExistingId_ShouldUpdateStatus() {
        // Arrange
        testNamhatta.setStatus("PENDING_APPROVAL");
        when(namhattaRepository.findById(1L)).thenReturn(Optional.of(testNamhatta));
        when(namhattaRepository.save(any(Namhatta.class))).thenReturn(testNamhatta);
        when(entityMapper.toNamhattaResponse(testNamhatta)).thenReturn(responseDTO);

        // Act
        NamhattaDTO.Response result = namhattaService.approveNamhatta(1L);

        // Assert
        assertNotNull(result);
        assertEquals("APPROVED", testNamhatta.getStatus());
        verify(namhattaRepository).findById(1L);
        verify(namhattaRepository).save(testNamhatta);
        verify(entityMapper).toNamhattaResponse(testNamhatta);
    }

    @Test
    void rejectNamhatta_WithExistingId_ShouldUpdateStatus() {
        // Arrange
        testNamhatta.setStatus("PENDING_APPROVAL");
        when(namhattaRepository.findById(1L)).thenReturn(Optional.of(testNamhatta));
        when(namhattaRepository.save(any(Namhatta.class))).thenReturn(testNamhatta);
        when(entityMapper.toNamhattaResponse(testNamhatta)).thenReturn(responseDTO);

        // Act
        NamhattaDTO.Response result = namhattaService.rejectNamhatta(1L);

        // Assert
        assertNotNull(result);
        assertEquals("REJECTED", testNamhatta.getStatus());
        verify(namhattaRepository).findById(1L);
        verify(namhattaRepository).save(testNamhatta);
        verify(entityMapper).toNamhattaResponse(testNamhatta);
    }

    @Test
    void getPendingNamhattas_ShouldReturnOnlyPendingStatus() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        testNamhatta.setStatus("PENDING_APPROVAL");
        List<Namhatta> pendingNamhattas = Arrays.asList(testNamhatta);
        Page<Namhatta> page = new PageImpl<>(pendingNamhattas, pageable, 1);
        
        when(namhattaRepository.findByStatus("PENDING_APPROVAL", pageable)).thenReturn(page);
        when(entityMapper.toNamhattaResponse(testNamhatta)).thenReturn(responseDTO);

        // Act
        Page<NamhattaDTO.Response> result = namhattaService.getPendingNamhattas(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(namhattaRepository).findByStatus("PENDING_APPROVAL", pageable);
        verify(entityMapper).toNamhattaResponse(testNamhatta);
    }

    @Test
    void isCodeUnique_WithExistingCode_ShouldReturnFalse() {
        // Arrange
        when(namhattaRepository.existsByCode("TEST001")).thenReturn(true);

        // Act
        boolean result = namhattaService.isCodeUnique("TEST001");

        // Assert
        assertFalse(result);
        verify(namhattaRepository).existsByCode("TEST001");
    }

    @Test
    void isCodeUnique_WithNewCode_ShouldReturnTrue() {
        // Arrange
        when(namhattaRepository.existsByCode("NEW001")).thenReturn(false);

        // Act
        boolean result = namhattaService.isCodeUnique("NEW001");

        // Assert
        assertTrue(result);
        verify(namhattaRepository).existsByCode("NEW001");
    }
}