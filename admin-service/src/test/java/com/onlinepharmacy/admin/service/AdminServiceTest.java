package com.onlinepharmacy.admin.service;

import com.onlinepharmacy.admin.client.CatalogAdminClient;
import com.onlinepharmacy.admin.client.OrderAdminClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;

public class AdminServiceTest {

  private AdminService adminService;

  @Mock
  private CatalogAdminClient catalogAdminClient;

  @Mock
  private OrderAdminClient orderAdminClient;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    adminService = new AdminService(catalogAdminClient, orderAdminClient);
  }

  @Test
  public void getDashboardData_aggregatesCorrectly() {
    CatalogAdminClient.CatalogStatsDto stats = new CatalogAdminClient.CatalogStatsDto(5L, 3L, 2L);
    when(catalogAdminClient.getCatalogStats()).thenReturn(stats);
    when(orderAdminClient.getTodayOrderCount()).thenReturn(10L);
    when(catalogAdminClient.listMedicines()).thenReturn(List.of(
        new CatalogAdminClient.MedicineDto(1L, "Med1", null, null, null, null, null, null, null, false, 10, null, null),
        new CatalogAdminClient.MedicineDto(2L, "Med2", null, null, null, null, null, null, null, false, 10, null, null)
    ));

    CatalogAdminClient.AdminDashboard result = adminService.getDashboardData();

    Assertions.assertEquals(3, result.lowStockCount());
    Assertions.assertEquals(2, result.totalMedicines());
    Assertions.assertEquals(5, result.pendingPrescriptions());
    Assertions.assertEquals(10, result.todayOrders());
  }

  @Test
  public void getSalesReport_transformsData() {
    OrderAdminClient.SalesReportDto report = new OrderAdminClient.SalesReportDto(100L, new BigDecimal("5000.00"));
    when(orderAdminClient.getSalesReport()).thenReturn(report);

    com.onlinepharmacy.admin.dto.response.SalesReportDto result = adminService.getSalesReport();

    Assertions.assertEquals(100L, result.getOrders());
    Assertions.assertEquals(5000.0, result.getTotalRevenue());
  }
}
