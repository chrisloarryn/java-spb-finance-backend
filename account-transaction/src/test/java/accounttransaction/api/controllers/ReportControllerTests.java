package accounttransaction.api.controllers;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import accounttransaction.business.abstracts.ReportService;
import accounttransaction.business.dto.responses.get.GetReportResponse;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportControllerTests {

    @Mock
    private ReportService service;

    @InjectMocks
    private ReportController controller;

    @Test
    void getAllParsesTheClientIdentifierAndDelegatesToTheService() {
        UUID clientId = UUID.randomUUID();
        GetReportResponse response = new GetReportResponse();
        when(service.getReport("2026-03-08", clientId)).thenReturn(response);

        assertSame(response, controller.getAll("2026-03-08", clientId.toString()));
        verify(service).getReport("2026-03-08", clientId);
    }
}
