package mb.booksy.services;

import mb.booksy.domain.order.payment.Payment;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class PaymentServiceImpl implements PaymentService {
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
    public void delete(Payment object) {

    }

    @Override
    public void deleteById(Long aLong) {

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
}
