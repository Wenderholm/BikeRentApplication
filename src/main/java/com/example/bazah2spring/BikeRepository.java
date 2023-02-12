package com.example.bazah2spring;

import jakarta.persistence.EntityManager;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


interface BikeRepository extends CrudRepository<Bike, Long> {
    Optional<Bike> findBySerialNoIgnoreCase(String serialNo);
    int countAllByBorrowerIdIsNotNull();
    List<Bike> findAllByBorrowerIdIsNullOrderByDayPrice();
}

// POWYŻSZY KOD MOŻE ZOSTAĆ UŻYTY ZAMIAT PONIŻSZEGO
//@Repository
//public class BikeRepository {
//    private final EntityManager entityManager;
//
//    public BikeRepository(EntityManager entityManager) {
//        this.entityManager = entityManager;
//    }
//
//    public void save(Bike bike){
////        jezeli obiekt Bike o podanym id istnieje
//        if(existObject(bike)){
////            umozliwia dodanie obiektu lub zaktualizowanie
//            entityManager.merge(bike);
//        }else{
//            entityManager.persist(bike);
//        }
//    }
////        jezeli obiekt Bike o podanym id istnieje
//    private boolean existObject(Bike bike) {
//        return entityManager.find(Bike.class, bike.getId()) != null;
//    }
//
//    public Optional<Bike> findById(Long id){
////        find(jaki obiekt bedzie szukany, id- po czym bedzie szukany)
////        uzywamy optionala bo moze się zdarzyć ze pobieramy id bike ktorego nie ma wtedy bedzie null
//        return Optional.ofNullable(entityManager.find(Bike.class, id));
//    }
//    public void deleteById(Long id){
////        findById(id).ifPresent(ele -> entityManager.remove(ele));
//        findById(id).ifPresent(entityManager::remove);
//    }
//}
