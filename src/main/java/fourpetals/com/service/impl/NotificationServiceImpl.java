package fourpetals.com.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fourpetals.com.entity.Notification;
import fourpetals.com.enums.NotificationType;
import fourpetals.com.repository.NotificationRepository;
import fourpetals.com.service.NotificationService;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Notification createNotification(Integer nguoiNhanId, Integer donHangId, NotificationType loaiThongBao, String noiDung) {
        Notification n = new Notification();
        n.setNguoiNhanId(nguoiNhanId);
        n.setDonHangId(donHangId);
        n.setLoaiThongBao(loaiThongBao);
        n.setNoiDung(noiDung);
        n.setDaDoc(false);
        return notificationRepository.save(n);
    }

    @Override
    public List<Notification> getAllByUser(Integer nguoiNhanId) {
        return notificationRepository.findByNguoiNhanIdOrderByNgayTaoDesc(nguoiNhanId);
    }

    @Override
    public List<Notification> getUnreadByUser(Integer nguoiNhanId) {
        return notificationRepository.findByNguoiNhanIdAndDaDocFalseOrderByNgayTaoDesc(nguoiNhanId);
    }

    @Override
    public void markAsRead(Integer notificationId) {
        Optional<Notification> nOpt = notificationRepository.findById(notificationId);
        if(nOpt.isPresent()) {
            Notification n = nOpt.get();
            n.setDaDoc(true);
            notificationRepository.save(n);
        }
    }

    @Override
    public void deleteNotification(Integer notificationId) {
        notificationRepository.deleteById(notificationId);
    }
}
