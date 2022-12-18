package mb.booksy.services;

import mb.booksy.domain.order.Order;
import mb.booksy.domain.order.delivery.*;
import mb.booksy.repository.*;
import mb.booksy.web.mapper.DeliveryPointMapper;
import mb.booksy.web.mapper.InpostBoxMapper;
import mb.booksy.web.model.CourierDeliveryDto;
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
    private final InpostDeliveryRepository inpostDeliveryRepository;
    private final DeliveryPointRepository deliveryPointRepository;
    private final DeliveryRepository deliveryRepository;
    private final CourierDeliveryRepository courierDeliveryRepository;
    private final PointDeliveryRepository pointDeliveryRepository;
    private final InpostBoxMapper boxMapper;
    private final DeliveryPointMapper pointMapper;

    public DeliveryServiceImpl(InpostBoxRepository inpostBoxRepository, DeliveryPointRepository deliveryPointRepository, InpostBoxMapper boxMapper, DeliveryPointMapper pointMapper, DeliveryRepository deliveryRepository, InpostDeliveryRepository inpostDeliveryRepository, CourierDeliveryRepository courierDeliveryRepository, PointDeliveryRepository pointDeliveryRepository) {
        this.inpostBoxRepository = inpostBoxRepository;
        this.deliveryPointRepository = deliveryPointRepository;
        this.deliveryRepository = deliveryRepository;
        this.inpostDeliveryRepository = inpostDeliveryRepository;
        this.courierDeliveryRepository = courierDeliveryRepository;
        this.pointDeliveryRepository = pointDeliveryRepository;
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
    public void saveInpostDelivery(InpostBoxDto inpostBoxDto) {
        List<InpostDelivery> deliveries = deliveryRepository.findLastInpostDeliveryIndex();
        Long lastId = 1L;
        if(deliveries.size() != 0)
            lastId = deliveries.get(0).getId() + 1;
        InpostBox box = inpostBoxRepository.findAll().stream().filter(b -> (b.getId() == inpostBoxDto.getId())).collect(Collectors.toList()).get(0);
        InpostDelivery inpost = InpostDelivery.builder().id(lastId).build();
        inpost.setInpostBox(box);
        Delivery delivery = Delivery.builder().id(getNewId()).build();
        delivery.setInpostDelivery(inpost);
        inpostDeliveryRepository.save(inpost);
        deliveryRepository.save(delivery);
    }

    @Override
    public void saveCourierDelivery(CourierDeliveryDto courierDeliveryDto) {
        List<CourierDelivery> deliveries = deliveryRepository.findLastCourierDeliveryIndex();
        Long lastId = 1L;
        if(deliveries.size() != 0)
            lastId = deliveries.get(0).getId() + 1;
        CourierDelivery courier = CourierDelivery.builder().id(lastId).deliveryAddress(courierDeliveryDto.getAddress1() + ", " + courierDeliveryDto.getAddress2() + " " + courierDeliveryDto.getAddress3()).build();
        Delivery delivery = Delivery.builder().id(getNewId()).build();
        delivery.setCourierDelivery(courier);
        courierDeliveryRepository.save(courier);
        deliveryRepository.save(delivery);
    }

    @Override
    public void savePointDelivery(DeliveryPointDto deliveryPointDto) {
        List<PointDelivery> deliveries = deliveryRepository.findLastPointDeliveryIndex();
        Long lastId = 1L;
        if(deliveries.size() != 0)
            lastId = deliveries.get(0).getId() + 1;
        DeliveryPoint point = deliveryPointRepository.findAll().stream().filter(b -> (b.getId() == deliveryPointDto.getId())).collect(Collectors.toList()).get(0);
        PointDelivery pointDl = PointDelivery.builder().id(lastId).build();
        pointDl.setDeliveryPoint(point);
        Delivery delivery = Delivery.builder().id(getNewId()).build();
        delivery.setPointDelivery(pointDl);
        pointDeliveryRepository.save(pointDl);
        deliveryRepository.save(delivery);
    }

    private Long getNewId() {
        List<Delivery> deliveries = deliveryRepository.findLastIndex();
        Long lastId = 1L;
        if(deliveries.size() != 0)
            lastId = deliveries.get(0).getId() + 1;
        return lastId;
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
