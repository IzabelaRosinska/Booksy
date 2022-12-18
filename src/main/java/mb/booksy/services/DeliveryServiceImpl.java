package mb.booksy.services;

import mb.booksy.domain.order.delivery.Delivery;
import mb.booksy.domain.order.delivery.InpostBox;
import mb.booksy.repository.DeliveryPointRepository;
import mb.booksy.repository.InpostBoxRepository;
import mb.booksy.web.mapper.DeliveryPointMapper;
import mb.booksy.web.mapper.InpostBoxMapper;
import mb.booksy.web.model.DeliveryPointDto;
import mb.booksy.web.model.InpostBoxDto;
import mb.booksy.web.model.ItemDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    private final InpostBoxRepository inpostBoxRepository;
    private final DeliveryPointRepository deliveryPointRepository;
    private final InpostBoxMapper boxMapper;
    private final DeliveryPointMapper pointMapper;

    public DeliveryServiceImpl(InpostBoxRepository inpostBoxRepository, DeliveryPointRepository deliveryPointRepository, InpostBoxMapper boxMapper, DeliveryPointMapper pointMapper) {
        this.inpostBoxRepository = inpostBoxRepository;
        this.deliveryPointRepository = deliveryPointRepository;
        this.boxMapper = boxMapper;
        this.pointMapper = pointMapper;
    }

    @Override
    public Set<Delivery> findAll() {
        return null;
    }

    @Override
    public Delivery findById(Long aLong) {
        return null;
    }

    @Override
    public Delivery save(Delivery object) {
        return null;
    }

    @Override
    public void delete(Delivery object) {

    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public List<InpostBoxDto> getAllInpostBoxes() {
        List<InpostBoxDto> boxes = inpostBoxRepository.findAll().stream()
                .map(box-> boxMapper.boxToBoxDto(box))
                .collect(Collectors.toList());
        return boxes;
    }

    @Override
    public List<DeliveryPointDto> getAllZabkaPoints() {
        List<DeliveryPointDto> points = deliveryPointRepository.findAll().stream()
                .filter(point -> point.getPointName().equals("Żabka"))
                .map(point-> pointMapper.pointToPointDto(point))
                .collect(Collectors.toList());
        return points;
    }

    @Override
    public List<DeliveryPointDto> getAllRuchPoints() {
        List<DeliveryPointDto> points = deliveryPointRepository.findAll().stream()
                .filter(point -> point.getPointName().equals("Kiosk RUCH"))
                .map(point-> pointMapper.pointToPointDto(point))
                .collect(Collectors.toList());
        return points;
    }
}
