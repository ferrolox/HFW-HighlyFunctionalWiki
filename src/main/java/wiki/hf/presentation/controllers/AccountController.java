package wiki.hf.presentation.controllers;

import wiki.hf.presentation.dataTransferObjects.*;
import wiki.hf.service.*;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import lombok.*;
import wiki.hf.service.exceptions.AccountException;

import java.net.*;
import java.util.*;

@RequiredArgsConstructor

@RestController
@RequestMapping("api/account")
public class AccountController {

    private final AccountService service;
    public static final String url = "/api/account";

    @GetMapping
    public HttpEntity<List<AccountResult>> getAccounts(@RequestParam Optional<String> name, @RequestParam Optional<String> username) {
        List<AccountResult> accountResult = service.findAllByName(name, username)
                                                   .stream()
                                                   .map(AccountResult::new)
                                                   .toList();

        return accountResult.isEmpty() ? ResponseEntity.noContent().build()
                                       : ResponseEntity.ok(accountResult);
    }

    /* TODO: Fix
    @GetMapping("/{username}/{password}")
    public HttpEntity<AccountResult> checkAccount(@PathVariable String username, @PathVariable String password) {
        return ResponseEntity.ok(new AccountResult(service.findByUsernameAndPassword(username, password)));
    }
     */

    @PutMapping("/update")
    public HttpEntity<AccountResult> putAccount(@RequestBody AccountRequest accountRequest) {
        service.update(accountRequest);

        return ResponseEntity.ok().body(new AccountResult(accountRequest));
    }

    @PostMapping("/register")
    public HttpEntity<AccountResult> postAccount(@RequestBody AccountRequest accountRequest) {
        service.create(accountRequest);
        URI location = URI.create("%s/%s".formatted(url, accountRequest.username()));

        return ResponseEntity.created(location).body(new AccountResult(accountRequest));
    }

    @DeleteMapping("/delete/{id}")
    public HttpEntity<Long> deleteAccount(@PathVariable Long id) {
        service.delete(id);

        return ResponseEntity.ok(id);
    }

    @ExceptionHandler(AccountException.class)
    public HttpEntity<ProblemDetail> handleException(AccountException exception) {
        ProblemDetail problemDetail;
        HttpStatus status;

        if (exception.getType().equals("WrongPassword") || exception.getType().equals("NoAccountExists")) { status = HttpStatus.NOT_FOUND; }
        else { status = HttpStatus.BAD_REQUEST; }

        problemDetail = ProblemDetail.forStatusAndDetail(status, exception.getMessage());
        problemDetail.setProperty("object", exception.getObject());

        return ResponseEntity.of(problemDetail).build();
    }
}