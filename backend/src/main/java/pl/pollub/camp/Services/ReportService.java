package pl.pollub.camp.Services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pollub.camp.Models.DTO.ReportRequest;
import pl.pollub.camp.Models.OrderStatus;
import pl.pollub.camp.Models.ReportType;
import pl.pollub.camp.Models.Reports;
import pl.pollub.camp.Models.Reservations;
import pl.pollub.camp.Repositories.ReportRepository;
import pl.pollub.camp.Repositories.ReservationRepository;

import java.util.Optional;

@Service
public class ReportService {
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private ReservationRepository reservationRepository;


    public Reports createReport(ReportRequest reportRequest,ReportType reportType){
        Optional<Reservations> reservation = reservationRepository.findById(reportRequest.getReservationid());
        Reports report = new Reports();
        if(reservation.isPresent()){
            report.setReportType(reportType);
            report.setReportDate(reportRequest.getReportDate());
            report.setComment(reportRequest.getComment());
            report.setReservation(reservation.get());
            report.setInteriorCondition(reportRequest.getInteriorCondition());
            report.setTechnicalCondition(reportRequest.getTechnicalCondition());
            report.setVisualCondition(reportRequest.getVisualCondition());

            if(reportType == ReportType.PICKUP){
                reservation.get().getOrder().setOrderStatus(OrderStatus.IN_USE);
            } else if (reportType == ReportType.RETURN) {
                reservation.get().getOrder().setOrderStatus(OrderStatus.FINISHED);
            }

            reportRepository.save(report);
        }else{
            throw new EntityNotFoundException("Could not find reservation with id: "+reportRequest.getReservationid());
        }
        return report;
    }

    public Iterable<Reports> getAll() {
        return reportRepository.findAll();
    }

    public Reports deleteReport(int id) {
        Optional<Reports> report = reportRepository.findById(id);
        if(report.isPresent()){
            reportRepository.delete(report.get());
            return report.get();
        }else {
            throw new EntityNotFoundException("report with id "+id+" not found");
        }
    }

    public Reports updateReport(int id, ReportRequest reportRequest) {
        Optional<Reports> reportsOptional = reportRepository.findById(id);
        Optional<Reservations> reservationsOptional =reservationRepository.findById(reportRequest.getReservationid());
        if(!reservationsOptional.isPresent()){
            throw new EntityNotFoundException("reservation with id "+id+" not found");
        }
        if(reportsOptional.isPresent()){
            Reports report = reportsOptional.get();
            report.setReportDate(reportRequest.getReportDate());
            report.setComment(reportRequest.getComment());
            report.setReservation(reservationsOptional.get());
            report.setInteriorCondition(reportRequest.getInteriorCondition());
            report.setTechnicalCondition(reportRequest.getTechnicalCondition());
            report.setVisualCondition(reportRequest.getVisualCondition());

            reportRepository.save(report);
            return report;

        }else {
            throw new EntityNotFoundException("report with id "+id+" not found");
        }
    }
}
