package dh.pi.cardservice.service;

import dh.pi.cardservice.exception.BadRequestException;
import dh.pi.cardservice.exception.ResourceNotFoundException;
import dh.pi.cardservice.entity.Card;
import dh.pi.cardservice.repository.CardRepository;
import dh.pi.cardservice.service.interfaces.ICardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CardService implements ICardService {
    private CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public List<Card> getAll(Integer accountId, String type) {
        if(accountId == null && type == null){
            return this.cardRepository.findAll();
        }
        if(accountId != null && type != null){
            return this.cardRepository.findAllByAccountIdAndType(accountId, type);
        }
        return this.cardRepository.findAllByAccountIdOrType(accountId, type);
    }

    @Override
    public List<Card> getAllByAccountId(Integer accountId) {
        return this.cardRepository.findAllByAccountId(accountId);
    }

    @Override
    public List<Card> getAllByType(String type) {
        return this.cardRepository.findAllByType(type);
    }

    @Override
    public Optional<Card> getById(Integer id) throws ResourceNotFoundException {
        Optional<Card> cardFound = cardRepository.findById(id);
        if (cardFound.isEmpty()){
            throw new ResourceNotFoundException("No se encontro card con ese id");
        }else{
            return cardFound;
        }
    }

    @Override
    public Card create(Card card) throws BadRequestException {
        if((card.getType()== null || card.getType().isEmpty()) && (card.getAccountId()== null) ) {
            throw new BadRequestException("Debe completar todos los campos");
        } else  {
            if (card.getAccountId() == null) {
                throw new BadRequestException("Falta completar el campo AccountId");
            }
            if (card.getType() == null || card.getType().isEmpty()) {
                throw new BadRequestException("Falta completar el campo Type");
            }
            if (card.getCardNumber() == null) {
                throw new BadRequestException("Falta completar el campo CardNumber");
            }
            if (card.getOwner() == null) {
                throw new BadRequestException("Falta completar el campo Owner");
            }
            if (card.getSecurityNumber() == null) {
                throw new BadRequestException("Falta completar el campo SecurityNumber");
            }
            if (card.getExpirationDate() == null) {
                throw new BadRequestException("Falta completar el campo ExpirationDate");
            }
            if (card.getBalance() == null) {
                throw new BadRequestException("Falta completar el campo Balance");
            }
        }

        return this.cardRepository.save(card);
    }

    public boolean validarVacio(String cadena){
        return cadena.trim().isEmpty();
    }

    @Override
    public Card update(Card card, Integer id) throws ResourceNotFoundException, BadRequestException {
        //return this.cardRepository.save(card);
        Optional<Card> cardUpdate = cardRepository.findById(id);
        if(card.getType() == null){
            throw new BadRequestException("Falta completar el campo tipo");
        }
        if(card.getAccountId() == null){
            throw new BadRequestException("Falta completar el campo account id");
        }
        if(cardUpdate.isPresent()){
            if(card.getType() != null ) {
                if (validarVacio(card.getType())) {
                    throw new BadRequestException("No se puede actualizar tipo por un campo vacio");
                } else if (card.getType() != cardUpdate.get().getType()) {
                    cardUpdate.get().setType(card.getType());
                }
            }

            if(card.getAccountId() != null && card.getAccountId() != cardUpdate.get().getAccountId()){
                cardUpdate.get().setAccountId(card.getAccountId());
            }

            cardUpdate.get().setBalance(card.getBalance());

            return cardRepository.save(cardUpdate.get());
        }else{
            throw new ResourceNotFoundException("No se encontro tarjeta con este id");
        }
    }

    @Override
    public void delete(Integer id) {
        this.cardRepository.deleteById(id);
    }

    @Override
    public List<Card> getAllByOwner(String owner) throws ResourceNotFoundException, BadRequestException {
        return this.cardRepository.findAllByOwner(owner);
    }

    @Override
    public List<Card> getOnlyLastNumbers(Integer accountId) throws ResourceNotFoundException, BadRequestException {
        List<Card> listCards = cardRepository.findAllByAccountId(accountId);

        for (Card card : listCards) {
            String numbers = card.getCardNumber();
            String lastFourNumbers = numbers.substring(12, 16);
            System.out.println(lastFourNumbers);
            card.setLastNumbers(lastFourNumbers);
        }

        return listCards;
    }

    @Override
    public Card getCardByLastNumbers(String lastNumbers) throws ResourceNotFoundException, BadRequestException {
        return cardRepository.findAllByLastNumbers(lastNumbers);
    }
}
