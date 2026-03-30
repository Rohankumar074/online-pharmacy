package com.onlinepharmacy.catalog.controller;

import com.onlinepharmacy.catalog.dto.response.CatalogStatsDto;
import com.onlinepharmacy.catalog.dto.response.MedicineResponseDto;
import com.onlinepharmacy.catalog.service.MedicineService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MedicineController.class)
@AutoConfigureMockMvc(addFilters = false)
public class MedicineControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private MedicineService medicineService;

  @Test
  public void listMedicines_returns200() throws Exception {
    Mockito.when(medicineService.listMedicines(Mockito.anyString(), Mockito.anyString()))
        .thenReturn(Collections.emptyList());

    mockMvc.perform(get("/api/catalog/medicines")
        .param("name", "para"))
        .andExpect(status().isOk());
  }

  @Test
  public void getMedicine_returns200() throws Exception {
    Mockito.when(medicineService.getMedicine(1L))
        .thenReturn(new MedicineResponseDto(1L, "Med", null, null, null, null, null, null, null, false, 0, null, null));

    mockMvc.perform(get("/api/catalog/medicines/1"))
        .andExpect(status().isOk());
  }

  @Test
  public void getCatalogStats_returns200() throws Exception {
    Mockito.when(medicineService.getCatalogStats())
        .thenReturn(new CatalogStatsDto(1, 1, 1));

    mockMvc.perform(get("/api/catalog/medicines/stats"))
        .andExpect(status().isOk());
  }
}
