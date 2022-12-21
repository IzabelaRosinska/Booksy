package mb.booksy.services;

import mb.booksy.domain.order.delivery.*;
import mb.booksy.repository.*;
import mb.booksy.web.mapper.DeliveryPointMapper;
import mb.booksy.web.mapper.InpostBoxMapper;
import mb.booksy.web.model.CourierDeliveryDto;
import mb.booksy.web.model.DeliveryPointDto;
import mb.booksy.web.model.InpostBoxDto;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    private final InpostBoxMapper inpostBoxMapper;
    private final InpostBoxRepository inpostBoxRepository;
    private final InpostDeliveryRepository inpostDeliveryRepository;
    private final DeliveryPointRepository deliveryPointRepository;
    private final DeliveryRepository deliveryRepository;
    private final CourierDeliveryRepository courierDeliveryRepository;
    private final PointDeliveryRepository pointDeliveryRepository;
    private final DeliveryPointMapper deliveryPointMapper;

    public DeliveryServiceImpl(InpostBoxRepository inpostBoxRepository, DeliveryPointRepository deliveryPointRepository, InpostBoxMapper inpostBoxMapper, DeliveryPointMapper deliveryPointMapper, DeliveryRepository deliveryRepository, InpostDeliveryRepository inpostDeliveryRepository, CourierDeliveryRepository courierDeliveryRepository, PointDeliveryRepository pointDeliveryRepository) {
        this.inpostBoxRepository = inpostBoxRepository;
        this.deliveryPointRepository = deliveryPointRepository;
        this.deliveryRepository = deliveryRepository;
        this.inpostDeliveryRepository = inpostDeliveryRepository;
        this.courierDeliveryRepository = courierDeliveryRepository;
        this.pointDeliveryRepository = pointDeliveryRepository;
        this.inpostBoxMapper = inpostBoxMapper;
        this.deliveryPointMapper = deliveryPointMapper;
    }

    @Override
    public void saveInpostDelivery(InpostBoxDto inpostBoxDto) {
        InpostBox inpostBox = inpostBoxRepository.findByBoxId(inpostBoxDto.getId());
        InpostDelivery inpostDelivery = InpostDelivery.builder().inpostBox(inpostBox).build();
        Delivery delivery = Delivery.builder().inpostDelivery(inpostDelivery).build();
        inpostDeliveryRepository.save(inpostDelivery);
        deliveryRepository.save(delivery);
    }

    @Override
    public void saveCourierDelivery(CourierDeliveryDto courierDeliveryDto) {
        CourierDelivery courierDelivery = CourierDelivery.builder().deliveryAddress(courierDeliveryDto.getAddress1() + ", " + courierDeliveryDto.getAddress2() + " " + courierDeliveryDto.getAddress3()).build();
        Delivery delivery = Delivery.builder().courierDelivery(courierDelivery).build();
        courierDeliveryRepository.save(courierDelivery);
        deliveryRepository.save(delivery);
    }

    @Override
    public void savePointDelivery(DeliveryPointDto deliveryPointDto) {
        DeliveryPoint deliveryPoint = deliveryPointRepository.findByPointId(deliveryPointDto.getId());
        PointDelivery pointDelivery = PointDelivery.builder().deliveryPoint(deliveryPoint).build();
        Delivery delivery = Delivery.builder().pointDelivery(pointDelivery).build();
        pointDeliveryRepository.save(pointDelivery);
        deliveryRepository.save(delivery);
    }

    @Override
    public List<InpostBoxDto> getAllInpostBoxes() {
        List<InpostBoxDto> inpostBoxes = inpostBoxRepository.findAll().stream()
                .map(box-> inpostBoxMapper.boxToBoxDto(box))
                .collect(Collectors.toList());
        return inpostBoxes;
    }

    @Override
    public List<DeliveryPointDto> getAllZabkaPoints() {
        List<DeliveryPointDto> deliveryPoints = deliveryPointRepository.findAll().stream()
                .filter(point -> point.getPointName().equals("Żabka"))
                .map(point-> deliveryPointMapper.pointToPointDto(point))
                .collect(Collectors.toList());
        return deliveryPoints;
    }

    @Override
    public List<DeliveryPointDto> getAllRuchPoints() {
        List<DeliveryPointDto> deliveryPoints = deliveryPointRepository.findAll().stream()
                .filter(point -> point.getPointName().equals("Kiosk RUCH"))
                .map(point-> deliveryPointMapper.pointToPointDto(point))
                .collect(Collectors.toList());
        return deliveryPoints;
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
    public void delete(Delivery object) {}

    @Override
    public void deleteById(Long aLong) {}
}
