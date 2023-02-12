package com.example.bazah2spring;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BikeService {
    private final BikeRepository bikeRepository;

    public BikeService(BikeRepository bikeRepository) {
        this.bikeRepository = bikeRepository;
    }
    @Transactional
//    @Transactional - potrzebne tam gdzie modyfikujemy obiekt dodajemy, update, lub usuwamy
//    metoda ta musi być publiczna jak nie to błąd TransactionRequiredException
    public void add(BikeDto newBike){
        Bike bike = new Bike(newBike.getId(),
                newBike.getName(),
                newBike.getSerialNo(),
                newBike.getHourPrice(),
                newBike.getDayPrice());
        bikeRepository.save(bike);
    }
    @Transactional
    public void deleteById(Long bikeId){
        bikeRepository.deleteById(bikeId);
    }
    @Transactional
    public double rentForHours(String serialNo, int hours, String borrowerId){
//        wpisujemy kiedy rower powinien byc oddany czyli czas tera plus ilosc godzin wypozyczenia
        LocalDateTime dateOfReturnBike = LocalDateTime.now().plusHours(hours);
        Bike bike = updateBike(serialNo, borrowerId, dateOfReturnBike);
        return bike.getHourPrice() * hours;
    }
    @Transactional
    public double rentForDay(String serialNo, String borrowerId){
        LocalDateTime dateOfReturnBike = LocalDateTime.now().withHour(23).withMinute(59);
        Bike bike = updateBike(serialNo, borrowerId, dateOfReturnBike);
        return bike.getDayPrice();
    }
    @Transactional
    public void returnBike(String serialNo){
        updateBike(serialNo,null,null);
    }

    private Bike updateBike(String serialNo, String borrowerId, LocalDateTime dateOfReturnBike) {
//        wyszukujemy rower po id aby uaktualnic date i czas zwrotu
//        jezeli rower po id istnieje to zwraca obiekt bike jezeli nie to wyrzuca nam wyjatek stworzony przez nas
        Bike bike = bikeRepository.findBySerialNoIgnoreCase(serialNo)
                .orElseThrow(BikeNotFoundException::new);
//        ustawiamy date powrotu
        bike.setDateOfReturn(dateOfReturnBike);
//        ustawiamy dane wyporzyczajacego
        bike.setBorrowerId(borrowerId);
        return bike;
    }

    public int countBorrowedBikes(){
        return bikeRepository.countAllByBorrowerIdIsNotNull();
    }

    public List<BikeDto> findAllAvailableBikes(){
        return bikeRepository.findAllByBorrowerIdIsNullOrderByDayPrice().stream()
                .map(bike -> new BikeDto(
                        bike.getId(),
                        bike.getName(),
                        bike.getSerialNo(),
                        bike.getHourPrice(),
                        bike.getDayPrice())
                ).collect(Collectors.toList());
    }
}

