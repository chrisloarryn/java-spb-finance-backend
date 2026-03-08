package accounttransaction.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import accounttransaction.business.concretes.ReportManager;
import accounttransaction.business.dto.responses.get.GetAllMovementsResponse;
import accounttransaction.business.dto.responses.get.GetClientResponse;
import accounttransaction.entities.Account;
import accounttransaction.entities.AccountNotFoundException;
import accounttransaction.entities.Movement;
import accounttransaction.entities.enums.AccountType;
import accounttransaction.exceptions.UnparseableDateException;
import accounttransaction.repository.AccountRepository;
import accounttransaction.repository.MovementRepository;
import accounttransaction.utils.mappers.ModelMapperService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ReportManagerTests {

    @Mock
    private MovementRepository movementRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ModelMapperService mapper;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ModelMapper responseMapper;

    private ReportManager manager;

    @BeforeEach
    void setUp() {
        manager = new ReportManager(movementRepository, accountRepository, mapper, restTemplate);
        ReflectionTestUtils.setField(manager, "clientPersonaBaseUrl", "http://127.0.0.1:19090");
        when(mapper.forResponse()).thenReturn(responseMapper);
    }

    @Test
    void getReportReturnsOnlyTheMovementsForTheRequestedDate() throws ParseException {
        UUID clientId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        GetClientResponse externalClient = new GetClientResponse();
        externalClient.setId(clientId);

        Account account = new Account();
        account.setAccountNumber("ACC-001");
        account.setPersonId(clientId);

        Movement includedMovement = new Movement();
        includedMovement.setAccountNumber("ACC-001");
        includedMovement.setCreatedAt(parse("2026-03-08 10:00:00"));

        Movement excludedMovement = new Movement();
        excludedMovement.setAccountNumber("ACC-001");
        excludedMovement.setCreatedAt(parse("2026-03-09 10:00:00"));

        GetAllMovementsResponse mappedMovement = new GetAllMovementsResponse();
        mappedMovement.setAccountNumber("ACC-001");

        when(restTemplate.getForObject(
                "http://127.0.0.1:19090/api/clients/" + clientId,
                GetClientResponse.class))
                .thenReturn(externalClient);
        when(accountRepository.findByPersonId(clientId)).thenReturn(Optional.of(List.of(account)));
        when(movementRepository.findByAccountNumber("ACC-001")).thenReturn(Optional.of(List.of(includedMovement, excludedMovement)));
        when(responseMapper.map(includedMovement, GetAllMovementsResponse.class)).thenReturn(mappedMovement);

        var report = manager.getReport("2026-03-08", clientId);

        assertEquals(1, report.getResults());
        assertEquals(1, report.getData().size());
        assertSame(mappedMovement, report.getData().getFirst());
    }

    @Test
    void getReportRejectsInvalidDates() {
        assertThrows(
                UnparseableDateException.class,
                () -> manager.getReport("2026/03/08", UUID.randomUUID()));
    }

    @Test
    void getReportRejectsUnknownExternalClients() {
        UUID clientId = UUID.randomUUID();
        when(restTemplate.getForObject(
                "http://127.0.0.1:19090/api/clients/" + clientId,
                GetClientResponse.class))
                .thenReturn(null);

        assertThrows(AccountNotFoundException.class, () -> manager.getReport("2026-03-08", clientId));
    }

    private Date parse(String value) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value);
    }
}
