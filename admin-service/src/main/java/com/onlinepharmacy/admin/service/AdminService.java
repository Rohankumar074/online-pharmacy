package com.onlinepharmacy.admin.service;

import com.onlinepharmacy.admin.client.CatalogAdminClient;
import com.onlinepharmacy.admin.client.OrderAdminClient;
import com.onlinepharmacy.admin.dto.response.SalesReportDto;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

  private final CatalogAdminClient catalogAdminClient;
  private final OrderAdminClient orderAdminClient;

  public AdminService(CatalogAdminClient catalogAdminClient, OrderAdminClient orderAdminClient) {
    this.catalogAdminClient = catalogAdminClient;
    this.orderAdminClient = orderAdminClient;
  }

  public CatalogAdminClient.AdminDashboard getDashboardData() {
    CatalogAdminClient.CatalogStatsDto catalogStats = catalogAdminClient.getCatalogStats();
    long todayOrders = orderAdminClient.getTodayOrderCount();
    List<CatalogAdminClient.MedicineDto> meds = catalogAdminClient.listMedicines();

    return new CatalogAdminClient.AdminDashboard(
        catalogStats.lowStockCount(),
        meds.size(),
        catalogStats.pendingPrescriptions(),
        catalogStats.expiringBatchesCount(),
        todayOrders
    );
  }

  public CatalogAdminClient.MedicineDto createMedicine(CatalogAdminClient.AdminMedicineRequestDto req) {
    return catalogAdminClient.createMedicine(req);
  }

  public OrderAdminClient.OrderDetailsDto updateOrderStatus(Long id, OrderAdminClient.UpdateOrderStatusRequest req) {
    return orderAdminClient.updateOrderStatus(id, req);
  }

  public SalesReportDto getSalesReport() {
    OrderAdminClient.SalesReportDto report = orderAdminClient.getSalesReport();
    return new SalesReportDto(report.totalOrders(), report.totalRevenue().doubleValue(), "Real-time Sales Report");
  }

  public Map<String, Long> getPrescriptionVolume() {
    return catalogAdminClient.getPrescriptionVolume();
  }

  public List<CatalogAdminClient.BatchDto> getExpiringBatches() {
    return catalogAdminClient.getExpiringBatches();
  }

  public void approvePrescription(Long id) {
    catalogAdminClient.approvePrescription(id);
  }

  public void rejectPrescription(Long id, CatalogAdminClient.RejectRequestDto req) {
    catalogAdminClient.rejectPrescription(id, req);
  }
}
