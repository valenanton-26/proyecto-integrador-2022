package dh.pi.transactionservice.controller;

import dh.pi.transactionservice.exception.BadRequestException;
import dh.pi.transactionservice.exception.ResourceNotFoundException;
import dh.pi.transactionservice.entity.Transaction;
import dh.pi.transactionservice.service.interfaces.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transaction")
public class TransactionController {



        private ITransactionService transactionService;

        @Autowired
        public TransactionController(ITransactionService transactionService) {
            this.transactionService = transactionService;
        }

        @GetMapping
        public ResponseEntity<List<Transaction>> getAllTransactions(){
            return ResponseEntity.ok().body(transactionService.getAll());
        }

        @GetMapping("/{id}")
        public ResponseEntity<Transaction> getById(@PathVariable Integer id){
            Optional<Transaction> optionalTransaction = transactionService.getById(id);
            if(optionalTransaction.isPresent()){
                return ResponseEntity.ok().body(optionalTransaction.get());
            }
            return (ResponseEntity<Transaction>) ResponseEntity.notFound();
        }

        @GetMapping("/{transactionId}/{accountId}")
        public ResponseEntity<Transaction> getByAccountAndTransactionId(@PathVariable(name = "accountId") Integer accountId, @PathVariable(name = "transactionId") Integer transactionId) throws BadRequestException, ResourceNotFoundException {
            Optional<Transaction> optionalTransaction = transactionService.getByAccountAndTransactionId(accountId, transactionId);

            if(optionalTransaction.isPresent()){
                return ResponseEntity.ok().body(optionalTransaction.get());
            }

            return (ResponseEntity<Transaction>) ResponseEntity.notFound();
        }

        @GetMapping("/accountId/{accountId}")
        public ResponseEntity<List<Transaction>> getAllByAccountId(@PathVariable(name = "accountId") Integer accountId) throws BadRequestException, ResourceNotFoundException {

            Optional<List<Transaction>> optionalTransactions = transactionService.getAllByAccountId(accountId);

            if(optionalTransactions.isPresent()){
                return ResponseEntity.ok().body(optionalTransactions.get());
            }

            return (ResponseEntity<List<Transaction>>) ResponseEntity.notFound();
        }

    @GetMapping("/accountId/lastTen/{accountId}")
    public ResponseEntity<List<Transaction>> getLastTenByAccountId(@PathVariable(name = "accountId") Integer accountId) throws BadRequestException, ResourceNotFoundException {

        Optional<List<Transaction>> optionalTransactions = transactionService.getLastTenByAccountId(accountId);

        if(optionalTransactions.isPresent()){
            return ResponseEntity.ok().body(optionalTransactions.get());
        }

        return (ResponseEntity<List<Transaction>>) ResponseEntity.notFound();
    }

        @PostMapping
        public ResponseEntity<Transaction> create(@RequestBody Transaction Transaction){
            return  ResponseEntity.ok().body(transactionService.create(Transaction));
        }

        @PutMapping("/{id}")
        public ResponseEntity<Transaction> update(@RequestBody Transaction Transaction, @PathVariable Integer id){
            try {
                Optional<Transaction> transaction = transactionService.getById(id);
                if (transaction.isEmpty()){
                    return ResponseEntity.badRequest().build();
                }
                if (Transaction.getId() == null) {
                    Transaction.setId(id);
                }
                return ResponseEntity.ok().body(transactionService.update(Transaction));
            }catch (Exception e){
                return ResponseEntity.badRequest().build();
            }
        }

        @DeleteMapping("/{id}")
        public ResponseEntity delete(@PathVariable Integer id){
            transactionService.delete(id);
            return  ResponseEntity.noContent().build();
        }



    }
