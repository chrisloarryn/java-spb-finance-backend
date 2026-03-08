package accounttransaction.business.concretes;

import accounttransaction.business.abstracts.ReportService;
import accounttransaction.business.dto.responses.get.GetAllMovementsResponse;
import accounttransaction.business.dto.responses.get.GetClientResponse;
import accounttransaction.business.dto.responses.get.GetReportResponse;
import accounttransaction.entities.Account;
import accounttransaction.entities.AccountNotFoundException;
import accounttransaction.entities.Movement;
import accounttransaction.exceptions.UnparseableDateException;
import accounttransaction.repository.AccountRepository;
import accounttransaction.repository.MovementRepository;
import accounttransaction.utils.mappers.ModelMapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportManager implements ReportService {

    private final MovementRepository repo;
    private final AccountRepository accountRepo;
    private final ModelMapperService mapper;
    private final RestTemplate restTemplate;

    @Value("${client.person.base-url:http://client-person:1203}")
    private String clientPersonBaseUrl;


    @Override
    public GetReportResponse getReport(String date, UUID clientId) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(date);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            Date endDate = calendar.getTime();

            String url = clientPersonBaseUrl + "/api/clients/" + clientId;

            GetClientResponse externalAccountDetails = restTemplate.getForObject(url, GetClientResponse.class);

            if (externalAccountDetails == null) {
                throw new AccountNotFoundException("Client " + clientId + " was not found");
            }

            List<Account> accounts = accountRepo.findByPersonId(externalAccountDetails.getId())
                    .orElseThrow(() -> new AccountNotFoundException("No accounts were found for client " + clientId));

            List<Movement> allMovements = accounts.stream()
                    .flatMap(account -> repo.findByAccountNumber(account.getAccountNumber())
                            .orElseThrow(() -> new AccountNotFoundException("No movements were found for account " + account.getAccountNumber()))
                            .stream())
                    .filter(movement -> (movement.getCreatedAt().after(startDate) || movement.getCreatedAt().equals(startDate)) && (movement.getCreatedAt().before(endDate) || movement.getCreatedAt().equals(endDate)))
                    .toList();

            List<GetAllMovementsResponse> mapped = allMovements.stream().map(movement -> mapper.forResponse().map(movement, GetAllMovementsResponse.class)).collect(Collectors.toList());

            GetReportResponse report = new GetReportResponse();
            report.setData(mapped);
            report.setDate(endDate);
            report.setResults(allMovements.size());

            return report;

        } catch (ParseException e) {
            throw new UnparseableDateException("Date format is not valid");
        }
    }
}
