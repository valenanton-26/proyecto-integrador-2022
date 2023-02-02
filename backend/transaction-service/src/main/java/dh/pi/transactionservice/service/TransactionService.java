package dh.pi.transactionservice.service;

import dh.pi.transactionservice.exception.BadRequestException;
import dh.pi.transactionservice.exception.ResourceNotFoundException;
import dh.pi.transactionservice.entity.Transaction;
import dh.pi.transactionservice.repository.TransactionRepository;
import dh.pi.transactionservice.service.interfaces.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService implements ITransactionService {
    private TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<Transaction> getAll() {
        return this.transactionRepository.findAll();
    }

    @Override
    public Optional<Transaction> getById(Integer id) {
        return this.transactionRepository.findById(id);
    }

    @Override
    public Optional<Transaction> getByAccountAndTransactionId(Integer accountId, Integer transactionId) throws BadRequestException, ResourceNotFoundException {

        Transaction transaction = transactionRepository.getByAccountAndTransactionId(transactionId,accountId);

        if (transaction == null) {
            throw new ResourceNotFoundException("No se encontro transferencia con este id");
        }

        return Optional.of(transaction);
    }

    @Override
    public Optional<List<Transaction>> getAllByAccountId(Integer id) throws BadRequestException, ResourceNotFoundException {

        List<Transaction> transactions = transactionRepository.getAllByAccountId(id);

        if (transactions == null) {
            throw new ResourceNotFoundException("No se encontraton transferencias con este id");
        }

        return Optional.of(transactions);
    }

    @Override
    public Optional<List<Transaction>> getLastTenByAccountId(Integer id) throws BadRequestException, ResourceNotFoundException {

        List<Transaction> transactions = transactionRepository.getLastTenByAccountId(id, Pageable.ofSize(10));

        if (transactions == null) {
            throw new ResourceNotFoundException("No se encontraton transferencias con este id");
        }

        return Optional.of(transactions);
    }

    @Override
    public Transaction create(Transaction transaction) {
        return this.transactionRepository.save(transaction);
    }

    @Override
    public Transaction update(Transaction transaction) {
        return this.transactionRepository.save(transaction);
    }

    @Override
    public void delete(Integer id) {
        this.transactionRepository.deleteById(id);
    }

}
