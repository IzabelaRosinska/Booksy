package mb.booksy.services;

import mb.booksy.domain.order.payment.Payment;
import mb.booksy.domain.order.payment.PaymentType;
import mb.booksy.domain.user.Client;
import mb.booksy.repository.OrderRepository;
import mb.booksy.repository.PaymentRepository;
import mb.booksy.web.mapper.PaymentMapper;
import mb.booksy.web.model.PaymentDto;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapper paymentMapper;
    private final ItemService itemService;
    private final UserAuthenticationService userAuthenticationService;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentServiceImpl(ItemService itemService, UserAuthenticationService userAuthenticationService, PaymentMapper paymentMapper, PaymentRepository paymentrepository, OrderRepository orderRepository) {
        this.itemService = itemService;
        this.userAuthenticationService = userAuthenticationService;
        this.paymentMapper = paymentMapper;
        this.paymentRepository = paymentrepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public List<String> findPaymentImage() {
        List<String> images = new ArrayList<>();
        images.add(createImage("src/main/resources/static/resources/images/payu.jpg"));
        images.add(createImage("src/main/resources/static/resources/images/blik.png"));
        return images;
    }

    @Override
    public List<String> findBankImage() {
        List<String> images = new ArrayList<>();
        images.add(createImage("src/main/resources/static/resources/images/mbank.png"));
        images.add(createImage("src/main/resources/static/resources/images/db.jpg"));
        images.add(createImage("src/main/resources/static/resources/images/alior.png"));
        images.add(createImage("src/main/resources/static/resources/images/milenium.jpg"));
        images.add(createImage("src/main/resources/static/resources/images/natwest.png"));
        images.add(createImage("src/main/resources/static/resources/images/ubs.png"));
        images.add(createImage("src/main/resources/static/resources/images/pocztowy.png"));
        images.add(createImage("src/main/resources/static/resources/images/pko.png"));
        images.add(createImage("src/main/resources/static/resources/images/citi.png"));
        return images;
    }

    public String createImage(String path) {
        byte[] array = null;
        String itemPhoto = "";
        try {
            array = Files.readAllBytes(Paths.get(path));
            itemPhoto = new String(Base64.encodeBase64(array), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return itemPhoto;
    }

    @Override
    public PaymentDto createBlikPayment() {
        Client user = (Client)userAuthenticationService.getAuthenticatedUser();

        Payment payment = Payment.builder().paymentDate(LocalDate.now()).paymentType(PaymentType.BLIK).build();
        payment.setAmount(itemService.countDiscount(user.getId()));
        paymentRepository.save(payment);

        PaymentDto dto = paymentMapper.paymentToPaymentDto(payment);
        dto.setRachNadawca(user.getAccount());
        dto.setOdbiorca("Booksy Sp. z o.o.");
        dto.setRachOdbiorca("57 1111 2222 3333 4444 5555 6666");
        return dto;
    }

    @Override
    @Transactional
    public void updateOrder(PaymentDto paymentDto) {
        Client user = (Client)userAuthenticationService.getAuthenticatedUser();
        Long orderId = orderRepository.getOrderId(user.getId()).get(0).getId();
        orderRepository.savePayment(paymentDto.getId(), orderId);
    }

    @Override
    public PaymentDto createPayUPayment(PaymentDto paymentDto) {
        Client user = (Client)userAuthenticationService.getAuthenticatedUser();

        Payment payment = Payment.builder().paymentDate(LocalDate.now()).paymentType(PaymentType.PAYU).build();
        payment.setAmount(itemService.countDiscount(user.getId()));
        paymentRepository.save(payment);

        PaymentDto dto = paymentMapper.paymentToPaymentDto(payment);
        dto.setRachNadawca(user.getAccount());
        dto.setOdbiorca("Booksy Sp. z o.o.");
        dto.setRachOdbiorca("57 1111 2222 3333 4444 5555 6666");
        return dto;
    }

    @Override
    public Set<Payment> findAll() {
        return null;
    }

    @Override
    public Payment findById(Long aLong) {
        return null;
    }

    @Override
    public Payment save(Payment object) {
        return null;
    }

    @Override
    public void delete(Payment object) {}

    @Override
    public void deleteById(Long aLong) {}
}
