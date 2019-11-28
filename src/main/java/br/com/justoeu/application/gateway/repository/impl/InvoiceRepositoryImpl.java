package br.com.justoeu.application.gateway.repository.impl;

import br.com.justoeu.application.gateway.repository.InvoiceRepository;
import br.com.justoeu.application.gateway.repository.model.InvoiceEntity;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InvoiceRepositoryImpl implements InvoiceRepository {

    private Map<Long, InvoiceEntity> invoices;

    @Override
    public void create(InvoiceEntity entity) {

        invoices.put(entity.getId(), entity);
    }

    @Override
    public void update(InvoiceEntity entity) {

        invoices.put(entity.getId(), entity);
    }

    @Override
    public InvoiceEntity get(Long id) {

        if (invoices == null) {
            initializeDB();
        }

        Map<Long, InvoiceEntity> entity = filterByValue(invoices, x -> x.getId().equals(id));
        return entity.size() > 0 ? entity.get(id) : null;

    }

    public static <K, V> Map<K, V> filterByValue(Map<K, V> map, Predicate<V> predicate) {
        return map.entrySet()
                .stream()
                .filter(x -> predicate.test(x.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    private void initializeDB() {
        invoices = new HashMap<>();
    }
}
